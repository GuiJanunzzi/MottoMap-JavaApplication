package br.com.fiap.mottomap.controller;

import br.com.fiap.mottomap.model.PosicaoPatio;
import br.com.fiap.mottomap.model.Usuario;
import br.com.fiap.mottomap.service.FilialService;
import br.com.fiap.mottomap.service.MotoService;
import br.com.fiap.mottomap.service.PosicaoPatioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Controller para gerenciar as posições do pátio e suas interações
@Controller
@RequestMapping("/posicoes")
public class PosicaoPatioController {

    private static final Logger log = LoggerFactory.getLogger(PosicaoPatioController.class);

    private final PosicaoPatioService posicaoPatioService;
    private final MotoService motoService;
    private final FilialService filialService;

    // Injeção dos serviços necessários
    public PosicaoPatioController(PosicaoPatioService posService, MotoService motoSvc, FilialService filialService) {
        this.posicaoPatioService = posService;
        this.motoService = motoSvc;
        this.filialService = filialService;
    }

    // Exibe a lista de todas as posições cadastradas
    @GetMapping
    public String listarPosicoes(Model model) {
        model.addAttribute("posicoes", posicaoPatioService.buscarTodas());
        return "posicoes/list";
    }

    // Exibe o formulário para criar uma nova posição
    @GetMapping("/new")
    public String mostrarFormularioCadastro(Model model, @AuthenticationPrincipal Usuario usuarioLogado) {
        var posicao = new PosicaoPatio();
        // Lógica para adaptar o formulário de acordo com o perfil do usuário
        if (usuarioLogado.getCargoUsuario().name().equals("ADM_LOCAL")) {
            // Se for ADM_LOCAL, a filial já vem preenchida e bloqueada
            posicao.setFilial(usuarioLogado.getFilial());
        } else {
            // Se for ADM_GERAL, ele pode escolher qualquer filial
            model.addAttribute("filiais", filialService.buscarTodas());
        }
        model.addAttribute("posicaoPatio", posicao);
        return "posicoes/form";
    }

    // Processa o salvamento de uma nova posição
    @PostMapping
    public String salvarPosicao(@Valid @ModelAttribute("posicaoPatio") PosicaoPatio posicaoPatio,
                                BindingResult result,
                                Model model,
                                RedirectAttributes attrs,
                                @AuthenticationPrincipal Usuario usuarioLogado) {

        // Validação de segurança para garantir que o ADM_LOCAL só cadastre na própria filial
        if (usuarioLogado.getCargoUsuario().name().equals("ADM_LOCAL")) {
            if (posicaoPatio.getFilial().getId() != usuarioLogado.getFilial().getId()) {
                throw new SecurityException("Acesso negado: tentativa de cadastro em filial não permitida.");
            }
        }

        // Em caso de erro de validação, retorna ao formulário
        if (result.hasErrors()) {
            if (usuarioLogado.getCargoUsuario().name().equals("ADM_GERAL")) {
                model.addAttribute("filiais", filialService.buscarTodas());
            }
            return "posicoes/form";
        }

        posicaoPatioService.salvar(posicaoPatio);
        attrs.addFlashAttribute("successMessage", "Posição salva com sucesso!");
        return "redirect:/posicoes";
    }

    // Exibe o formulário de edição de uma posição
    @GetMapping("/edit/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {
        model.addAttribute("posicaoPatio", posicaoPatioService.buscarPorId(id));
        model.addAttribute("filiais", filialService.buscarTodas());
        return "posicoes/form";
    }

    // Deleta uma posição do pátio
    @GetMapping("/delete/{id}")
    public String deletarPosicao(@PathVariable Long id, RedirectAttributes attrs) {
        try {
            posicaoPatioService.deletarPorId(id);
            attrs.addFlashAttribute("successMessage", "Posição deletada com sucesso!");
        } catch (Exception e) {
            attrs.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/posicoes";
    }

    // Exibe o formulário para alocar uma moto em uma vaga específica
    @GetMapping("/{id}/ocupar")
    public String mostrarFormularioOcupar(@PathVariable Long id, Model model) {
        var posicao = posicaoPatioService.buscarPorId(id);
        model.addAttribute("posicao", posicao);
        // Busca apenas as motos da mesma filial que ainda não têm vaga
        model.addAttribute("motosDisponiveis", motoService.buscarMotosSemPosicao(posicao.getFilial().getId()));
        return "posicoes/ocupar-form";
    }

    // Processa a alocação de uma moto em uma vaga
    @PostMapping("/ocupar")
    public String ocuparPosicao(@RequestParam Long posicaoId, @RequestParam Long motoId, RedirectAttributes attrs) {
        posicaoPatioService.ocuparPosicao(posicaoId, motoId);
        // Pega o ID da filial para redirecionar de volta ao mapa correto
        var filialId = posicaoPatioService.buscarPorId(posicaoId).getFilial().getId();
        attrs.addFlashAttribute("successMessage", "Moto alocada com sucesso!");
        return "redirect:/filiais/" + filialId + "/patio";
    }

    // Libera uma moto de uma vaga
    @GetMapping("/{id}/liberar")
    public String liberarPosicao(@PathVariable Long id, RedirectAttributes attrs) {
        var filialId = posicaoPatioService.buscarPorId(id).getFilial().getId();
        posicaoPatioService.liberarPosicao(id);
        attrs.addFlashAttribute("successMessage", "Posição liberada com sucesso!");
        return "redirect:/filiais/" + filialId + "/patio";
    }

    // Rota de atalho para o Colaborador de Pátio ver o mapa de sua filial
    @GetMapping("/meu-patio")
    public String verMeuPatio(@AuthenticationPrincipal Usuario usuarioLogado, RedirectAttributes attrs) {
        if (usuarioLogado.getFilial() == null) {
            attrs.addFlashAttribute("errorMessage", "Você não está associado a nenhuma filial.");
            return "redirect:/";
        }
        Long filialId = usuarioLogado.getFilial().getId();
        // Redireciona para a URL do mapa do pátio, passando o ID da filial do usuário
        return "redirect:/filiais/" + filialId + "/patio";
    }
}