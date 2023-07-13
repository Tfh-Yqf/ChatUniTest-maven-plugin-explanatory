package com.example.spring.pojo.Entity;

import java.security.Timestamp;

public class VerificationCode {
    private int id;
    private String email;
    private String code;
    private Timestamp createdAt;

    public VerificationCode() {
    }

    public VerificationCode(String email, String code) {
        this.email = email;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
