package br.com.fiap.mottomap.controller;

import br.com.fiap.mottomap.repository.PosicaoPatioRepository;
import br.com.fiap.mottomap.service.MotoService;
import br.com.fiap.mottomap.service.PosicaoPatioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/posicoes")
public class PosicaoPatioController {

    private final PosicaoPatioService posicaoPatioService;
    private final PosicaoPatioRepository posicaoPatioRepository;
    private final MotoService motoService;

    public PosicaoPatioController(PosicaoPatioService posService, PosicaoPatioRepository posRepo, MotoService motoSvc) {
        this.posicaoPatioService = posService;
        this.posicaoPatioRepository = posRepo;
        this.motoService = motoSvc;
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
}
