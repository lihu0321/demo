package com.example.demo.user.dao;

import com.example.demo.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao {

    User get(@Param("id") long id);

    List<User> list();
}
