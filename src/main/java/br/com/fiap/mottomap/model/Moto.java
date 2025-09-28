package br.com.fiap.mottomap.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Representa a entidade Moto
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Moto {

    // Chave primária da tabela
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Placa da moto, com validação para o formato padrão e Mercosul.
    @NotBlank(message = "A placa é obrigatória")
    @Size(min = 7, max = 7, message = "A placa deve conter exatamente 7 caracteres.")
    @Pattern(regexp = "^[A-Z]{3}[0-9]{1}[A-Z0-9]{1}[0-9]{2}$|^[A-Z]{3}[0-9]{4}$", message = "Formato da placa inválido. Exemplo: ABC1D23")
    private String placa;

    // Número do chassi, com validação de tamanho e caracteres.
    @NotBlank(message = "O chassi é obrigatório")
    @Size(min = 17, max = 17, message = "O chassi deve conter exatamente 17 caracteres.")
    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "O chassi possui caracteres inválidos.")
    private String chassi;

    // Modelo da moto, utilizando o Enum ModeloMoto.
    @NotNull(message = "O modelo é obrigatório.")
    @Enumerated(EnumType.STRING) // Grava o nome do enum (ex: "POP_110I") no banco, em vez de um número.
    private ModeloMoto modeloMoto;

    // Ano de fabricação da moto.
    @NotNull(message = "O ano é obrigatório")
    @Min(value = 1990, message = "O ano deve ser a partir de 1990")
    @Max(value = 2100, message = "O ano deve ser até 2100")
    private Integer ano;

    // Status geral da moto (ativa ou inativa na frota).
    @NotNull(message = "O status é obrigatório.")
    @Enumerated(EnumType.STRING)
    private StatusMoto statusMoto;

    // Relacionamento Muitos-para-Um: Muitas motos podem pertencer a uma filial.
    @NotNull(message = "A filial é obrigatória")
    @ManyToOne
    private Filial filial;
}