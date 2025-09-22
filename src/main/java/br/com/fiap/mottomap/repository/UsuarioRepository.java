package br.com.fiap.mottomap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.mottomap.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    Optional<Usuario> findByUsername(String username);
}

