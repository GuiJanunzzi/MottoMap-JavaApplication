package br.com.fiap.mottomap.service;

import br.com.fiap.mottomap.dto.PasswordChangeDTO;
import br.com.fiap.mottomap.dto.UsuarioDTO; // Importar DTO
import br.com.fiap.mottomap.model.Usuario;
import br.com.fiap.mottomap.repository.FilialRepository;
import br.com.fiap.mottomap.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final FilialRepository filialRepository; // Precisamos para buscar a filial
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository uRepo, FilialRepository fRepo, PasswordEncoder encoder) {
        this.usuarioRepository = uRepo;
        this.filialRepository = fRepo;
        this.passwordEncoder = encoder;
    }

    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    public void salvar(UsuarioDTO usuarioDTO) {
        Usuario usuario;

        // Se é um usuário novo (ID nulo)
        if (usuarioDTO.getId() == null) {
            if (usuarioDTO.getPassword() == null || usuarioDTO.getPassword().isEmpty()) {
                throw new IllegalArgumentException("A senha é obrigatória para novos usuários.");
            }
            usuario = new Usuario();
            usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        } else {
            // Se é um usuário existente
            usuario = buscarPorId(usuarioDTO.getId());
            // Só atualiza a senha se uma nova foi fornecida
            if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
                usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
            }
        }

        // Mapeia os outros campos do DTO para a entidade
        usuario.setNome(usuarioDTO.getNome());
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setCargoUsuario(usuarioDTO.getCargoUsuario());
        var filial = filialRepository.findById(usuarioDTO.getFilialId()).orElseThrow();
        usuario.setFilial(filial);
        usuario.setAtivo(usuarioDTO.isAtivo());

        usuarioRepository.save(usuario);
    }

    public void desativar(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    public void ativar(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(true);
        usuarioRepository.save(usuario);
    }

    public void alterarSenha(String username, PasswordChangeDTO dto) {
        // 1. Verifica se a nova senha e a confirmação são iguais
        if (!dto.getNovaSenha().equals(dto.getConfirmacaoSenha())) {
            throw new IllegalArgumentException("A nova senha e a confirmação não coincidem.");
        }

        // 2. Busca o usuário no banco
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        // 3. Verifica se a "senha atual" fornecida bate com a senha do banco
        if (!passwordEncoder.matches(dto.getSenhaAtual(), usuario.getPassword())) {
            throw new IllegalArgumentException("A senha atual está incorreta.");
        }

        // 4. Se tudo estiver certo, criptografa e salva a nova senha
        usuario.setPassword(passwordEncoder.encode(dto.getNovaSenha()));
        usuarioRepository.save(usuario);
    }
}
