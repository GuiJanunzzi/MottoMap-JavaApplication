package br.com.fiap.mottomap.repository;

import br.com.fiap.mottomap.model.Filial;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.mottomap.model.PosicaoPatio;

import java.util.List;

public interface PosicaoPatioRepository extends JpaRepository<PosicaoPatio, Long>{
    List<PosicaoPatio> findByFilialOrderByNumeroLinhaAscNumeroColunaAsc(Filial filial);
}
