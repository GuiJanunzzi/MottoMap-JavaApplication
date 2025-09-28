package br.com.fiap.mottomap.service;

import br.com.fiap.mottomap.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Serviço responsável por integrar o sistema de usuários do banco de dados com o Spring Security.
@Service
public class AuthenticationService implements UserDetailsService {

    // Injeta o repositório de usuários para poder consultar o banco.
    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca o usuário no banco de dados pelo username.
        // Se não encontrar, lança uma exceção padrão do Spring Security.
        // Se encontrar, retorna o objeto do usuário (que implementa UserDetails).
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }
}