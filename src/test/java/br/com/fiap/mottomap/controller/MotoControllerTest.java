package br.com.fiap.mottomap.controller;

import br.com.fiap.mottomap.model.Filial;
import br.com.fiap.mottomap.model.Moto;
import br.com.fiap.mottomap.model.ModeloMoto;
import br.com.fiap.mottomap.model.StatusMoto;
import br.com.fiap.mottomap.service.FilialService;
import br.com.fiap.mottomap.service.MotoService;
import br.com.fiap.mottomap.service.PosicaoPatioService;
import br.com.fiap.mottomap.service.ProblemaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes automatizados para o MotoController
 * Testa os endpoints e a lógica de controle relacionada à entidade Moto
 */
@WebMvcTest(MotoController.class)
class MotoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MotoService motoService;

    @MockBean
    private FilialService filialService;

    @MockBean
    private ProblemaService problemaService;

    @MockBean
    private PosicaoPatioService posicaoPatioService;

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
     * Teste 1: Deve listar todas as motos quando usuário autenticado acessa /motos
     */
    @Test
    @WithMockUser
    void deveListarTodasAsMotos() throws Exception {
        // Arrange
        List<Moto> motos = new ArrayList<>();
        motos.add(moto);
        when(motoService.buscarTodas()).thenReturn(motos);

        // Act & Assert
        mockMvc.perform(get("/motos"))
                .andExpect(status().isOk())
                .andExpect(view().name("motos/list"))
                .andExpect(model().attributeExists("motos"));

        verify(motoService).buscarTodas();
    }

    /**
     * Teste 2: Deve exibir formulário de cadastro para ADM_GERAL com lista de filiais
     */
    @Test
    @WithMockUser(authorities = "ADM_GERAL", username = "admin@teste.com")
    void deveExibirFormularioCadastroParaAdmGeralComFiliais() throws Exception {
        // Arrange
        List<Filial> filiais = new ArrayList<>();
        filiais.add(filial);
        when(filialService.buscarTodas()).thenReturn(filiais);

        // Act & Assert
        mockMvc.perform(get("/motos/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("motos/form"))
                .andExpect(model().attributeExists("moto"))
                .andExpect(model().attributeExists("filiais"));

        verify(filialService).buscarTodas();
    }

    /**
     * Teste 3: Deve salvar moto com sucesso e redirecionar para lista
     */
    @Test
    @WithMockUser(authorities = "ADM_GERAL", username = "admin@teste.com")
    void deveSalvarMotoComSucessoERedirecionar() throws Exception {
        // Arrange
        doNothing().when(motoService).salvar(any(Moto.class));

        // Act & Assert
        mockMvc.perform(post("/motos")
                        .param("placa", "ABC1D23")
                        .param("chassi", "9BW11111111111111")
                        .param("modeloMoto", "POP_110I")
                        .param("ano", "2023")
                        .param("statusMoto", "ATIVA")
                        .param("filial.id", "1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/motos"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(motoService).salvar(any(Moto.class));
    }

    /**
     * Teste 4: Deve exibir detalhes da moto quando acessa /motos/{id}
     */
    @Test
    @WithMockUser
    void deveExibirDetalhesDaMoto() throws Exception {
        // Arrange
        when(motoService.buscarPorId(1L)).thenReturn(moto);
        when(problemaService.buscarPorMotoId(1L)).thenReturn(new ArrayList<>());
        when(posicaoPatioService.buscarPorMotoId(1L)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/motos/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("motos/details"))
                .andExpect(model().attributeExists("moto"))
                .andExpect(model().attributeExists("problemas"));

        verify(motoService).buscarPorId(1L);
        verify(problemaService).buscarPorMotoId(1L);
    }

    /**
     * Teste 5: Deve deletar moto com sucesso e redirecionar
     */
    @Test
    @WithMockUser
    void deveDeletarMotoComSucesso() throws Exception {
        // Arrange
        doNothing().when(motoService).deletarPorId(1L);

        // Act & Assert
        mockMvc.perform(get("/motos/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/motos"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(motoService).deletarPorId(1L);
    }

    /**
     * Teste 6: Deve exibir mensagem de erro ao deletar moto com restrições
     */
    @Test
    @WithMockUser
    void deveExibirErroAoDeletarMotoComRestricoes() throws Exception {
        // Arrange
        doThrow(new IllegalStateException("Não é possível excluir a moto, pois ela está alocada em uma posição no pátio."))
                .when(motoService).deletarPorId(1L);

        // Act & Assert
        mockMvc.perform(get("/motos/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/motos"))
                .andExpect(flash().attributeExists("errorMessage"));

        verify(motoService).deletarPorId(1L);
    }
}