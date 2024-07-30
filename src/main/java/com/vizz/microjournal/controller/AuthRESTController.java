package com.vizz.microjournal.controller;

import com.vizz.microjournal.model.Users;
import com.vizz.microjournal.service.AuthRESTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/v1/auth")
public class AuthRESTController {

    AuthRESTService authRESTService;
    @Autowired
    AuthRESTController(AuthRESTService authRESTService){
        this.authRESTService = authRESTService;
    }

    @PostMapping("/sign-up")
    ResponseEntity<String> signUp(@RequestBody Users user){
        return authRESTService.signUpService(user);
    }

    @PostMapping("/sign-in")
    ResponseEntity<String> signIn(@RequestBody Users user){
        return authRESTService.signInService(user);
    }

    @PostMapping("/sign-out")
    ResponseEntity<String> signOut(@RequestBody Users user){
        return authRESTService.signOutService(user);
    }

    @DeleteMapping("/delete-account")
    ResponseEntity<String> deleteAccount(@RequestBody Users user){
        return authRESTService.deleteAccountService(user);
    }

    @GetMapping("/get-name")
    ResponseEntity<String> getName(@RequestParam String token){
        return authRESTService.getNameService(token);
    }
}