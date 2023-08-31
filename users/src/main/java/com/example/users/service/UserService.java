package com.example.users.service;

import com.example.users.dao.UserMapper;
import com.example.users.type.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;


    public List<User> select(User user){
        List<User> list = userMapper.select(user);
        return list;
    }

    public void insert(User user){
        userMapper.insert(user);
    }

}
