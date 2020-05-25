package com.example.narva;

public class RegisterDatabase {

    String id;
    String username;
    String email;
    String password;

    public RegisterDatabase(){

    }

    public RegisterDatabase(String id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
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
}
