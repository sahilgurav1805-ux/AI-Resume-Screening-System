package com.sahil.resume_checker.controller;


import com.sahil.resume_checker.dto.LoginRequest;
import com.sahil.resume_checker.dto.LoginResponse;
import com.sahil.resume_checker.dto.RegisterRequest;
import com.sahil.resume_checker.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService ;

    public AuthController(AuthService authService) {
        this.authService = authService ;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request) ;
    }

}
