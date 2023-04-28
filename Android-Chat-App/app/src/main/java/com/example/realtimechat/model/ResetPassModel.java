package com.example.realtimechat.model;

public class ResetPassModel {
    String email;
    String password;

    public ResetPassModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public ResetPassModel() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
