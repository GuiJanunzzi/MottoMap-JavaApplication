package br.com.fiap.mottomap.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Filial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da filial é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "O endereço é obrigatório")
    @Size(max = 200, message = "O endereço deve ter até 200 caracteres")
    private String endereco;

    @NotBlank(message = "A cidade é obrigatória")
    @Size(max = 50, message = "A cidade deve ter até 50 caracteres")
    private String cidade;

    @NotBlank(message = "A sigla do estado é obrigatória")
    @Pattern(regexp = "[A-Z]{2}", message = "A sigla do estado deve ter 2 letras maiúsculas (ex: SP, RJ)")
    private String siglaEstado;

    @NotNull(message = "O número de colunas é obrigatório")
    @Min(value = 1, message = "O pátio deve ter pelo menos 1 coluna")
    @Max(value = 200, message = "O número máximo de colunas é 200")
    private Integer numeroColuna;

    @NotNull(message = "O número de linhas é obrigatório")
    @Min(value = 1, message = "O pátio deve ter pelo menos 1 linha")
    @Max(value = 200, message = "O número máximo de linhas é 200")
    private Integer numeroLinha;

    @NotNull(message = "A capacidade máxima é obrigatória")
    @Positive(message = "A capacidade deve ser maior que zero")
    private Integer capacidadeMaxima;
}
