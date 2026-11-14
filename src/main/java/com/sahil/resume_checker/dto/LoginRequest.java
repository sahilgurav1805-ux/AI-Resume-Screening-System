package com.sahil.resume_checker.dto;

public class LoginRequest {

    private String email;
    private String password ;

    public LoginRequest() {}

    public String getEmail() {
        return email ;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password ;
    }

}
