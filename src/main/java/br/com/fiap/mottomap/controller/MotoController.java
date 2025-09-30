package br.com.fiap.mottomap.controller;

import br.com.fiap.mottomap.model.CargoUsuario;
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
    public String mostrarFormulario(Model model, @AuthenticationPrincipal Usuario usuarioLogado) {
        var moto = new Moto();
        // Diferencia o ADM_LOCAL do ADM_GERAL
        if (usuarioLogado.getCargoUsuario() == CargoUsuario.ADM_LOCAL) {
            moto.setFilial(usuarioLogado.getFilial());
        } else {
            // A lista de filiais só é enviada para o ADM_GERAL
            model.addAttribute("filiais", filialService.buscarTodas());
        }
        model.addAttribute("moto", moto);
        return "motos/form";
    }

    // Processa os dados do formulario para salvar uma moto nova ou editada
    @PostMapping
    public String salvarMoto(@Valid @ModelAttribute("moto") Moto moto,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal Usuario usuarioLogado) {

        // Garante que um ADM_LOCAL só pode salvar motos na sua própria filial.
        if (usuarioLogado.getCargoUsuario() == CargoUsuario.ADM_LOCAL) {
            if (moto.getFilial().getId() != usuarioLogado.getFilial().getId()) {
                throw new SecurityException("Acesso negado: tentativa de cadastro em filial não permitida.");
            }
        }

        // Checa se há erros das anotações de validação
        if (result.hasErrors()) {
            // Se der erro de validação, recarrega os dados necessários e volta ao form
            if (usuarioLogado.getCargoUsuario() == CargoUsuario.ADM_GERAL) {
                model.addAttribute("filiais", filialService.buscarTodas());
            }
            return "motos/form";
        }

        // Tenta salvar a moto, tratando possíveis erros.
        try {
            motoService.salvar(moto);
            redirectAttributes.addFlashAttribute("successMessage", "Moto salva com sucesso!");
        } catch (IllegalStateException e) {
            // Captura exceções de regras de negócio do serviço (ex: placa ou chassi duplicado).
            // Adiciona o erro ao campo específico ('placa' ou 'chassi') para ser exibido na tela.
            result.rejectValue(e.getMessage().contains("placa") ? "placa" : "chassi", "duplicado", e.getMessage());
            if (usuarioLogado.getCargoUsuario() == CargoUsuario.ADM_GERAL) {
                model.addAttribute("filiais", filialService.buscarTodas());
            }
            return "motos/form";
        }

        // redireciona para a lista de motos.
        return "redirect:/motos";
    }

    // Exibe o formulário de edição para uma moto específica
    @GetMapping("/edit/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model, @AuthenticationPrincipal Usuario usuarioLogado) {
        model.addAttribute("moto", motoService.buscarPorId(id));
        // Apenas o ADM_GERAL precisa da lista de todas as filiais para (potencialmente) transferir uma moto
        if (usuarioLogado.getCargoUsuario() == CargoUsuario.ADM_GERAL) {
            model.addAttribute("filiais", filialService.buscarTodas());
        }
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