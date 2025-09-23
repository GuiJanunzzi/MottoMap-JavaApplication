package br.com.fiap.mottomap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.mottomap.model.Problema;

import java.util.List;

public interface ProblemaRepository extends JpaRepository<Problema, Long>{
    List<Problema> findByMotoId(Long motoId);
}
