package com.example.users.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptUtils {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 加密密码
    public static String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    // 验证密码是否正确
    public static boolean verifyPassword(String userInputPassword, String hashedPassword) {
        return passwordEncoder.matches(userInputPassword, hashedPassword);
    }
}

