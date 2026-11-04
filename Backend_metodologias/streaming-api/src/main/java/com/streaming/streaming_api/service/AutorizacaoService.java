
package com.streaming.streaming_api.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AutorizacaoService {

    public void verificarUsuario(int usuarioId) {

        Authentication autenticacao =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        if (autenticacao == null
                || !autenticacao.isAuthenticated()
                || !(autenticacao.getPrincipal() instanceof Integer)) {

            throw new AccessDeniedException(
                    "Usuário não autenticado."
            );
        }

        int usuarioAutenticado =
                (Integer) autenticacao.getPrincipal();

        if (usuarioAutenticado != usuarioId) {
            throw new AccessDeniedException(
                    "Você não tem permissão para acessar " +
                            "os dados de outro usuário."
            );
        }
    }
}
