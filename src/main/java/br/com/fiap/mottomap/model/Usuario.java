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

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do usuário é obrigatório")
    @Size(min = 3, max = 150, message = "O nome deve ter entre 3 e 150 caracteres")
    private String nome;

    @Column(unique = true) // O username (email) deve ser único
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "O e-mail deve ser válido")
    private String username;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 8, max = 100, message = "A senha deve ter entre 8 e 100 caracteres")
    private String password;

    @NotNull(message = "O cargo é obrigatório")
    @Enumerated(EnumType.STRING)
    private CargoUsuario cargoUsuario;

    private boolean ativo = true;

    @NotNull(message = "campo obrigatório")
    @ManyToOne
    private Filial filial;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // O "papel" ou "permissão" do usuário.
        return List.of(new SimpleGrantedAuthority(cargoUsuario.name()));
    }

    // O Spring Security usa este metodo internamente, já está correto.
    @Override
    public String getPassword() {
        return this.password;
    }

    // O Spring Security usa este metodo para identificar o usuário.
    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isEnabled(){
        return this.ativo;
    }
}
