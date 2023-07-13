package com.example.spring.pojo.Entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ProjectEntity {

    public int id;

    public String name;

    public String email;

    public String status;

    public String dirName;

    public String result;

    public Timestamp createtime;
}
