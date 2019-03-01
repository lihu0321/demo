package com.example.demo.user.service;

import com.example.demo.user.entity.User;

import java.util.List;

public interface IUserService {

    User get(long id);

    List<User> list();
}
