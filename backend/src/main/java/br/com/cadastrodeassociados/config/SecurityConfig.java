package br.com.cadastrodeassociados.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher; // Importar

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())) // Necessário para H2 Console em iframes
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll() // Permitir H2 Console
                .requestMatchers("/api/login").permitAll()
                .requestMatchers("/api/users/**").hasRole("ADMIN")
                .requestMatchers("/api/associados/**").hasAnyRole("ADMIN", "SECRETARIO")
                .requestMatchers("/api/pagamentos/**").hasAnyRole("ADMIN", "SECRETARIO")
                .requestMatchers("/api/eventos/**").hasAnyRole("ADMIN", "SECRETARIO")
                .requestMatchers("/api/relatorios/**").hasAnyRole("ADMIN", "SECRETARIO")
                .requestMatchers("/api/admin/**").hasRole("ADMIN") // Adicionado para proteger endpoints de admin
                .requestMatchers("/api/documentos/**").hasAnyRole("ADMIN", "SECRETARIO") // Adicionado para proteger documentos
                .requestMatchers("/api/mensagens/**").hasRole("ADMIN") // Adicionado para proteger mensagens
                .requestMatchers("/api/dashboard/**").hasAnyRole("ADMIN", "SECRETARIO") // Adicionado para proteger dashboard
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginProcessingUrl("/api/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler((request, response, authentication) -> {
                    response.setStatus(200);
                    response.getWriter().write("{\"message\": \"Login successful\"}");
                    response.getWriter().flush();
                })
                .failureHandler((request, response, exception) -> {
                    response.setStatus(401);
                    response.getWriter().write("{\"message\": \"Login failed: " + exception.getMessage() + "\"}");
                    response.getWriter().flush();
                })
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/api/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(200);
                    response.getWriter().write("{\"message\": \"Logout successful\"}");
                    response.getWriter().flush();
                })
            );
        
        return http.build();
    }
}
