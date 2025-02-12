package com.cart_page.cart_page.entities;

import jakarta.validation.constraints.NotBlank;

public class User {
    private int id;

    @NotBlank(message = "Le mail ne peut pas être vide.")
    private String email;

    @NotBlank(message = "Le mot de passe ne peut pas être vide.")
    private String password;

    @NotBlank(message = "Le rôle ne peut pas être vide.")
    private String role;

    public User(){

    }

    public User(int id, String email, String password, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
