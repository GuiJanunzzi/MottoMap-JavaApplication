package br.com.fiap.mottomap.controller;

import br.com.fiap.mottomap.repository.PosicaoPatioRepository;
import br.com.fiap.mottomap.service.MotoService;
import br.com.fiap.mottomap.service.PatioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/posicoes")
public class PosicaoPatioController {

    private final PatioService patioService;
    private final PosicaoPatioRepository posicaoPatioRepository;
    private final MotoService motoService;

    public PosicaoPatioController(PatioService posService, PosicaoPatioRepository posRepo, MotoService motoSvc) {
        this.patioService = posService;
        this.posicaoPatioRepository = posRepo;
        this.motoService = motoSvc;
    }

    @GetMapping("/{id}/ocupar")
    public String mostrarFormularioOcupar(@PathVariable Long id, Model model) {
        var posicao = posicaoPatioRepository.findById(id).orElseThrow();
        var motosDisponiveis = motoService.buscarMotosSemPosicao(posicao.getFilial().getId());

        model.addAttribute("posicao", posicao);
        model.addAttribute("motosDisponiveis", motosDisponiveis);
        return "posicoes/ocupar-form";
    }

    @PostMapping("/ocupar")
    public String ocuparPosicao(@RequestParam Long posicaoId, @RequestParam Long motoId, RedirectAttributes redirectAttributes) {
        patioService.ocuparPosicao(posicaoId, motoId);
        var filialId = posicaoPatioRepository.findById(posicaoId).get().getFilial().getId();
        redirectAttributes.addFlashAttribute("successMessage", "Moto alocada com sucesso!");
        return "redirect:/filiais/" + filialId + "/patio";
    }
}
