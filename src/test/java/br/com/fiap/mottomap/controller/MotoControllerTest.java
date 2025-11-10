package br.com.fiap.mottomap.controller;

import br.com.fiap.mottomap.model.*;
import br.com.fiap.mottomap.service.FilialService;
import br.com.fiap.mottomap.service.MotoService;
import br.com.fiap.mottomap.service.PosicaoPatioService;
import br.com.fiap.mottomap.service.ProblemaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
// O UserDetailsService foi removido, não precisamos mais dele
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // <<< IMPORT ADICIONADO
import org.springframework.security.core.Authentication; // <<< IMPORT ADICIONADO
import org.springframework.security.core.authority.SimpleGrantedAuthority; // <<< IMPORT ADICIONADO
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication; // <<< IMPORT ADICIONADO
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

    // @MockBean private UserDetailsService userDetailsService; // <<< REMOVIDO

    private Filial filial;
    private Moto moto;
    private Usuario admGeral;

    @BeforeEach
    void setUp() {
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

        moto = Moto.builder()
                .id(1L)
                .placa("ABC1D23")
                .chassi("9BW11111111111111")
                .modeloMoto(ModeloMoto.POP_110I)
                .ano(2023)
                .statusMoto(StatusMoto.ATIVA)
                .filial(filial)
                .build();

        admGeral = new Usuario();
        admGeral.setUsername("admin@teste.com");
        admGeral.setCargoUsuario(CargoUsuario.ADM_GERAL);
        admGeral.setFilial(filial);
    }

    /**
     * Teste 1: Deve listar todas as motos quando usuário autenticado acessa /motos
     */
    @Test
    @WithMockUser // Este pode ficar, pois o controller não usa o @AuthenticationPrincipal
    void deveListarTodasAsMotos() throws Exception {
        // ... (código deste teste está correto, sem mudanças)
        List<Moto> motos = new ArrayList<>();
        motos.add(moto);
        when(motoService.buscarTodas()).thenReturn(motos);

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
    // @WithUserDetails("admin@teste.com") // <<< REMOVIDO
    void deveExibirFormularioCadastroParaAdmGeralComFiliais() throws Exception {
        // Arrange

        // --- BLOCO DE AUTENTICAÇÃO MANUAL ADICIONADO ---
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ADM_GERAL"));
        Authentication auth = new UsernamePasswordAuthenticationToken(admGeral, null, authorities);
        // --- FIM DO BLOCO ---

        List<Filial> filiais = new ArrayList<>();
        filiais.add(filial);
        when(filialService.buscarTodas()).thenReturn(filiais);

        // Act & Assert
        mockMvc.perform(get("/motos/new")
                        .with(authentication(auth))) // <<< AUTENTICAÇÃO INJETADA AQUI
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
    // @WithUserDetails("admin@teste.com") // <<< REMOVIDO
    void deveSalvarMotoComSucessoERedirecionar() throws Exception {
        // Arrange

        // --- BLOCO DE AUTENTICAÇÃO MANUAL ADICIONADO ---
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ADM_GERAL"));
        Authentication auth = new UsernamePasswordAuthenticationToken(admGeral, null, authorities);
        // --- FIM DO BLOCO ---

        doNothing().when(motoService).salvar(any(Moto.class));

        // Act & Assert
        mockMvc.perform(post("/motos")
                        .param("placa", "ABC1D23")
                        .param("chassi", "9BW11111111111111")
                        .param("modeloMoto", "POP_110I")
                        .param("ano", "2023")
                        .param("statusMoto", "ATIVA")
                        .param("filial.id", "1")
                        .with(csrf())
                        .with(authentication(auth))) // <<< AUTENTICAÇÃO INJETADA AQUI
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/motos"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(motoService).salvar(any(Moto.class));
    }

    /**
     * Teste 4: Deve exibir detalhes da moto quando acessa /motos/{id}
     */
    @Test
    @WithMockUser // Este pode ficar
    void deveExibirDetalhesDaMoto() throws Exception {
        // ... (código deste teste está correto, sem mudanças)
        when(motoService.buscarPorId(1L)).thenReturn(moto);
        when(problemaService.buscarPorMotoId(1L)).thenReturn(new ArrayList<>());
        when(posicaoPatioService.buscarPorMotoId(1L)).thenReturn(java.util.Optional.empty());

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
    @WithMockUser // Este pode ficar
    void deveDeletarMotoComSucesso() throws Exception {
        // ... (código deste teste está correto, sem mudanças)
        doNothing().when(motoService).deletarPorId(1L);

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
    @WithMockUser // Este pode ficar
    void deveExibirErroAoDeletarMotoComRestricoes() throws Exception {
        // ... (código deste teste está correto, sem mudanças)
        doThrow(new IllegalStateException("Não é possível excluir a moto, pois ela está alocada em uma posição no pátio."))
                .when(motoService).deletarPorId(1L);

        mockMvc.perform(get("/motos/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/motos"))
                .andExpect(flash().attributeExists("errorMessage"));

        verify(motoService).deletarPorId(1L);
    }
}