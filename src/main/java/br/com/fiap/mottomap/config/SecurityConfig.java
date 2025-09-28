package br.com.fiap.mottomap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Libera o acesso a recursos estáticos (CSS, JS, imagens)
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        // Libera o acesso à página de login
                        .requestMatchers("/login").permitAll()
                        // Define regras de autorização para URLs específicas
                        .requestMatchers("/posicoes/meu-patio").hasAnyAuthority("COL_PATIO", "COL_MECANICO", "ADM_LOCAL")
                        .requestMatchers("/filiais/{id}/patio").authenticated()
                        .requestMatchers("/filiais/**", "/usuarios/**").hasAuthority("ADM_GERAL")
                        .requestMatchers("/motos/delete/**").hasAuthority("ADM_GERAL")
                        .requestMatchers("/motos/new", "/motos/edit/**").hasAnyAuthority("ADM_GERAL", "ADM_LOCAL")
                        .requestMatchers("/posicoes/**").hasAnyAuthority("ADM_GERAL", "ADM_LOCAL")
                        .requestMatchers("/filiais/{id}/patio", "/posicoes/**").authenticated()
                        // Exige autenticação para qualquer outra requisição
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        // Define a URL da página de login customizada
                        .loginPage("/login")
                        // Define a URL para onde o usuário é redirecionado após o login bem-sucedido
                        .defaultSuccessUrl("/", true)
                        // Permite que todos acessem a página de login
                        .permitAll()
                )
                .logout(logout -> logout
                        // Define a URL para acionar o logout
                        .logoutUrl("/logout")
                        // Define a URL para onde o usuário é redirecionado após o logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Define o algoritmo de criptografia de senhas. Essencial para a segurança.
        return new BCryptPasswordEncoder();
    }
}
