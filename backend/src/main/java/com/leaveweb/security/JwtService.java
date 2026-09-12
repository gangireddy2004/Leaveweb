package com.leaveweb.security;

import java.util.Date;
import javax.crypto.SecretKey;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final SecretKey key;
    private final long expirationMs;
    public JwtService(@Value("${app.jwt.secret}") String secret, @Value("${app.jwt.expiration-ms}") long expirationMs) { this.key = Keys.hmacShaKeyFor(secret.getBytes(java.nio.charset.StandardCharsets.UTF_8)); this.expirationMs = expirationMs; }
    public String generateToken(UserDetails user) { return Jwts.builder().subject(user.getUsername()).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + expirationMs)).signWith(key).compact(); }
    public String extractUsername(String token) { return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject(); }
    public boolean isValid(String token, UserDetails user) { try { return extractUsername(token).equals(user.getUsername()) && !Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date()); } catch (RuntimeException ex) { return false; } }
}