package com.example.users.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;

    private String userName;

    private String password;

    private String mail;

    private String phone;

}
