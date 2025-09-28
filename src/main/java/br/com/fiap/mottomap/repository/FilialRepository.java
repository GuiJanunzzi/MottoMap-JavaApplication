package br.com.fiap.mottomap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.mottomap.model.Filial;

// Interface do Spring Data JPA para a entidade Filial.
public interface FilialRepository extends JpaRepository<Filial, Long>{


}
