
package com.streaming.streaming_api.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MinhaListaService {

    private final JdbcTemplate jdbcTemplate;

    public MinhaListaService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Adicionar conteúdo à lista
    public Map<String, Object> adicionarConteudo(
            int usuarioId, int conteudoId) {

        Integer usuarioExiste = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM usuarios WHERE id = ?",
                Integer.class,
                usuarioId
        );

        if (usuarioExiste == null || usuarioExiste == 0) {
            throw new IllegalArgumentException(
                    "Usuário não encontrado."
            );
        }

        Integer conteudoExiste = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM conteudos WHERE id = ?",
                Integer.class,
                conteudoId
        );

        if (conteudoExiste == null || conteudoExiste == 0) {
            throw new IllegalArgumentException(
                    "Conteúdo não encontrado."
            );
        }

        Integer jaAdicionado = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM minha_lista " +
                        "WHERE usuario_id = ? AND conteudo_id = ?",
                Integer.class,
                usuarioId, conteudoId
        );

        if (jaAdicionado != null && jaAdicionado > 0) {
            throw new IllegalArgumentException(
                    "Este conteúdo já está na sua lista."
            );
        }

        jdbcTemplate.update(
                "INSERT INTO minha_lista (usuario_id, conteudo_id) " +
                        "VALUES (?, ?)",
                usuarioId, conteudoId
        );

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("usuarioId", usuarioId);
        resposta.put("conteudoId", conteudoId);
        resposta.put("mensagem", "Conteúdo adicionado à sua lista!");

        return resposta;
    }

    // Listar conteúdos com todos os dados
    public List<Map<String, Object>> listarConteudos(int usuarioId) {

        return jdbcTemplate.queryForList(
                "SELECT c.id AS \"id\", " +
                        "c.titulo AS \"titulo\", " +
                        "c.tipo AS \"tipo\", " +
                        "c.ano AS \"ano\", " +
                        "c.genero AS \"genero\" " +
                        "FROM conteudos c " +
                        "INNER JOIN minha_lista m " +
                        "ON c.id = m.conteudo_id " +
                        "WHERE m.usuario_id = ? " +
                        "ORDER BY c.id",
                usuarioId
        );
    }

    // Remover conteúdo da lista
    public boolean removerConteudo(
            int usuarioId, int conteudoId) {

        int linhasExcluidas = jdbcTemplate.update(
                "DELETE FROM minha_lista " +
                        "WHERE usuario_id = ? AND conteudo_id = ?",
                usuarioId, conteudoId
        );

        return linhasExcluidas > 0;
    }
}
