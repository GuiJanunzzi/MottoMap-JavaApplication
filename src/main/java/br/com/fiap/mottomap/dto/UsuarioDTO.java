package br.com.fiap.mottomap.dto;

import br.com.fiap.mottomap.model.CargoUsuario;
import jakarta.validation.constraints.*;
import lombok.Data;


//DTO para o formulário de criação e edição de usuários.
//Separa os dados da view da entidade do banco, permitindo validações diferentes
@Data
public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    // Email do usuário, que também é usado para login
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String username;

    // Senha do usuário. A validação permite que o campo seja vazio (para edição)
    // ou, se preenchido, tenha no mínimo 8 caracteres.
    @Pattern(regexp = "^$|.{8,}", message = "A senha deve estar em branco ou ter no mínimo 8 caracteres")
    private String password;

    @NotNull(message = "O cargo é obrigatório")
    private CargoUsuario cargoUsuario;

    @NotNull(message = "A filial é obrigatória")
    private Long filialId;

    private boolean ativo = true;
}