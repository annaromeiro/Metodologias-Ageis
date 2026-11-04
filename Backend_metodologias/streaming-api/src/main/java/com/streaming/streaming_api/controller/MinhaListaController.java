
package com.streaming.streaming_api.controller;

import com.streaming.streaming_api.service.MinhaListaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.streaming.streaming_api.service.AutorizacaoService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/minha-lista")
public class MinhaListaController {

    private final AutorizacaoService autorizacaoService;
    private final MinhaListaService minhaListaService;

    public MinhaListaController(
            MinhaListaService minhaListaService,
            AutorizacaoService autorizacaoService) {

        this.minhaListaService = minhaListaService;
        this.autorizacaoService = autorizacaoService;
    }
    @PostMapping("/{usuarioId}/{conteudoId}")
    public Map<String, Object> adicionarConteudo(
            @PathVariable int usuarioId,
            @PathVariable int conteudoId) {
        autorizacaoService.verificarUsuario(usuarioId);
        return minhaListaService.adicionarConteudo(usuarioId, conteudoId);
    }

    @GetMapping("/{usuarioId}")
    public List<Map<String, Object>> listarConteudos(
            @PathVariable int usuarioId) {
        autorizacaoService.verificarUsuario(usuarioId);
        return minhaListaService.listarConteudos(usuarioId);
    }
    @DeleteMapping("/{usuarioId}/{conteudoId}")
    public ResponseEntity<Void> removerConteudo(
            @PathVariable int usuarioId,
            @PathVariable int conteudoId) {
        autorizacaoService.verificarUsuario(usuarioId);
        boolean removido = minhaListaService.removerConteudo(
                usuarioId, conteudoId
        );

        if (!removido) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> tratarErro(IllegalArgumentException e) {
        return Map.of("erro", e.getMessage());
    }
}
