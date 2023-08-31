package com.example.users.dao;

import com.example.users.type.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    List<User> select(@Param("param") User user);

    void insert(@Param("param") User user);
}
