
package com.streaming.streaming_api.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Erros de validação
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> tratarValidacao(
            IllegalArgumentException e) {

        return ResponseEntity
                .badRequest()
                .body(Map.of("erro", e.getMessage()));
    }

    // Dados inválidos no JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> tratarJsonInvalido(
            HttpMessageNotReadableException e) {

        return ResponseEntity
                .badRequest()
                .body(Map.of("erro", "JSON inválido ou mal formatado."));
    }

    // Erros de integridade do banco
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> tratarBanco(
            DataIntegrityViolationException e) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "erro",
                        "Não foi possível realizar a operação. " +
                                "Verifique se o registro está duplicado ou " +
                                "possui dados relacionados."
                ));
    }

    @ExceptionHandler(
            org.springframework.security.access.AccessDeniedException.class
    )
    public ResponseEntity<Map<String, String>> tratarAcessoNegado(
            org.springframework.security.access.AccessDeniedException e) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Map.of("erro", "Acesso negado."));
    }

}
