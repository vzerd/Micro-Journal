package com.vizz.microjournal.model;

import jakarta.persistence.*;
import java.util.Base64;
import java.util.UUID;

@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String password;
    private UUID token;

    public int getID() {
        return id;
    }
    public void setID(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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

    public UUID getToken() {
        return token;
    }
    public void setToken(UUID token) {
        this.token = token;
    }

    public void trimName(){
        this.name = this.name.trim();
    }
    public void trimEmail(){
        this.email = this.email.trim();
    }
    public void trimPassword(){
        this.password = this.password.trim();
    }
    public void encodePassword(){
        this.password = Base64.getEncoder().encodeToString(this.password.getBytes());
    }
}
