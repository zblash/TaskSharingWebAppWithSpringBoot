package com.tasksharing.tasksharing.controllers;

import com.tasksharing.tasksharing.Security.JWTAuthToken.JWTGenerator;
import com.tasksharing.tasksharing.models.User;
import com.tasksharing.tasksharing.services.Concrete.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Map;
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private JWTGenerator jwtGenerator;

    public UserController(JWTGenerator jwtGenerator) {
        this.jwtGenerator = jwtGenerator;
    }

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody User user){
        userService.Add(user);
        return new ResponseEntity<>(user, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody(required = true) Map<String,String> login){
        User userDetails = userService.findByUserName(login.get("username"));
        if (passwordEncoder.matches(login.get("password"),userDetails.getPassword())){
            String jwt = jwtGenerator.generate(userDetails);
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }

        return new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);

    }
}
