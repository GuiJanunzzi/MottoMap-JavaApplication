package br.com.fiap.mottomap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity // Habilita a segurança por metodo (ex: @PreAuthorize)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Libera o acesso a recursos estáticos e a página de login
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/login").permitAll()

                        // Permite que qualquer usuario logado acesse o patio
                        .requestMatchers("/filiais/{id}/patio").authenticated()
                        .requestMatchers("/posicoes/meu-patio").authenticated()

                        // Apenas ADM_GERAL pode gerenciar usuários e filiais
                        .requestMatchers("/usuarios/**", "/filiais/**").hasAuthority("ADM_GERAL")

                        // Admins e Colaboradores de Pátio podem gerenciar as posições
                        .requestMatchers("/posicoes/**").hasAnyAuthority("ADM_GERAL", "ADM_LOCAL", "COL_PATIO")

                        // Ações e visões específicas do Mecânico
                        .requestMatchers("/motos/pendentes", "/problemas/resolver/**").hasAuthority("COL_MECANICO")

                        // Qualquer outra página precisa de login
                        .anyRequest().authenticated()
                )
                // Configura o formulário de login
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                // Configura a funcionalidade de logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    // Bean para criptografar as senhas com BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}