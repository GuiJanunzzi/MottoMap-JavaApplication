package br.com.fiap.mottomap.service;

import br.com.fiap.mottomap.dto.PasswordChangeDTO;
import br.com.fiap.mottomap.dto.UsuarioDTO;
import br.com.fiap.mottomap.model.Usuario;
import br.com.fiap.mottomap.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

// Serviço que centraliza toda a lógica de negócio relacionada a Usuários.
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final FilialService filialService; // ALTERADO: Usa FilialService
    private final PasswordEncoder passwordEncoder;

    // Construtor atualizado para injetar FilialService
    public UsuarioService(UsuarioRepository uRepo, FilialService fService, PasswordEncoder encoder) {
        this.usuarioRepository = uRepo;
        this.filialService = fService;
        this.passwordEncoder = encoder;
    }

    // Retorna todos os usuários cadastrados.
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }

    // Busca um usuário pelo ID, lançando erro se não for encontrado.
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    // Metodo principal para criar ou atualizar um usuário a partir dos dados do formulário.
    public void salvar(UsuarioDTO usuarioDTO) {
        Usuario usuario;

        // Lógica para CRIAR um novo usuário.
        if (usuarioDTO.getId() == null) {
            // Garante que a senha não seja nula ao criar um novo usuário.
            if (usuarioDTO.getPassword() == null || usuarioDTO.getPassword().isEmpty()) {
                throw new IllegalArgumentException("A senha é obrigatória para novos usuários.");
            }
            usuario = new Usuario();
            // Criptografa a senha antes de salvar.
            usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        } else {
            // Lógica para ATUALIZAR um usuário existente.
            usuario = buscarPorId(usuarioDTO.getId());
            // Verifica se uma nova senha foi informada no formulário de edição.
            if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
                // Se sim, criptografa e atualiza a senha. Se não, a senha antiga é mantida.
                usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
            }
        }

        // Mapeia os outros dados do DTO para a entidade antes de salvar.
        usuario.setNome(usuarioDTO.getNome());
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setCargoUsuario(usuarioDTO.getCargoUsuario());
        // Busca a entidade Filial completa a partir do ID vindo do DTO.
        var filial = filialService.buscarPorId(usuarioDTO.getFilialId());
        usuario.setFilial(filial);
        usuario.setAtivo(usuarioDTO.isAtivo());

        usuarioRepository.save(usuario);
    }

    // Lógica de "soft-delete" para desativar um usuário.
    public void desativar(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    // Reativa a conta de um usuário.
    public void ativar(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(true);
        usuarioRepository.save(usuario);
    }

    // Lógica para o próprio usuário alterar sua senha na tela "Minha Conta".
    public void alterarSenha(String username, PasswordChangeDTO dto) {
        // Verifica se a nova senha e a confirmação são idênticas.
        if (!dto.getNovaSenha().equals(dto.getConfirmacaoSenha())) {
            throw new IllegalArgumentException("A nova senha e a confirmação não coincidem.");
        }

        // Busca o usuário que está tentando alterar a senha.
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        // Verifica se a 'senha atual' informada no formulário corresponde à senha criptografada no banco.
        if (!passwordEncoder.matches(dto.getSenhaAtual(), usuario.getPassword())) {
            throw new IllegalArgumentException("A senha atual está incorreta.");
        }

        // Se todas as verificações passarem, criptografa e atualiza a nova senha.
        usuario.setPassword(passwordEncoder.encode(dto.getNovaSenha()));
        usuarioRepository.save(usuario);
    }
}