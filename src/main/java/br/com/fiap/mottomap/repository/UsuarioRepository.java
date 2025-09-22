package br.com.fiap.mottomap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.mottomap.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{


}
