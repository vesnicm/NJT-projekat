/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fon.prijavaBackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generisiToken(long korisnikId, String email, String uloga, Long kandidatId) {
        Date sada = new Date();
        Date istice = new Date(sada.getTime() + expirationMs);

        return Jwts.builder()
                .subject(email)
                .claim("korisnikId", korisnikId)
                .claim("uloga", uloga)
                .claim("kandidatId", kandidatId)
                .issuedAt(sada)
                .expiration(istice)
                .signWith(key())
                .compact();
    }

    public Claims procitajClaims(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validan(String token) {
        try {
            procitajClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
