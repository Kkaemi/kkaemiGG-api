package com.spring.kkaemiGG.service;

import com.spring.kkaemiGG.auth.Token;
import com.spring.kkaemiGG.config.AppProperties;
import com.spring.kkaemiGG.domain.token.RefreshToken;
import com.spring.kkaemiGG.domain.token.RefreshTokenRepository;
import com.spring.kkaemiGG.domain.user.User;
import com.spring.kkaemiGG.exception.BadRequestException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;

@RequiredArgsConstructor
@Transactional
@Service
public class TokenService {

    private final AppProperties appProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    private SecretKey key;
    private long accessTokenExp;
    private long refreshTokenExp;

    @PostConstruct
    void init() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(appProperties.getJwt().getJwtSecretKey()));
        this.accessTokenExp = appProperties.getJwt().getAccessTokenExp();
        this.refreshTokenExp = appProperties.getJwt().getRefreshTokenExp();
    }

    public Token generateToken(User user) {
        Date now = new Date();

        return new Token(
                Jwts.builder()
                        .setHeaderParam("typ", "JWT")
                        .setHeaderParam("alg", "HS512")
                        .setSubject(user.getId().toString())
                        .setExpiration(new Date(now.getTime() + accessTokenExp))
                        .setIssuedAt(now)
                        .claim("nickname", user.getNickname())
                        .claim("email", user.getEmail())
                        .signWith(key, SignatureAlgorithm.HS512)
                        .compact(),
                Jwts.builder()
                        .setHeaderParam("typ", "JWT")
                        .setHeaderParam("alg", "HS512")
                        .setSubject(user.getId().toString())
                        .setExpiration(new Date(now.getTime() + refreshTokenExp))
                        .setIssuedAt(now)
                        .claim("nickname", user.getNickname())
                        .claim("email", user.getEmail())
                        .signWith(key, SignatureAlgorithm.HS512)
                        .compact()
        );
    }

    public boolean verifyToken(String token) {
        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return jws.getBody()
                    .getExpiration().after(new Date());
        } catch (JwtException ex) {
            return false;
        }
    }

    public Long getUserId(String token) {
        return Long.valueOf(
                Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token).getBody().getSubject()
        );
    }

    public Long saveOrUpdate(User user, String refreshToken) {
        return refreshTokenRepository.findByUser(user)
                .map(RefreshToken::getId)
                .orElseGet(() -> refreshTokenRepository.save(RefreshToken.builder(user, refreshToken).build()).getId());
    }

    public RefreshToken findRefreshTokenById(Long refreshTokenId) {
        return refreshTokenRepository.findById(refreshTokenId)
                .orElseThrow(() -> new BadRequestException("해당 아이디의 리프레시 토큰을 찾을 수 없습니다."));
    }

    public void deleteRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }

    public void deleteRefreshToken(Long refreshTokenId) {
        RefreshToken refreshToken = refreshTokenRepository.findById(refreshTokenId)
                .orElseThrow(() -> new BadRequestException("해당 아이디의 리프레시 토큰을 찾을 수 없습니다."));

        refreshTokenRepository.delete(refreshToken);
    }

    public Long updateRefreshToken(User user, String refreshToken) {
        RefreshToken entity = refreshTokenRepository.findByUser(user)
                .orElseThrow(() -> new BadRequestException("해당 외래키의 리프레시 토큰을 찾을 수 없습니다."));

        entity.updateToken(refreshToken);
        return refreshTokenRepository.save(entity).getId();
    }
}
