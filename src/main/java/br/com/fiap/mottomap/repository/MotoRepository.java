package br.com.fiap.mottomap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.mottomap.model.Moto;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MotoRepository extends JpaRepository<Moto, Long>{
    @Query("SELECT m FROM Moto m WHERE m.filial.id = :filialId AND m.id NOT IN (SELECT p.moto.id FROM PosicaoPatio p WHERE p.moto.id IS NOT NULL)")
    List<Moto> findMotosSemPosicaoNaFilial(Long filialId);
}
