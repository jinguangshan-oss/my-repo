package com.example.users.controller;

import com.alibaba.fastjson.JSON;
import com.example.users.pojo.Login;
import com.example.users.pojo.Response;
import com.example.users.service.UserService;
import com.example.users.type.User;
import com.example.users.utils.BcryptUtils;
import com.example.users.utils.JwtUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Resource
    UserService userService;

    @RequestMapping("/login")
    public Response login(@RequestBody Login login){
        System.out.printf("请求参数：" + JSON.toJSONString(login));
        //校验用户名密码
        User param = new User();
        param.setUserName(login.getUserName());
        List<User> list = userService.select(param);
        if(list != null && list.size() > 0){
            User user = list.get(0);
            String password = user.getPassword();
            //验证密码
            boolean boo = BcryptUtils.verifyPassword(login.getPassword() , password);
            if(boo){
                //生成jwt
                String jwt = JwtUtils.generateToken(login.getUserName());
                return Response.ok(jwt);
            }else {
                return Response.fail(null, "用户名或密码错误！");
            }
        }else{
            return Response.fail(null, "用户不存在！");
        }


    }

    @RequestMapping("/register")
    public Response  register(@RequestBody User user){
        List<User> list = userService.select(user);
        if(list == null || list.size() > 0){
            return Response.fail(null, "注册失败，用户已存在");
        }

        //密码加密
        user.setPassword(BcryptUtils.encryptPassword(user.getPassword()));

        userService.insert(user);
        return Response.ok("注册成功");
    }
}
