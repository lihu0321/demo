package com.example.demo.user.service;

import com.example.demo.user.dao.UserDao;
import com.example.demo.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    UserDao userDao;
    @Override
    public User get(long id) {
        return userDao.get(id);
    }

    @Override
    public List<User> list() {
        return userDao.list();
    }
}
