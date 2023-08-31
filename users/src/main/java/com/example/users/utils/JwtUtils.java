package com.example.users.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtUtils {

    private static final String SECRET_KEY = "yourSecretKey"; // 密钥，保持安全
    private static final long EXPIRATION_TIME = 3600000; // JWT 过期时间：1小时

    // 生成 JWT
    public static String generateToken(String username) {
        //指定算法
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        return JWT.create()
                .withClaim("username", username)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
    }

    // 验证 JWT
    public static boolean validateToken(String token) {
        try {
            //指定算法
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

            DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(token);

            // 在这里可以根据 decodedJWT 中的信息进行进一步的授权判断

            return true;
        } catch (Exception e) {
            return false;
        }
    }

//    public static void main(String[] args) {
//        String str = "0123456";
//        System.out.printf(str.substring(2,5));
//    }
}
