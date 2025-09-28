package br.com.fiap.mottomap.dto;

import br.com.fiap.mottomap.model.CargoUsuario;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String username;

    @Pattern(regexp = "^$|.{8,}", message = "A senha deve estar em branco ou ter no mínimo 8 caracteres")
    private String password;

    @NotNull(message = "O cargo é obrigatório")
    private CargoUsuario cargoUsuario;

    @NotNull(message = "A filial é obrigatória")
    private Long filialId;

    private boolean ativo = true;
}
