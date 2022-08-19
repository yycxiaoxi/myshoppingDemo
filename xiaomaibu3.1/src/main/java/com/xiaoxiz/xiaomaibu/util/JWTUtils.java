package com.xiaoxiz.xiaomaibu.util;

import cn.hutool.crypto.asymmetric.RSA;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Calendar;
import java.util.Map;

public class JWTUtils {
    private static final String RSA_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCS517oIpoILu2XLbVuMsy+eTbELW6bQafnfnmarlaCRcW/qJEkfPogBPHaMWbeGau2QMH22FDf9gOIaoZG+pRK4Al3ZQeV1OERDnA3AvJGq5jy8C3atHAV5uSTfPRYKUN9yr1xyQ1hyydQG6eb4u+uYRInrKV6dMlzAJUh16lNX0JhkCS7kzEymPrr5/dGzV2vN6b5gm7qjl2NOQ1QwqtP4YnXBOAn9IfDk4gc+Tuni0dpqz0RXEmH4ZwUbK949Dx/aCUppjWhMVuSZ8baVA4ryxf/OFcBHm2M0G4nIVA1hOTc57is5vfTp1wKTKGVP6f+qGphirlkhRFQeuj6AWeDAgMBAAECggEAL3phvQq51507IpoeQiBe9cx9O8gnMDx3BKVmrexcfzwHnlrflfhiaojOlaNTyMJpMb0aMypUcR20pJkxDq2b0ds4lALuAanMl9OfxtfH77kz9IhVeZLJivqVs2k2G2wyqOwmcCj3jzy2VvipIgOlTSmIUZRF4x7toKJ2kHp/+EYr/zB2NdLpsYUiBPAgEXOmhVhebTsxki/flIoeAFbuY7RolPknzagQg6l94lFwlMVAtf478ExnmpEiM2ej8XkqPF3UC/TvzAngvqJHuZJoAUvPjCEZ/IuIbZChRIvmO6SsKTKjILDhd5mcbMqCODUPOuPG0MsIgzfJL8tvsWnzEQKBgQDbj+1A3ixoE/cQDXnl0Pt1RvbymgQ1F5qs9Jd+AQdRkFsROkBEdguYQSCfYMEFKGoQwfL1SGQfsSiNB31pFpCMUnc1hbH+GEHNOEkewRdN4nVY5KHDp+8CZ6WLG1jlpBZ+OI1IXELIg/z2Lvn/0UWgMAUM/8QVAZ+ksB8BxW1HqwKBgQCrSI9akrTnvboC0rRZmK4lEYXUM3q2DfbGT3CqB1wgOMPzYWQjVGlvYs6Njvbsg4f6Pq2uedYRs+fJiSKsfPETyNFD1hN5n4RUOQoLjv4Z0DVPClYbGAu9dry/Fk7a2vpBAb8gM/j348IWNAA0A2Pghv2Oa9h9asLBg3ovftEniQKBgQCP+01JiaizQ8xz0XKLm8UavkO5CfkSW/1JyEgEEGadPT4LCRkWDBdEbAkZmxhY6ozLmnRaF3PZOJLwEOZT4ME/0+Bqz9bOW1fjXTLOIS/IGmZ7ucq/BMR+3js4AuIEDdvWP/2eYypEVhGv21SERtcS2gql4J/UarEnDXv37GDiawKBgBZgimn41K2Z+d04jdtOuSGaonNQrm1ucZkC8+MixwRmpiL7zEtZUdT9gaK41MWdgXxOB/7NaAw0Bsorol4KjKxbBgvowe6Xyn/+W7Oa4NOagfxri0hK4e3Ev2/viCnLhfpMMh4Q7Cvmk4q3DHe/IaNJv4KF0GjCM+N4O9RMqyKRAoGASbtK8ME+HhI1uAIcjFGOLj2gbxisarsDnzCOSWlv15iXp0Zfc44SIFr92fUs4B8+dnVM3drExZk9jK89nrxOzPc4AjS+vVZ2uzm2CG43oyjfk4ol2RfoKjweEWQJujqF+iaXmCkxV7b+9EMAF0v1/G0Fd/CA9LmwIwrso9pX5PE=";
    private static final String RSA_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkude6CKaCC7tly21bjLMvnk2xC1um0Gn5355mq5WgkXFv6iRJHz6IATx2jFm3hmrtkDB9thQ3/YDiGqGRvqUSuAJd2UHldThEQ5wNwLyRquY8vAt2rRwFebkk3z0WClDfcq9cckNYcsnUBunm+LvrmESJ6ylenTJcwCVIdepTV9CYZAku5MxMpj66+f3Rs1drzem+YJu6o5djTkNUMKrT+GJ1wTgJ/SHw5OIHPk7p4tHaas9EVxJh+GcFGyvePQ8f2glKaY1oTFbkmfG2lQOK8sX/zhXAR5tjNBuJyFQNYTk3Oe4rOb306dcCkyhlT+n/qhqYYq5ZIURUHro+gFngwIDAQAB";

    /**
     * 生成token
     * @param payload token携带的信息
     * @return token字符串
     */
    public static String getTokenRsa(Map<String,String> payload){
        // 指定token过期时间为7天
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);

        JWTCreator.Builder builder = JWT.create();
        // 构建payload
        payload.forEach((k,v) -> builder.withClaim(k, v));

        // 利用hutool创建RSA
        RSA rsa = new RSA(RSA_PRIVATE_KEY, null);
        // 获取私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) rsa.getPrivateKey();
        // 签名时传入私钥
        String token = builder.withExpiresAt(calendar.getTime()).sign(Algorithm.RSA256(null, privateKey));
        return token;
    }

    /**
     * 解析token
     * @param token token字符串
     * @return 解析后的token
     */
    public static DecodedJWT decodeRsa(String token){
        // 利用hutool创建RSA
        RSA rsa = new RSA(null, RSA_PUBLIC_KEY);
        // 获取RSA公钥
        RSAPublicKey publicKey = (RSAPublicKey) rsa.getPublicKey();
        // 验签时传入公钥
        JWTVerifier jwtVerifier = JWT.require(Algorithm.RSA256(publicKey, null)).build();
        DecodedJWT decodedJWT;
        try{
            decodedJWT = jwtVerifier.verify(token);
        }catch (Exception e){
            decodedJWT=null;
        }

        return decodedJWT;
    }

}
