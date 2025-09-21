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

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Moto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A placa é obrigatória")
    @Size(min = 7, max = 7, message = "A placa deve conter exatamente 7 caracteres.")
    @Pattern(regexp = "^[A-Z]{3}[0-9]{1}[A-Z0-9]{1}[0-9]{2}$|^[A-Z]{3}[0-9]{4}$", message = "Formato da placa inválido. Exemplo: ABC1D23")
    private String placa;

    @NotBlank(message = "O chassi é obrigatório")
    @Size(min = 17, max = 17, message = "O chassi deve conter exatamente 17 caracteres.")
    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "O chassi possui caracteres inválidos.")
    private String chassi;

    @NotNull(message = "O modelo é obrigatório.")
    @Enumerated(EnumType.STRING)
    private ModeloMoto modeloMoto;

    @NotNull(message = "O ano é obrigatório")
    @Min(value = 1990, message = "O ano deve ser apartir de 1990")
    @Max(value = 2100, message = "O ano deve ser até 2100")
    private Integer ano;

    @NotNull(message = "O status é obrigatório.")
    @Enumerated(EnumType.STRING)
    private StatusMoto statusMoto;

    @NotNull(message = "campo obrigatório")
    @ManyToOne
    private Filial filial;
}
