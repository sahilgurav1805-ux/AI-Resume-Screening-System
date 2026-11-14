package com.sahil.resume_checker.controller;


import com.sahil.resume_checker.dto.RegisterRequest;
import com.sahil.resume_checker.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AuthService authService ;

    public AdminController(AuthService authService) {
        this.authService =authService;
    }

    @PostMapping("/create-recruiter")
    public String createRecruiter(@RequestBody RegisterRequest request) {
        return authService.createRecruiter(request) ;
    }

}
