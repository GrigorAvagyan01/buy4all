package com.grish.buy4all4;

public class    UserModel {
    String username;
    String email;
    String password;
    String confirmPassword;
    public UserModel() {}

    public UserModel(String username, String email, String password, String confirmPassword) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String username) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String username) {
        this.password = password;
    }
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String username) {
        this.confirmPassword = confirmPassword;
    }
}
