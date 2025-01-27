package com.app.dharbor.inotes.web.rest;

import com.app.dharbor.inotes.common.Path;
import com.app.dharbor.inotes.dto.AuthLoginRequest;
import com.app.dharbor.inotes.dto.AuthResponse;
import com.app.dharbor.inotes.dto.AuthSignUpRequest;
import com.app.dharbor.inotes.service.implement.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    private final UserDetailsServiceImpl userDetailsService;

    public AuthenticationController(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping(Path.LOGIN)
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest){
        return new ResponseEntity<>(this.userDetailsService.loginUser(userRequest), HttpStatus.OK);
    }

    @PostMapping(Path.SIGNUP)
    public ResponseEntity<AuthResponse> signUp(@RequestBody @Valid AuthSignUpRequest userRequest){
        return new ResponseEntity<>(this.userDetailsService.createUser(userRequest), HttpStatus.CREATED);
    }
}
