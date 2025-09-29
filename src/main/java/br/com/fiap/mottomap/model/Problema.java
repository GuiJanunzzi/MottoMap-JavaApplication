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

// Representa um problema ou defeito reportado para uma moto específica.
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Problema {

    // Chave primária da tabela
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Categoria do problema (MECANICO, ELETRICO, etc.) para facilitar a triagem.
    @NotNull(message = "O tipo do problema é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoProblema tipoProblema;

    // Detalhes sobre o problema reportado.
    @NotBlank(message = "A descrição é obrigatória")
    @Size(min = 10, max = 255, message = "A descrição deve ter entre 10 e 255 caracteres")
    private String descricao;

    // Data em que o problema foi registrado no sistema.
    @PastOrPresent(message = "A data não pode ser no futuro")
    @JsonFormat(pattern = "dd/MM/yyyy") // Anotação do Jackson, útil para APIs REST, mas não tem efeito no Thymeleaf.
    private LocalDate dataRegistro;

    // Indica se o problema já foi solucionado (true) ou se está pendente (false).
    @NotNull(message = "O status de resolução é obrigatório")
    private Boolean resolvido = false;

    // Relacionamento Muitos-para-Um: Muitos problemas podem ser associados a uma moto.
    @ManyToOne
    private Moto moto;

    // Relacionamento Muitos-para-Um: Muitos problemas podem ser registrados por um mesmo usuário.
    @ManyToOne
    private Usuario usuario;
}