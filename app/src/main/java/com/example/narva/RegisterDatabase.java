package com.example.narva;

public class RegisterDatabase {

    String username;
    String email;
    String password;
    Integer points;

    public RegisterDatabase(){

    }

    public RegisterDatabase( String username, String email, String password,Integer points) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.points = points;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
    public String getPassword(){
        return password;
    }

    public Integer getPoints() {
        return points;
    }
}
