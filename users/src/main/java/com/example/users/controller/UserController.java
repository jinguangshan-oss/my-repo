package com.example.users.controller;

import com.example.users.dao.UserMapper;
import com.example.users.pojo.Response;
import com.example.users.service.UserService;
import com.example.users.type.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    UserService userService;

    @RequestMapping("/select")
    public Response<List<User>>  select(@RequestBody User user){
        List<User> list = userService.select(user);
        return Response.ok(list);
    }

    @RequestMapping("/insert")
    public Response  insert(@RequestBody User user){
        userService.insert(user);
        return Response.ok("注册成功");
    }

}
