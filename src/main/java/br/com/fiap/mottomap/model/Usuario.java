package br.com.fiap.mottomap.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// Representa a entidade Usuaria
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    // Chave primária do usuário.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome completo do colaborador.
    @NotBlank(message = "O nome do usuário é obrigatório")
    @Size(min = 3, max = 150, message = "O nome deve ter entre 3 e 150 caracteres")
    private String nome;

    // Email do usuário, que será usado como login. Deve ser único no sistema.
    @Column(unique = true)
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "O e-mail deve ser válido")
    private String username;

    // Senha do usuário, que será armazenada de forma criptografada (hash) no banco.
    @NotBlank(message = "A senha é obrigatória")
    // A validação de tamanho é mais importante no DTO para permitir edição sem alterar a senha.
    private String password;

    // Papel do usuário no sistema, definido pelo enum CargoUsuario.
    @NotNull(message = "O cargo é obrigatório")
    @Enumerated(EnumType.STRING)
    private CargoUsuario cargoUsuario;

    // Controle de soft-delete. Se 'false', o usuário não pode logar.
    private boolean ativo = true;

    // Relacionamento Muitos-para-Um: Muitos usuários podem pertencer a uma filial.
    @NotNull(message = "A filial é obrigatória")
    @ManyToOne
    private Filial filial;


    // --- Métodos da Interface UserDetails implementados abaixo ---
    // Estes métodos fazem a ponte entre a nossa entidade e o Spring Security.

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retorna a "permissão" do usuário (o cargo) para o Spring Security.
        return List.of(new SimpleGrantedAuthority(cargoUsuario.name()));
    }

    @Override
    public String getPassword() {
        // Retorna a senha. O Spring Security usa isso para comparar com a senha enviada no login.
        return this.password;
    }

    @Override
    public String getUsername() {
        // Retorna o nome de usuário (que no nosso caso é o email).
        return this.username;
    }

    @Override
    public boolean isEnabled(){
        // Informa ao Spring Security se a conta do usuário está ativa ou não.
        return this.ativo;
    }
}