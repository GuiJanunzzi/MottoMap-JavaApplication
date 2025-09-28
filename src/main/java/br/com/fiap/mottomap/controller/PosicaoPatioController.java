package br.com.fiap.mottomap.controller;

import br.com.fiap.mottomap.model.PosicaoPatio;
import br.com.fiap.mottomap.model.Usuario;
import br.com.fiap.mottomap.repository.FilialRepository;
import br.com.fiap.mottomap.repository.PosicaoPatioRepository;
import br.com.fiap.mottomap.service.MotoService;
import br.com.fiap.mottomap.service.PosicaoPatioService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/posicoes")
public class PosicaoPatioController {

    private static final Logger log = LoggerFactory.getLogger(PosicaoPatioController.class);

    private final PosicaoPatioService posicaoPatioService;
    private final PosicaoPatioRepository posicaoPatioRepository;
    private final MotoService motoService;
    private final FilialRepository filialRepository;

    public PosicaoPatioController(PosicaoPatioService posService, PosicaoPatioRepository posRepo, MotoService motoSvc, FilialRepository filialRepository) {
        this.posicaoPatioService = posService;
        this.posicaoPatioRepository = posRepo;
        this.motoService = motoSvc;
        this.filialRepository = filialRepository;
    }

    @GetMapping
    public String listarPosicoes(Model model) {
        model.addAttribute("posicoes", posicaoPatioService.buscarTodas());
        return "posicoes/list";
    }

    @GetMapping("/new")
    public String mostrarFormularioCadastro(Model model, @AuthenticationPrincipal Usuario usuarioLogado) {
        var posicao = new PosicaoPatio();

        // Se o usuário for ADM_LOCAL, já pré-definimos a filial dele
        if (usuarioLogado.getCargoUsuario().name().equals("ADM_LOCAL")) {
            posicao.setFilial(usuarioLogado.getFilial());
        } else {
            // Se for ADM_GERAL, enviamos a lista de todas as filiais para ele escolher
            model.addAttribute("filiais", filialRepository.findAll());
        }

        model.addAttribute("posicaoPatio", posicao);
        return "posicoes/form";
    }

    @PostMapping
    public String salvarPosicao(@Valid @ModelAttribute("posicaoPatio") PosicaoPatio posicaoPatio,
                                BindingResult result,
                                Model model,
                                RedirectAttributes attrs,
                                @AuthenticationPrincipal Usuario usuarioLogado) {

        // Validação extra de segurança: ADM_LOCAL só pode cadastrar na própria filial
        if (usuarioLogado.getCargoUsuario().name().equals("ADM_LOCAL")) {
            if (posicaoPatio.getFilial().getId() != usuarioLogado.getFilial().getId()) {
                throw new SecurityException("Acesso negado: tentativa de cadastro em filial não permitida.");
            }
        }

        if (result.hasErrors()) {
            // Se der erro e for ADM_GERAL, recarregar a lista de filiais
            if (usuarioLogado.getCargoUsuario().name().equals("ADM_GERAL")) {
                model.addAttribute("filiais", filialRepository.findAll());
            }
            return "posicoes/form";
        }

        posicaoPatioService.salvar(posicaoPatio);
        attrs.addFlashAttribute("successMessage", "Posição salva com sucesso!");
        return "redirect:/posicoes";
    }

    @GetMapping("/edit/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {
        model.addAttribute("posicaoPatio", posicaoPatioService.buscarPorId(id));
        model.addAttribute("filiais", filialRepository.findAll());
        return "posicoes/form";
    }

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

    @GetMapping("/{id}/ocupar")
    public String mostrarFormularioOcupar(@PathVariable Long id, Model model) {
        var posicao = posicaoPatioRepository.findById(id).orElseThrow();
        model.addAttribute("posicao", posicao);
        model.addAttribute("motosDisponiveis", motoService.buscarMotosSemPosicao(posicao.getFilial().getId()));
        return "posicoes/ocupar-form";
    }

    @PostMapping("/ocupar")
    public String ocuparPosicao(@RequestParam Long posicaoId, @RequestParam Long motoId, RedirectAttributes attrs) {
        posicaoPatioService.ocuparPosicao(posicaoId, motoId);
        var filialId = posicaoPatioRepository.findById(posicaoId).get().getFilial().getId();
        attrs.addFlashAttribute("successMessage", "Moto alocada com sucesso!");
        return "redirect:/filiais/" + filialId + "/patio";
    }

    @GetMapping("/{id}/liberar")
    public String liberarPosicao(@PathVariable Long id, RedirectAttributes attrs) {
        var filialId = posicaoPatioRepository.findById(id).get().getFilial().getId();
        posicaoPatioService.liberarPosicao(id);
        attrs.addFlashAttribute("successMessage", "Posição liberada com sucesso!");
        return "redirect:/filiais/" + filialId + "/patio";
    }

    @GetMapping("/meu-patio")
    public String verMeuPatio(@AuthenticationPrincipal Usuario usuarioLogado, RedirectAttributes attrs) {
        if (usuarioLogado.getFilial() == null) {
            attrs.addFlashAttribute("errorMessage", "Você não está associado a nenhuma filial.");
            return "redirect:/";
        }
        Long filialId = usuarioLogado.getFilial().getId();
        return "redirect:/filiais/" + filialId + "/patio";
    }

}
