package com.example.demo.user.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
@Data
@Component
@ConfigurationProperties(prefix = "person")
public class Person {

    public String lastName;

    private Integer age;

    private Boolean boss;

    private Date birth;

    private Map<String,Object> maps;

    private List<Object> lists;

}
