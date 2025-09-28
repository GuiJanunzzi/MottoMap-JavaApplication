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

// Representa a entidade Filial no banco de dados.
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Filial {

    // Chave primária da tabela, gerada automaticamente.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome da filial, ex: "Mottu São Paulo".
    @NotBlank(message = "O nome da filial é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String nome;

    // Endereço completo da filial.
    @NotBlank(message = "O endereço é obrigatório")
    @Size(max = 200, message = "O endereço deve ter até 200 caracteres")
    private String endereco;

    // Cidade onde a filial está localizada.
    @NotBlank(message = "A cidade é obrigatória")
    @Size(max = 50, message = "A cidade deve ter até 50 caracteres")
    private String cidade;

    // Sigla do estado (UF), ex: "SP".
    @NotBlank(message = "A sigla do estado é obrigatória")
    @Pattern(regexp = "[A-Z]{2}", message = "A sigla do estado deve ter 2 letras maiúsculas (ex: SP, RJ)")
    private String siglaEstado;

    // Dimensões do pátio para a montagem do mapa visual.
    // Número de colunas do pátio.
    @NotNull(message = "O número de colunas é obrigatório")
    @Min(value = 1, message = "O pátio deve ter pelo menos 1 coluna")
    @Max(value = 200, message = "O número máximo de colunas é 200")
    private Integer numeroColuna;

    // Número de linhas do pátio.
    @NotNull(message = "O número de linhas é obrigatório")
    @Min(value = 1, message = "O pátio deve ter pelo menos 1 linha")
    @Max(value = 200, message = "O número máximo de linhas é 200")
    private Integer numeroLinha;

    // Capacidade total de motos que o pátio pode abrigar.
    @NotNull(message = "A capacidade máxima é obrigatória")
    @Positive(message = "A capacidade deve ser maior que zero")
    private Integer capacidadeMaxima;
}