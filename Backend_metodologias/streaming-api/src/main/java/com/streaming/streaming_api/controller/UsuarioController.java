
package com.streaming.streaming_api.controller;

import com.streaming.streaming_api.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastro")
    public Map<String, Object> cadastrarUsuario(
            @RequestBody Map<String, String> dados) {

        return usuarioService.cadastrarUsuario(
                dados.get("nome"),
                dados.get("email"),
                dados.get("senha")
        );
    }

    @PostMapping("/login")
    public Map<String, Object> fazerLogin(
            @RequestBody Map<String, String> dados) {

        return usuarioService.fazerLogin(
                dados.get("email"),
                dados.get("senha")
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> tratarErro(
            IllegalArgumentException e) {

        return Map.of("erro", e.getMessage());
    }
}
