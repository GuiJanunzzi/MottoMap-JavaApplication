package br.com.fiap.mottomap.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

// Representa uma vaga específica (uma posição) no pátio de uma filial.
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PosicaoPatio {

    // Chave primária da tabela
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Código da vaga para identificação visual, ex: "A1", "B12".
    @NotBlank(message = "A identificação é obrigatória")
    @Size(min = 2, message = "A identificação deve ter no mínimo 2 caracteres")
    private String identificacao;

    // Coordenadas (linha e coluna) para desenhar a posição no mapa do pátio.
    @NotNull(message = "O número da linha é obrigatório")
    @Positive(message = "O número não pode ser zero ou negativo")
    private int numeroLinha;

    @NotNull(message = "O número da coluna é obrigatório")
    @Positive(message = "O número não pode ser zero ou negativo")
    private int numeroColuna;

    // Define a qual área do pátio esta vaga pertence (ex: PRONTAS, PROBLEMAS_SIMPLES).
    @NotNull(message = "A area é obrigatória")
    @Enumerated(EnumType.STRING)
    private Area area;

    // Indica se a vaga está ocupada por uma moto (true) ou se está livre (false).
    // Começa como 'false' por padrão para novas posições.
    private Boolean ocupado = false;

    // Relacionamento Muitos-para-Um: Muitas posições pertencem a uma filial.
    @NotNull(message = "A filial é obrigatória")
    @ManyToOne
    private Filial filial;

    // Relacionamento Um-para-Um: Uma posição pode ser ocupada por uma única moto.
    // Este campo será nulo se a vaga estiver vazia.
    @OneToOne
    @JoinColumn(name = "moto_id", unique = true)
    private Moto moto;
}