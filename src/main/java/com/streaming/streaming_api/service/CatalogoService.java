
package com.streaming.streaming_api.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CatalogoService {

    private final JdbcTemplate jdbcTemplate;

    public CatalogoService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Listar todos os conteúdos
    public List<Map<String, Object>> listarTodos() {
        return jdbcTemplate.queryForList(
                "SELECT * FROM conteudos ORDER BY id"
        );
    }

    // Buscar conteúdo pelo ID
    public Map<String, Object> buscarPorId(int id) {

        List<Map<String, Object>> resultado =
                jdbcTemplate.queryForList(
                        "SELECT * FROM conteudos WHERE id = ?",
                        id
                );

        if (resultado.isEmpty()) {
            return null;
        }

        return resultado.get(0);
    }

    // Cadastrar novo conteúdo com ficha técnica
    public Map<String, Object> cadastrarConteudo(
            int id,
            String titulo,
            String tipo,
            int ano,
            String genero,
            String sinopse,
            String elenco,
            String classificacao,
            String imagemUrl,
            String trailerUrl) {

        if (titulo == null || titulo.isBlank()
                || tipo == null || tipo.isBlank()
                || genero == null || genero.isBlank()
                || sinopse == null || sinopse.isBlank()) {

            throw new IllegalArgumentException(
                    "Preencha os campos obrigatórios."
            );
        }

        if (!tipo.equalsIgnoreCase("Filme")
                && !tipo.equalsIgnoreCase("Série")) {
            throw new IllegalArgumentException(
                    "O tipo deve ser Filme ou Série."
            );
        }

        if (ano <= 0) {
            throw new IllegalArgumentException(
                    "O ano deve ser válido."
            );
        }

        if (buscarPorId(id) != null) {
            throw new IllegalArgumentException(
                    "Já existe um conteúdo com esse ID."
            );
        }

        String tipoFormatado =
                tipo.equalsIgnoreCase("filme")
                        ? "Filme" : "Série";

        jdbcTemplate.update(
                "INSERT INTO conteudos " +
                        "(id, titulo, tipo, ano, genero, sinopse, " +
                        "elenco, classificacao, imagem_url, trailer_url) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                id,
                titulo,
                tipoFormatado,
                ano,
                genero,
                sinopse,
                elenco,
                classificacao,
                imagemUrl,
                trailerUrl
        );

        return buscarPorId(id);
    }

    // Excluir conteúdo pelo ID
    public boolean excluirPorId(int id) {

        int linhasExcluidas = jdbcTemplate.update(
                "DELETE FROM conteudos WHERE id = ?",
                id
        );

        return linhasExcluidas > 0;
    }

    // Buscar filmes e séries por gênero
    public List<Map<String, Object>> buscarPorGenero(
            String genero) {

        return jdbcTemplate.queryForList(
                "SELECT * FROM conteudos " +
                        "WHERE LOWER(genero) = LOWER(?) " +
                        "ORDER BY titulo",
                genero
        );
    }

    // Buscar conteúdo em destaque
    public Map<String, Object> buscarDestaque() {

        List<Map<String, Object>> resultado =
                jdbcTemplate.queryForList(
                        "SELECT * FROM conteudos " +
                                "WHERE destaque = TRUE " +
                                "ORDER BY id DESC LIMIT 1"
                );

        if (resultado.isEmpty()) {
            return null;
        }

        return resultado.get(0);
    }

}
