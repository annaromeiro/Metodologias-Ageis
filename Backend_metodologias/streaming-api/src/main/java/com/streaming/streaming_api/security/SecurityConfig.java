
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.Customizer;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtService jwtService) throws Exception {

        JwtAuthenticationFilter jwtFilter =
                new JwtAuthenticationFilter(jwtService);

        http
                .cors(Customizer.withDefaults())
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


                        .requestMatchers("/api/conteudos/**").permitAll()

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
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:5173")
        );

        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );

        configuration.setAllowedHeaders(
                List.of("Authorization", "Content-Type")
        );

        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
