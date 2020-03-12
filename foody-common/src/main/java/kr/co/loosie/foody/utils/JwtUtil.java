package kr.co.loosie.foody.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;

public class JwtUtil {

    private Key key;


    public JwtUtil(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());;
     }


    public String createToken(long userId, String name) {
        String token = Jwts.builder()
                .claim("userId",userId)
                .claim("name",name)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return token;
    }
}
