
package com.streaming.streaming_api.controller;

import com.streaming.streaming_api.service.CatalogoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/conteudos")
public class ConteudoController {

    private final CatalogoService catalogoService;

    public ConteudoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    // Listar todos os filmes e séries
    @GetMapping
    public List<Map<String, Object>> listarConteudos() {
        return catalogoService.listarTodos();
    }

    // Buscar conteúdo pelo ID
    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<Map<String, Object>> buscarPorId(
            @PathVariable int id) {

        Map<String, Object> conteudo =
                catalogoService.buscarPorId(id);

        if (conteudo == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(conteudo);
    }

    // Cadastrar filme ou série com ficha técnica
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> cadastrarConteudo(
            @RequestBody Map<String, Object> dados) {

        if (!(dados.get("id") instanceof Number)
                || !(dados.get("ano") instanceof Number)) {
            throw new IllegalArgumentException(
                    "Informe um ID e um ano válidos."
            );
        }

        return catalogoService.cadastrarConteudo(
                ((Number) dados.get("id")).intValue(),
                (String) dados.get("titulo"),
                (String) dados.get("tipo"),
                ((Number) dados.get("ano")).intValue(),
                (String) dados.get("genero"),
                (String) dados.get("sinopse"),
                (String) dados.get("elenco"),
                (String) dados.get("classificacao"),
                (String) dados.get("imagemUrl"),
                (String) dados.get("trailerUrl")
        );
    }

    // Excluir conteúdo pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirConteudo(
            @PathVariable int id) {

        boolean excluido = catalogoService.excluirPorId(id);

        if (!excluido) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    // Listar conteúdos de uma categoria
    @GetMapping("/genero/{genero}")
    public List<Map<String, Object>> buscarPorGenero(
            @PathVariable String genero) {

        return catalogoService.buscarPorGenero(genero);
    }

    // Retornar o filme ou série em destaque
    @GetMapping("/destaque")
    public ResponseEntity<Map<String, Object>> buscarDestaque() {

        Map<String, Object> destaque =
                catalogoService.buscarDestaque();

        if (destaque == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(destaque);
    }

}
