package br.com.fiap.mottomap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.mottomap.model.Moto;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

// Interface do Spring Data JPA para a entidade Moto.
public interface MotoRepository extends JpaRepository<Moto, Long>{
    // Query customizada para encontrar todas as motos de uma filial específica que ainda não foram alocadas em nenhuma posição do pátio.
    @Query("SELECT m FROM Moto m WHERE m.filial.id = :filialId AND m.id NOT IN (SELECT p.moto.id FROM PosicaoPatio p WHERE p.moto.id IS NOT NULL)")
    List<Moto> findMotosSemPosicaoNaFilial(Long filialId);

    // Query customizada para encontrar as motos de uma filial que possuem pelo menos um problema com o status 'resolvido = false'.
    // O DISTINCT garante que cada moto apareça apenas uma vez, mesmo que tenha vários problemas.
    @Query("SELECT DISTINCT p.moto FROM Problema p WHERE p.resolvido = false AND p.moto.filial.id = :filialId")
    List<Moto> findMotosComProblemasNaoResolvidosNaFilial(Long filialId);

    Optional<Moto> findByPlaca(String placa);

    Optional<Moto> findByChassi(String chassi);

}
