package com.spring.kkaemiGG.service;

import com.spring.kkaemiGG.auth.Token;
import com.spring.kkaemiGG.domain.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey key;

    public JwtService(@Value("${JWT-SECRET-KEY}") String base64String) {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64String));
    }

    public Token generateToken(User user) {
        long accessTokenPeriod = 1000L * 60L * 30L; //  30분
        long refreshTokenPeriod = 1000L * 60L * 60L * 24L * 3L; // 3일

        Date now = new Date();

        return new Token(
                Jwts.builder()
                        .setHeaderParam("typ", "JWT")
                        .setHeaderParam("alg", "HS512")
                        .setSubject(user.getId().toString())
                        .setExpiration(new Date(now.getTime() + accessTokenPeriod))
                        .setIssuedAt(now)
                        .claim("nickname", user.getNickname())
                        .claim("email", user.getEmail())
                        .signWith(key, SignatureAlgorithm.HS512)
                        .compact(),
                Jwts.builder()
                        .setHeaderParam("typ", "JWT")
                        .setHeaderParam("alg", "HS512")
                        .setSubject(user.getId().toString())
                        .setExpiration(new Date(now.getTime() + refreshTokenPeriod))
                        .setIssuedAt(now)
                        .claim("nickname", user.getNickname())
                        .claim("email", user.getEmail())
                        .signWith(key, SignatureAlgorithm.HS512)
                        .compact()
        );
    }

    public boolean verifyToken(String token) {
        Jws<Claims> jws;

        try {
            jws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return jws.getBody()
                    .getExpiration().after(new Date());
        } catch (JwtException ex) {
            return false;
        }
    }
}
