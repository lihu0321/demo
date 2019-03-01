package com.example.demo.user.rest;

import com.example.demo.rbac.anno.RequireAuth;
import com.example.demo.user.entity.Person;
import com.example.demo.user.entity.User;
import com.example.demo.user.service.IUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Validated
@RestController
@RequestMapping("/user")
public class    UserRest {

    @Value("${server.port}")
    private String port;

    @Autowired
    private Person person;

    @Autowired
    IUserService userService;
    @GetMapping("/person")
    public Person get(){
        return person;
    }

    @GetMapping("/login")
    public void get(HttpServletRequest request) {
        request.getSession().setAttribute("loginUser",userService.get(1));
    }

    @RequireAuth()
    @GetMapping("/get/{id}")
    public User get(@PathVariable long id) {
        return userService.get(id);
    }

    @GetMapping("/list/{page}/{size}")
    public PageInfo<User> list(@PathVariable int page,@PathVariable int size) {
        PageHelper.startPage(page, size);
        System.out.println(port);
        PageInfo<User> pages = new PageInfo<>(userService.list());
        return pages;
    }


    @PostMapping("/insert")
    public void insert(@Valid UserPost userPost) {
        System.out.println(userPost);
    }


    @PostMapping("/add/email")
    public void addEmaill(@Valid @Email String email) {
        System.out.println(email);
    }
}
@Data
class UserPost{
    @NotBlank(message = "用户名不能为空")
    private String name;
    @Max(value = 100,message = "年龄太大")
    private Integer age;
    @Length(min = 2, max = 4, message = "密码长度超出限制")
    private String password;
}
@Data
class EmailPost{
    @Email
    private String email;
}