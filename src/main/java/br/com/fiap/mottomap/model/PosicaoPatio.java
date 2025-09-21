package br.com.fiap.mottomap.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class PosicaoPatio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A identificação é obrigátoria")
    @Size(min = 2, message = "A identificação deve ter no minimo 2 caracteres")
    private String identificacao;

    @NotNull(message = "O numero da fila é obrigátorio")
    @Positive(message = "O numero não pode ser zero ou negativo")
    private int numeroLinha;

    @NotNull(message = "O numero da coluna é obrigátorio")
    @Positive(message = "O numero não pode ser zero ou negativo")
    private int numeroColuna;

    @NotNull(message = "A area é obrigatória")
    @Enumerated(EnumType.STRING)
    private Area area;

    @NotNull(message = "O campo 'ocupado' deve ser informado")
    private Boolean ocupado;

    @NotNull(message = "campo obrigatório")
    @ManyToOne
    private Filial filial;
}
