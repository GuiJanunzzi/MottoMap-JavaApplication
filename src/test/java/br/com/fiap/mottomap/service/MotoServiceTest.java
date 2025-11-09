package br.com.fiap.mottomap.service;

import br.com.fiap.mottomap.model.Filial;
import br.com.fiap.mottomap.model.Moto;
import br.com.fiap.mottomap.model.ModeloMoto;
import br.com.fiap.mottomap.model.Problema;
import br.com.fiap.mottomap.model.StatusMoto;
import br.com.fiap.mottomap.repository.MotoRepository;
import br.com.fiap.mottomap.repository.PosicaoPatioRepository;
import br.com.fiap.mottomap.repository.ProblemaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes automatizados para o MotoService
 * Testa as regras de negócio relacionadas à entidade Moto
 */
@ExtendWith(MockitoExtension.class)
class MotoServiceTest {

    @Mock
    private MotoRepository motoRepository;

    @Mock
    private ProblemaRepository problemaRepository;

    @Mock
    private PosicaoPatioRepository posicaoPatioRepository;

    @InjectMocks
    private MotoService motoService;

    private Filial filial;
    private Moto moto;

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

        // Cria uma moto de teste
        moto = Moto.builder()
                .id(1L)
                .placa("ABC1D23")
                .chassi("9BW11111111111111")
                .modeloMoto(ModeloMoto.POP_110I)
                .ano(2023)
                .statusMoto(StatusMoto.ATIVA)
                .filial(filial)
                .build();
    }

    /**
     * Teste 1: Deve salvar uma moto com sucesso quando a placa e chassi são únicos
     */
    @Test
    void deveSalvarMotoComSucessoQuandoPlacaEChassiSaoUnicos() {
        // Arrange
        when(motoRepository.findByPlaca(moto.getPlaca())).thenReturn(Optional.empty());
        when(motoRepository.findByChassi(moto.getChassi())).thenReturn(Optional.empty());
        when(motoRepository.save(any(Moto.class))).thenReturn(moto);

        // Act
        motoService.salvar(moto);

        // Assert
        verify(motoRepository).findByPlaca(moto.getPlaca());
        verify(motoRepository).findByChassi(moto.getChassi());
        verify(motoRepository).save(moto);
    }

    /**
     * Teste 2: Deve lançar exceção ao tentar salvar moto com placa duplicada
     */
    @Test
    void deveLancarExcecaoQuandoPlacaJaExiste() {
        // Arrange
        Moto motoExistente = Moto.builder()
                .id(2L)
                .placa("ABC1D23")
                .chassi("9BW22222222222222")
                .modeloMoto(ModeloMoto.MOTTU_SPORT)
                .ano(2022)
                .statusMoto(StatusMoto.ATIVA)
                .filial(filial)
                .build();

        when(motoRepository.findByPlaca(moto.getPlaca())).thenReturn(Optional.of(motoExistente));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            motoService.salvar(moto);
        });

        assertTrue(exception.getMessage().contains("placa"));
        assertTrue(exception.getMessage().contains("já está cadastrada"));
        verify(motoRepository, never()).save(any(Moto.class));
    }

    /**
     * Teste 3: Deve lançar exceção ao tentar salvar moto com chassi duplicado
     */
    @Test
    void deveLancarExcecaoQuandoChassiJaExiste() {
        // Arrange
        Moto motoExistente = Moto.builder()
                .id(2L)
                .placa("XYZ9A87")
                .chassi("9BW11111111111111")
                .modeloMoto(ModeloMoto.MOTTU_SPORT)
                .ano(2022)
                .statusMoto(StatusMoto.ATIVA)
                .filial(filial)
                .build();

        when(motoRepository.findByPlaca(moto.getPlaca())).thenReturn(Optional.empty());
        when(motoRepository.findByChassi(moto.getChassi())).thenReturn(Optional.of(motoExistente));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            motoService.salvar(moto);
        });

        assertTrue(exception.getMessage().contains("chassi"));
        assertTrue(exception.getMessage().contains("já está cadastrado"));
        verify(motoRepository, never()).save(any(Moto.class));
    }

    /**
     * Teste 4: Deve permitir atualizar moto existente com mesma placa e chassi
     */
    @Test
    void devePermitirAtualizarMotoExistenteComMesmaPlacaEChassi() {
        // Arrange
        when(motoRepository.findByPlaca(moto.getPlaca())).thenReturn(Optional.of(moto));
        when(motoRepository.findByChassi(moto.getChassi())).thenReturn(Optional.of(moto));
        when(motoRepository.save(any(Moto.class))).thenReturn(moto);

        // Act
        motoService.salvar(moto);

        // Assert
        verify(motoRepository).save(moto);
    }

    /**
     * Teste 5: Deve lançar exceção ao tentar deletar moto que está alocada no pátio
     */
    @Test
    void deveLancarExcecaoAoDeletarMotoAlocadaNoPatio() {
        // Arrange
        when(motoRepository.findById(moto.getId())).thenReturn(Optional.of(moto));
        when(posicaoPatioRepository.findByMotoId(moto.getId())).thenReturn(Optional.of(any()));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            motoService.deletarPorId(moto.getId());
        });

        assertTrue(exception.getMessage().contains("alocada em uma posição"));
        verify(motoRepository, never()).delete(any(Moto.class));
    }

    /**
     * Teste 6: Deve lançar exceção ao tentar deletar moto com problemas registrados
     */
    @Test
    void deveLancarExcecaoAoDeletarMotoComProblemasRegistrados() {
        // Arrange
        List<Problema> problemas = new ArrayList<>();
        problemas.add(Problema.builder().id(1L).build()); // Simula um problema

        when(motoRepository.findById(moto.getId())).thenReturn(Optional.of(moto));
        when(posicaoPatioRepository.findByMotoId(moto.getId())).thenReturn(Optional.empty());
        when(problemaRepository.findByMotoId(moto.getId())).thenReturn(problemas);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            motoService.deletarPorId(moto.getId());
        });

        assertTrue(exception.getMessage().contains("registros de problemas"));
        verify(motoRepository, never()).delete(any(Moto.class));
    }

    /**
     * Teste 7: Deve deletar moto com sucesso quando não há restrições
     */
    @Test
    void deveDeletarMotoComSucessoQuandoNaoHaRestricoes() {
        // Arrange
        when(motoRepository.findById(moto.getId())).thenReturn(Optional.of(moto));
        when(posicaoPatioRepository.findByMotoId(moto.getId())).thenReturn(Optional.empty());
        when(problemaRepository.findByMotoId(moto.getId())).thenReturn(new ArrayList<>());

        // Act
        motoService.deletarPorId(moto.getId());

        // Assert
        verify(motoRepository).delete(moto);
    }

    /**
     * Teste 8: Deve lançar exceção ao buscar moto inexistente
     */
    @Test
    void deveLancarExcecaoAoBuscarMotoInexistente() {
        // Arrange
        Long idInexistente = 999L;
        when(motoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            motoService.buscarPorId(idInexistente);
        });

        assertTrue(exception.getMessage().contains("Moto não encontrada"));
    }
}

