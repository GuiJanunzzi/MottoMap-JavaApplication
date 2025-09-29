package br.com.fiap.mottomap.controller;

import br.com.fiap.mottomap.model.Moto;
import br.com.fiap.mottomap.model.Usuario;
import br.com.fiap.mottomap.service.FilialService;
import br.com.fiap.mottomap.service.MotoService;
import br.com.fiap.mottomap.service.PosicaoPatioService;
import br.com.fiap.mottomap.service.ProblemaService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Controller para todas as telas e ações relacionadas à entidade Moto
@Controller
@RequestMapping("/motos")
public class MotoController {

    private final MotoService motoService;
    private final FilialService filialService;
    private final ProblemaService problemaService;
    private final PosicaoPatioService posicaoPatioService;

    // Injeção de dependencias dos serviços que o controller precisa
    public MotoController(MotoService motoService, FilialService filialService, ProblemaService problemaService, PosicaoPatioService posicaoPatioService) {
        this.motoService = motoService;
        this.filialService = filialService;
        this.problemaService = problemaService;
        this.posicaoPatioService = posicaoPatioService;
    }

    // Exibe a pagina com a lista de todas as motos
    @GetMapping
    public String listarMotos(Model model) {
        model.addAttribute("motos", motoService.buscarTodas());
        return "motos/list";
    }

    // Exibe o formulario para cadastrar uma nova moto
    @GetMapping("/new")
    public String mostrarFormulario(Model model) {
        model.addAttribute("moto", new Moto());
        model.addAttribute("filiais", filialService.buscarTodas());
        return "motos/form";
    }

    // Processa os dados do formulario para salvar uma moto nova ou editada
    @PostMapping
    public String salvarMoto(@Valid @ModelAttribute("moto") Moto moto, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // Se a validação falhar, recarrega a lista de filiais e volta ao formulário
            model.addAttribute("filiais", filialService.buscarTodas());
            return "motos/form";
        }
        motoService.salvar(moto);
        redirectAttributes.addFlashAttribute("successMessage", "Moto salva com sucesso!");
        return "redirect:/motos";
    }

    // Exibe o formulário de edição para uma moto específica
    @GetMapping("/edit/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {
        model.addAttribute("moto", motoService.buscarPorId(id));
        model.addAttribute("filiais", filialService.buscarTodas());
        return "motos/form";
    }

    // Deleta uma moto
    @GetMapping("/delete/{id}")
    public String deletarMoto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            motoService.deletarPorId(id);
            redirectAttributes.addFlashAttribute("successMessage", "Moto deletada com sucesso!");
        } catch (Exception e) {
            // Captura a exceção do serviço e exibe a mensagem de erro na tela
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/motos";
    }

    // Exibe a pagina de detalhes de uma moto, incluindo seus problemas e posição no pátio
    @GetMapping("/{id}")
    public String verDetalhesDaMoto(@PathVariable Long id, Model model) {
        model.addAttribute("moto", motoService.buscarPorId(id));
        model.addAttribute("problemas", problemaService.buscarPorMotoId(id));
        // Busca a posição da moto e, se encontrar, adiciona ao modelo
        posicaoPatioService.buscarPorMotoId(id).ifPresent(posicao -> model.addAttribute("posicaoPatio", posicao));
        return "motos/details";
    }

    // Exibe uma lista de motos com problemas pendentes (visão do Mecânico)
    @GetMapping("/pendentes")
    @PreAuthorize("hasAuthority('COL_MECANICO')")
    public String listarMotosPendentes(Model model, @AuthenticationPrincipal Usuario usuarioLogado) {
        if (usuarioLogado.getFilial() == null) {
            return "redirect:/?error=no_filial";
        }
        Long filialId = usuarioLogado.getFilial().getId();
        model.addAttribute("motos", motoService.buscarMotosComProblemasNaoResolvidos(filialId));
        return "motos/pendentes";
    }

    // Exibe uma lista de motos que não estão alocadas em nenhuma vaga (visão do Colaborador de Pátio)
    @GetMapping("/sem-posicao")
    @PreAuthorize("hasAnyAuthority('COL_PATIO', 'ADM_GERAL', 'ADM_LOCAL')")
    public String listarMotosSemPosicao(Model model, @AuthenticationPrincipal Usuario usuarioLogado) {
        if (usuarioLogado.getFilial() == null) {
            return "redirect:/?error=no_filial";
        }
        Long filialId = usuarioLogado.getFilial().getId();
        model.addAttribute("motos", motoService.buscarMotosSemPosicao(filialId));
        model.addAttribute("filialId", filialId);
        return "motos/sem-posicao";
    }
}