
package com.streaming.streaming_api.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey chave;

    public JwtService(@Value("${jwt.secret}") String segredo) {
        byte[] chaveDecodificada =
                io.jsonwebtoken.io.Decoders.BASE64.decode(segredo);

        this.chave = Keys.hmacShaKeyFor(chaveDecodificada);
    }


    public String gerarToken(int usuarioId, String email) {

        long agora = System.currentTimeMillis();
        long validade = 24 * 60 * 60 * 1000L;

        return Jwts.builder()
                .subject(String.valueOf(usuarioId))
                .claim("email", email)
                .issuedAt(new Date(agora))
                .expiration(new Date(agora + validade))
                .signWith(chave)
                .compact();
    }

    public int obterUsuarioId(String token) {

        String id = Jwts.parser()
                .verifyWith(chave)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        return Integer.parseInt(id);
    }
}
