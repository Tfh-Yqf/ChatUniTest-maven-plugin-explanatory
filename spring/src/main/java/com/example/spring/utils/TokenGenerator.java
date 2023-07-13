package com.example.spring.utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TokenGenerator {
    private static final String SECRET_KEY = "AsdaskhdlaksjfklasjfklajsffansknalkjsdklasjdlkajdasfjahkajfkljasklfjaljfhbajopeigjiaphbjvnkhdpIUIHFNKVOPijfPOIUFioAhfbeoUVJhS0efhjKFNoAAisjfFSJFOIpjfiPFJpvneinioiJHUFRIUOJIPKJJNBHDJJFYURIOSJKNCBHVFGYFUIOJAWSSJSDKLVJNDFBHUEDFIJOAEWSKCDJNVHBEWUIOLKJNVDBHIWI";
    private static final long EXPIRATION_TIME = 10 * 60 * 60 * 1000; // 10小时

    public static String generateToken(String username) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expirationTime = currentTime.plusHours(10);

        Date expirationDate = Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setSubject(username)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public static String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public static boolean isTokenValid(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isTokenExpired(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        Date expirationDate = claims.getExpiration();
        LocalDateTime expirationTime = expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime currentTime = LocalDateTime.now();

        return currentTime.isAfter(expirationTime);
    }
}
