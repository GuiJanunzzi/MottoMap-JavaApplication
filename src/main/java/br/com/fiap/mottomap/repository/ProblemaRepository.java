package br.com.fiap.mottomap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.mottomap.model.Problema;

import java.util.List;

// Interface do Spring Data JPA para a entidade Problema.
public interface ProblemaRepository extends JpaRepository<Problema, Long>{

    // Busca todos os problemas associados a uma moto específica pelo ID da moto.
    List<Problema> findByMotoId(Long motoId);
}