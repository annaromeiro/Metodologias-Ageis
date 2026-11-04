
package com.streaming.streaming_api.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AvaliacaoService {

    private final JdbcTemplate jdbcTemplate;

    public AvaliacaoService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Cadastrar uma avaliação
    public Map<String, Object> avaliarConteudo(
            int usuarioId, int conteudoId, int nota) {

        if (nota < 0 || nota > 10) {
            throw new IllegalArgumentException(
                    "A nota deve estar entre 0 e 10."
            );
        }

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

        Integer jaAvaliou = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM avaliacoes " +
                        "WHERE usuario_id = ? AND conteudo_id = ?",
                Integer.class,
                usuarioId, conteudoId
        );

        if (jaAvaliou != null && jaAvaliou > 0) {
            throw new IllegalArgumentException(
                    "Você já avaliou este conteúdo."
            );
        }

        jdbcTemplate.update(
                "INSERT INTO avaliacoes " +
                        "(usuario_id, conteudo_id, nota) VALUES (?, ?, ?)",
                usuarioId, conteudoId, nota
        );

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("usuarioId", usuarioId);
        resposta.put("conteudoId", conteudoId);
        resposta.put("nota", nota);
        resposta.put("mensagem", "Avaliação registrada com sucesso!");

        return resposta;
    }

    // Listar avaliações de um conteúdo
    public List<Map<String, Object>> listarAvaliacoes(int conteudoId) {

        return jdbcTemplate.queryForList(
                "SELECT a.id AS \"id\", " +
                        "u.nome AS \"usuario\", " +
                        "a.nota AS \"nota\", " +
                        "a.data_avaliacao AS \"dataAvaliacao\" " +
                        "FROM avaliacoes a " +
                        "INNER JOIN usuarios u ON a.usuario_id = u.id " +
                        "WHERE a.conteudo_id = ? " +
                        "ORDER BY a.data_avaliacao DESC",
                conteudoId
        );
    }

    // Calcular a média das avaliações
    public Map<String, Object> calcularMedia(int conteudoId) {

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

        Map<String, Object> dados = jdbcTemplate.queryForMap(
                "SELECT COUNT(*) AS total, AVG(nota) AS media " +
                        "FROM avaliacoes WHERE conteudo_id = ?",
                conteudoId
        );

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("conteudoId", conteudoId);
        resposta.put("totalAvaliacoes", dados.get("TOTAL"));
        resposta.put("media", dados.get("MEDIA"));

        return resposta;
    }
}
