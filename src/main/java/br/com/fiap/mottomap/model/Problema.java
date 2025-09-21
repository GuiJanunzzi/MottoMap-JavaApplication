package br.com.fiap.mottomap.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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
public class Problema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O tipo do problema é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoProblema tipoProblema;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(min = 10, max = 255, message = "A descrição deve ter entre 10 e 255 caracteres")
    private String descricao;

    @NotNull(message = "A data é obrigatória")
    @PastOrPresent(message = "A data não pode ser no futuro")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataRegistro;

    @NotNull(message = "O status de resolução é obrigatório")
    private Boolean resolvido;

    @NotNull(message = "A moto é obrigatória")
    @ManyToOne
    private Moto moto;

    @NotNull(message = "O usuário que reportou é obrigatório")
    @ManyToOne
    private Usuario usuario;
}
