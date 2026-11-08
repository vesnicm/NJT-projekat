package com.fon.prijavaBackend.config;

import com.fon.prijavaBackend.security.JwtAuthFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/studijski-programi/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/srednje-skole/**").permitAll()
                
                .requestMatchers(HttpMethod.POST, "/api/studijski-programi/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/studijski-programi/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/studijski-programi/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/srednje-skole/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/srednje-skole/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/srednje-skole/**").hasRole("ADMIN")
                .requestMatchers("/api/korisnici/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/kandidati").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/prijave").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/prijave/pretraga").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/prijave/*/status").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/dokumenti/*/verifikuj").hasRole("ADMIN")
                
                .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                .authenticationEntryPoint(authenticationEntryPoint()) //401
                .accessDeniedHandler(accessDeniedHandler())) //403
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"Niste prijavljeni\",\"status\":\"UNAUTHORIZED\"}");
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"Nemate dozvolu za ovu akciju\",\"status\":\"FORBIDDEN\"}");
        };
    }
}
