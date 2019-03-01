package com.example.demo.user.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private long id;
    private String name;
    private String sex;
    private long mobile;
    private String job;
    private String state;
    private String username;
    private String password;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;


    public int getAge(){
        return (int)id;
    }



    public boolean locked() {
        return "lock".equals(getState());
    }

}