package br.com.fiap.mottomap.service;

import br.com.fiap.mottomap.dto.UsuarioDTO;
import br.com.fiap.mottomap.model.CargoUsuario;
import br.com.fiap.mottomap.model.Filial;
import br.com.fiap.mottomap.model.Usuario;
import br.com.fiap.mottomap.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Testes automatizados para o UsuarioService
 * Testa as regras de negócio relacionadas à entidade Usuario
 */
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private FilialService filialService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Filial filial;
    private Usuario usuario;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        // Cria uma filial de teste
        filial = Filial.builder()
                .id(1L)
                .nome("Filial Teste")
                .endereco("Rua Teste, 123")
                .cidade("São Paulo")
                .siglaEstado("SP")
                .numeroColuna(10)
                .numeroLinha(10)
                .capacidadeMaxima(100)
                .build();

        // Cria um usuário de teste
        usuario = Usuario.builder()
                .id(1L)
                .nome("João Silva")
                .username("joao@teste.com")
                .password("senhaCriptografada")
                .cargoUsuario(CargoUsuario.ADM_LOCAL)
                .filial(filial)
                .ativo(true)
                .build();

        // Cria um DTO de teste
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(null);
        usuarioDTO.setNome("Maria Santos");
        usuarioDTO.setUsername("maria@teste.com");
        usuarioDTO.setPassword("senha123");
        usuarioDTO.setCargoUsuario(CargoUsuario.COL_MECANICO);
        usuarioDTO.setFilialId(1L);
        usuarioDTO.setAtivo(true);
    }

    /**
     * Teste 1: Deve criar novo usuário com sucesso quando senha é fornecida
     */
    @Test
    void deveCriarNovoUsuarioComSucessoQuandoSenhaFornecida() {
        // Arrange
        when(filialService.buscarPorId(usuarioDTO.getFilialId())).thenReturn(filial);
        when(passwordEncoder.encode(anyString())).thenReturn("senhaCriptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        usuarioService.salvar(usuarioDTO);

        // Assert
        verify(filialService).buscarPorId(usuarioDTO.getFilialId());
        verify(passwordEncoder).encode(usuarioDTO.getPassword());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    /**
     * Teste 2: Deve lançar exceção ao criar usuário sem senha
     */
    @Test
    void deveLancarExcecaoAoCriarUsuarioSemSenha() {
        // Arrange
        usuarioDTO.setPassword(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.salvar(usuarioDTO);
        });

        assertEquals("A senha é obrigatória para novos usuários.", exception.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    /**
     * Teste 3: Deve atualizar usuário existente sem alterar senha quando senha não fornecida
     */
    @Test
    void deveAtualizarUsuarioSemAlterarSenhaQuandoSenhaNaoFornecida() {
        // Arrange
        usuarioDTO.setId(1L);
        usuarioDTO.setPassword(null); // Senha não fornecida na edição

        when(usuarioRepository.findById(usuarioDTO.getId())).thenReturn(Optional.of(usuario));
        when(filialService.buscarPorId(usuarioDTO.getFilialId())).thenReturn(filial);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        usuarioService.salvar(usuarioDTO);

        // Assert
        verify(usuarioRepository).findById(usuarioDTO.getId());
        verify(passwordEncoder, never()).encode(anyString()); // Senha não deve ser criptografada
        verify(usuarioRepository).save(any(Usuario.class));
    }

    /**
     * Teste 4: Deve atualizar senha quando nova senha é fornecida na edição
     */
    @Test
    void deveAtualizarSenhaQuandoNovaSenhaFornecida() {
        // Arrange
        usuarioDTO.setId(1L);
        usuarioDTO.setPassword("novaSenha123");

        when(usuarioRepository.findById(usuarioDTO.getId())).thenReturn(Optional.of(usuario));
        when(filialService.buscarPorId(usuarioDTO.getFilialId())).thenReturn(filial);
        when(passwordEncoder.encode("novaSenha123")).thenReturn("novaSenhaCriptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        usuarioService.salvar(usuarioDTO);

        // Assert
        verify(passwordEncoder).encode("novaSenha123");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    /**
     * Teste 5: Deve desativar usuário com sucesso
     */
    @Test
    void deveDesativarUsuarioComSucesso() {
        // Arrange
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        usuarioService.desativar(usuario.getId());

        // Assert
        assertFalse(usuario.isAtivo());
        verify(usuarioRepository).save(usuario);
    }

    /**
     * Teste 6: Deve ativar usuário com sucesso
     */
    @Test
    void deveAtivarUsuarioComSucesso() {
        // Arrange
        usuario.setAtivo(false);
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        usuarioService.ativar(usuario.getId());

        // Assert
        assertTrue(usuario.isAtivo());
        verify(usuarioRepository).save(usuario);
    }

    /**
     * Teste 7: Deve lançar exceção ao buscar usuário inexistente
     */
    @Test
    void deveLancarExcecaoAoBuscarUsuarioInexistente() {
        // Arrange
        Long idInexistente = 999L;
        when(usuarioRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.buscarPorId(idInexistente);
        });

        assertTrue(exception.getMessage().contains("Usuário não encontrado"));
    }
}