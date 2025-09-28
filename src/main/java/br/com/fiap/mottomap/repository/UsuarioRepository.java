package br.com.fiap.mottomap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.mottomap.model.Usuario;

import java.util.List;
import java.util.Optional;

// Interface do Spring Data JPA para a entidade Usuario.
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

    // Busca um usuário pelo seu username (que no sistema é o email).
    // Retorna um Optional para tratar de forma segura o caso de o usuário não ser encontrado.
    Optional<Usuario> findByUsername(String username);

    // Busca todos os usuários associados a uma filial específica pelo ID da filial.
    List<Usuario> findByFilialId(Long filialId);
}