package com.example.spring.pojo.Entity;

import lombok.Data;

@Data
public class UserEntity {

    public int id;
    public String email;

    public String password;

    public String code;

    public String token;

    public void UserEntity(String password,String code,String email){
        this.password = password;
        this.code = code;
        this.email = email;
    }
}
