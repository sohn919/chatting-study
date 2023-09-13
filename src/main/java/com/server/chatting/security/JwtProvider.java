package com.server.chatting.security;

import com.server.chatting.entity.member.Member;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    private final CustomUserDetailsService customUserDetailsService;

    public static final String TOKEN_PREFIX = "Bearer ";

    @Value("${jwt.key}")
    private String KEY;

    public String generateToken(Member member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", member.getId());
        claims.put("role", member.getRole());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(member.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getKeyFromBase64EncodedKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public void verifyToken(String jwt) {
        Jwts.parserBuilder()
                .setSigningKey(getKeyFromBase64EncodedKey())
                .build()
                .parseClaimsJws(jwt);
    }

    public Authentication authentication(String jwt) {
        CustomUserDetails myUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(getUsername(jwt));
        return new UsernamePasswordAuthenticationToken(myUserDetails, null, myUserDetails.getAuthorities());
    }

    private String getUsername(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(getKeyFromBase64EncodedKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }

    private Key getKeyFromBase64EncodedKey() {
        byte[] byteKey = Decoders.BASE64.decode(KEY);
        return Keys.hmacShaKeyFor(byteKey);
    }

}
