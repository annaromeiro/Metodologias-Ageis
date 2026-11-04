
package com.streaming.streaming_api.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Service
public class UsuarioService {

    private final JdbcTemplate jdbcTemplate;
    private final JwtService jwtService;

    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    public UsuarioService(
            JdbcTemplate jdbcTemplate,
            JwtService jwtService) {

        this.jdbcTemplate = jdbcTemplate;
        this.jwtService = jwtService;
    }

    public Map<String, Object> cadastrarUsuario(
            String nome, String email, String senha) {

        if (nome == null || nome.isBlank()
                || email == null || email.isBlank()
                || senha == null || senha.isBlank()) {

            throw new IllegalArgumentException(
                    "Preencha todos os campos."
            );
        }

        Integer quantidade = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM usuarios WHERE LOWER(email) = LOWER(?)",
                Integer.class,
                email
        );

        if (quantidade != null && quantidade > 0) {
            throw new IllegalArgumentException(
                    "Este e-mail já está cadastrado."
            );
        }

        String senhaProtegida = encoder.encode(senha);

        jdbcTemplate.update(
                "INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, ?)",
                nome, email, senhaProtegida
        );

        return new HashMap<>(jdbcTemplate.queryForMap(
                "SELECT id, nome, email FROM usuarios WHERE LOWER(email) = LOWER(?)",
                email
        ));
    }

    public Map<String, Object> fazerLogin(
            String email, String senha) {

        if (email == null || email.isBlank()
                || senha == null || senha.isBlank()) {

            throw new IllegalArgumentException(
                    "Preencha todos os campos."
            );
        }

        var usuarios = jdbcTemplate.queryForList(
                "SELECT id, nome, email, senha FROM usuarios " +
                        "WHERE LOWER(email) = LOWER(?)",
                email
        );

        if (usuarios.isEmpty()) {
            throw new IllegalArgumentException(
                    "E-mail ou senha incorretos."
            );
        }

        Map<String, Object> usuario = usuarios.get(0);

        String senhaSalva = (String) usuario.get("SENHA");

        if (!encoder.matches(senha, senhaSalva)) {
            throw new IllegalArgumentException(
                    "E-mail ou senha incorretos."
            );
        }

        int usuarioId = ((Number) usuario.get("ID")).intValue();
        String emailUsuario = (String) usuario.get("EMAIL");

        String token = jwtService.gerarToken(
                usuarioId, emailUsuario
        );

        Map<String, Object> resposta = new HashMap<>();

        resposta.put("id", usuarioId);
        resposta.put("nome", usuario.get("NOME"));
        resposta.put("email", emailUsuario);
        resposta.put("token", token);
        resposta.put("mensagem", "Login realizado com sucesso!");

        return resposta;
    }
}
