package br.com.fiap.mottomap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

//DTO para carregar os dados do formulário de alteração de senha.
@Data
public class PasswordChangeDTO {

    // Campo para a senha que o usuário vai digitar como sendo a sua atual
    @NotBlank(message = "A senha atual é obrigatória.")
    private String senhaAtual;

    // Campo para a nova senha desejada
    @NotBlank(message = "A nova senha é obrigatória.")
    @Size(min = 8, message = "A nova senha deve ter no mínimo 8 caracteres.")
    private String novaSenha;

    // Campo para confirmar a nova senha, evitando erros de digitação
    @NotBlank(message = "A confirmação da senha é obrigatória.")
    private String confirmacaoSenha;
}