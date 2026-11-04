
package com.streaming.streaming_api.security;

import com.streaming.streaming_api.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtService jwtService) throws Exception {

        JwtAuthenticationFilter jwtFilter =
                new JwtAuthenticationFilter(jwtService);

        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/h2-console", "/h2-console/**").permitAll()



                        .requestMatchers(
                                "/api/usuarios/cadastro",
                                "/api/usuarios/login"
                        ).permitAll()


                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/conteudos",
                                        "/api/conteudos/*",
                                        "/api/conteudos/genero/*"
                                ).permitAll()

                        .anyRequest().authenticated()
                )


                .exceptionHandling(ex -> ex

                        .authenticationEntryPoint(
                                (request, response, exception) ->
                                        response.sendError(
                                                HttpStatus.UNAUTHORIZED.value(),
                                                "Token ausente ou inválido."
                                        )
                        )

                        .accessDeniedHandler(
                                (request, response, exception) ->
                                        response.sendError(
                                                HttpStatus.FORBIDDEN.value(),
                                                "Acesso negado."
                                        )
                        )
                )


                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
