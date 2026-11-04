
package com.streaming.streaming_api.controller;

import com.streaming.streaming_api.service.AvaliacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    public AvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    // Cadastrar avaliação
    @PostMapping("/{usuarioId}/{conteudoId}")
    public Map<String, Object> avaliarConteudo(
            @PathVariable int usuarioId,
            @PathVariable int conteudoId,
            @RequestBody Map<String, Integer> dados) {

        Integer nota = dados.get("nota");

        if (nota == null) {
            throw new IllegalArgumentException(
                    "Informe a nota da avaliação."
            );
        }

        return avaliacaoService.avaliarConteudo(
                usuarioId, conteudoId, nota
        );
    }

    // Listar avaliações de um conteúdo
    @GetMapping("/conteudo/{conteudoId}")
    public List<Map<String, Object>> listarAvaliacoes(
            @PathVariable int conteudoId) {

        return avaliacaoService.listarAvaliacoes(conteudoId);
    }

    // Consultar média das avaliações
    @GetMapping("/conteudo/{conteudoId}/media")
    public Map<String, Object> calcularMedia(
            @PathVariable int conteudoId) {

        return avaliacaoService.calcularMedia(conteudoId);
    }

    // Tratar erros de validação
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> tratarErro(
            IllegalArgumentException e) {

        return Map.of("erro", e.getMessage());
    }
}
