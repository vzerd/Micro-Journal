package com.vzerd.microjournal.Service;

import com.vzerd.microjournal.Model.UserModel;
import com.vzerd.microjournal.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    UserRepository userRepository;
    @Autowired
    AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Transactional
    public ResponseEntity<String> signUpService(UserModel user){

        if(user.getName() == null || user.getName().isBlank() ||
                user.getEmail() == null || user.getEmail().isBlank() ||
                user.getPassword() == null || user.getPassword().isBlank()){
            return new ResponseEntity<>("{\"payload\":\"Field(s) is/are incomplete or empty.\"}",
                    HttpStatus.valueOf(400));
        }
        user.trimName();
        user.trimEmail();
        user.trimPassword();
        user.encodePassword();
        try{
            String newEmail = user.getEmail();
            List<String> emails = userRepository.getAllEmails();
            for(String email : emails){
                if(newEmail.equals(email)){
                    return new ResponseEntity<>("{\"payload\":\"Given email is already present.\"}",
                            HttpStatus.valueOf(409));
                }
            }
            UUID newToken = generateNewToken();
            user.setToken(newToken);
            userRepository.save(user);
            return new ResponseEntity<>("{\"payload\":\"" + newToken + "\"}",
                    HttpStatus.valueOf(201));
        }catch(DataAccessException e){
            return new ResponseEntity<>("{\"payload\":\"Server error: sign-up failed - user persistence failed.\"}",
                    HttpStatus.valueOf(500));
        }
    }

    @Transactional
    public ResponseEntity<String> signInService(UserModel user){

        if(user.getEmail() == null || user.getEmail().isBlank() ||
                user.getPassword() == null || user.getPassword().isBlank()){
            return new ResponseEntity<>("{\"payload\":\"Field(s) is/are incomplete or empty.\"}",
                    HttpStatus.valueOf(406));
        }
        user.trimEmail();
        user.trimPassword();
        user.encodePassword();
        try{
            List<String> emails = userRepository.getAllEmails();
            for(String email : emails){
                if(user.getEmail().equals(email)){
                    if(user.getPassword().equals(userRepository.getPasswordByEmail(user.getEmail()))){
                        UUID newToken = generateNewToken();
                        int rowsAffected = userRepository.updateTokenByEmail(newToken, user.getEmail());
                        if(rowsAffected == 1){
                            return new ResponseEntity<>("{\"payload\":\"" + newToken.toString() + "\"}",
                                    HttpStatus.valueOf(201));
                        }
                        return new ResponseEntity<>("{\"payload\":\"Server error: sign-in failed - user data update failed.\"}",
                                HttpStatus.valueOf(500));
                    }
                    return new ResponseEntity<>("{\"payload\":\"Given password is incorrect.\"}",
                            HttpStatus.valueOf(406));
                }
            }
            return new ResponseEntity<>("{\"payload\":\"Given email is not associated with any accounts.\"}",
                    HttpStatus.valueOf(404));
        }catch(DataAccessException e){
            return new ResponseEntity<>("{\"payload\":\"Server error: sign-in failed - user data fetch and update failed.\"}",
                    HttpStatus.valueOf(500));
        }
    }

    @Transactional
    public ResponseEntity<String> signOutService(UserModel user){

        if(user.getToken() == null || user.getToken().toString().isBlank()){
            return new ResponseEntity<>("{\"payload\":\"Associated token is missing/empty in request-body.\"}",
                    HttpStatus.valueOf(406));
        }
        try{
            int rowsAffected = userRepository.setTokenToNull(user.getToken());
            if(rowsAffected == 1) {
                return new ResponseEntity<>("{\"payload\":\"success\"}",
                        HttpStatus.valueOf(201));
            }
            return new ResponseEntity<>("{\"payload\":\"Given token is not found.\"}",
                    HttpStatus.valueOf(404));
        }catch(DataAccessException e){
            return new ResponseEntity<>("{\"payload\":\"Server error: sign-out failed - user data fetch and update failed.\"}",
                    HttpStatus.valueOf(500));
        }
    }

    @Transactional
    public ResponseEntity<String> deleteAccountService(UserModel user){

        if(user.getToken() == null || user.getToken().toString().isBlank()){
            return new ResponseEntity<>("{\"payload\":\"Associated token is missing/empty in request-body.\"}",
                    HttpStatus.valueOf(406));
        }
        try{
            int rowsAffected = userRepository.deleteUserByToken(user.getToken());
            if(rowsAffected == 1){
                return new ResponseEntity<>("{\"payload\":\"success\"}",
                        HttpStatus.valueOf(200));
            }
            return new ResponseEntity<>("{\"payload\":\"Given token is not found.\"}",
                    HttpStatus.valueOf(404));
        }catch(DataAccessException e){
            return new ResponseEntity<>("{\"payload\":\"Server error: account deletion failed - user data fetch and update failed.\"}",
                    HttpStatus.valueOf(500));
        }
    }

    public ResponseEntity<String> getNameService(String token){

        if(token == null || token.isBlank()){
            return new ResponseEntity<>("{\"payload\":\"Associated token is missing/empty in request-body.\"}",
                    HttpStatus.valueOf(406));
        }
        try{
            String name = userRepository.getNameByToken(UUID.fromString(token));
            if(name != null){
                return new ResponseEntity<>("{\"payload\":\"" + name + "\"}",
                        HttpStatus.valueOf(200));
            }
            return new ResponseEntity<>("{\"payload\":\"Given token is not found.\"}",
                    HttpStatus.valueOf(404));

        }catch(DataAccessException e){
            return new ResponseEntity<>("{\"payload\":\"Server error: get name failed - user name fetch failed.\"}",
                    HttpStatus.valueOf(500));
        }
    }

    private UUID generateNewToken(){
        UUID token = UUID.randomUUID();
        List<UUID> tokens = userRepository.getAllTokens();
        for(int i = 0; i < tokens.size(); i++){
            if(tokens.get(i) != null && token.toString().equals(tokens.get(i).toString())){
                token = UUID.randomUUID();
                i = 0;
            }
        }
        return token;
    }
}