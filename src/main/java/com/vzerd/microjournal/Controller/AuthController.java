package com.vzerd.microjournal.Controller;

import com.vzerd.microjournal.Model.UserModel;
import com.vzerd.microjournal.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/v1/auth")
public class AuthController {

    AuthService authService;
    @Autowired
    AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    ResponseEntity<String> signUp(@RequestBody UserModel user){
        return authService.signUpService(user);
    }

    @PostMapping("/sign-in")
    ResponseEntity<String> signIn(@RequestBody UserModel user){
        return authService.signInService(user);
    }

    @PostMapping("/sign-out")
    ResponseEntity<String> signOut(@RequestBody UserModel user){
        return authService.signOutService(user);
    }

    @DeleteMapping("/delete-account")
    ResponseEntity<String> deleteAccount(@RequestBody UserModel user){
        return authService.deleteAccountService(user);
    }

    @GetMapping("/get-name")
    ResponseEntity<String> getName(@RequestParam String token){
        return authService.getNameService(token);
    }
}