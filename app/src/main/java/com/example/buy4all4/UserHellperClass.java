package com.example.buy4all4;

public class UserHellperClass {
    String username, email, password, password2;
    public UserHellperClass() {

    }

    public UserHellperClass(String username, String email, String password, String password2) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.password2 = password2;

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
    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String username) {
        this.password2 = password2;
    }
}
