package br.com.fiap.mottomap.repository;

import br.com.fiap.mottomap.model.Filial;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.mottomap.model.PosicaoPatio;

import java.util.List;
import java.util.Optional;

// Interface do Spring Data JPA para a entidade PosicaoPatio.
public interface PosicaoPatioRepository extends JpaRepository<PosicaoPatio, Long>{
    // Busca todas as posições de uma filial específica.
    List<PosicaoPatio> findByFilialOrderByNumeroLinhaAscNumeroColunaAsc(Filial filial);
    // Busca a posição do pátio que está sendo ocupada por uma moto específica (pelo ID da moto).
    // Retorna um Optional, pois a moto pode não estar em nenhuma posição.
    Optional<PosicaoPatio> findByMotoId(Long motoId);

    // Busca todas as posições associados a uma filial específica pelo ID da filial.
    List<PosicaoPatio> findByFilialId(Long filialId);
}
