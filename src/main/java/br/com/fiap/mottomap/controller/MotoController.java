package br.com.fiap.mottomap.controller;

import br.com.fiap.mottomap.model.Moto;
import br.com.fiap.mottomap.model.Usuario;
import br.com.fiap.mottomap.repository.FilialRepository;
import br.com.fiap.mottomap.service.MotoService; // Importe o service
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

import java.util.List;

@Controller
@RequestMapping("/motos")
public class MotoController {

    private final MotoService motoService;
    private final FilialRepository filialRepository;
    private final ProblemaService problemaService;
    private final PosicaoPatioService posicaoPatioService;

    public MotoController(MotoService motoService, FilialRepository filialRepository, ProblemaService problemaService, PosicaoPatioService posicaoPatioService) {
        this.motoService = motoService;
        this.filialRepository = filialRepository;
        this.problemaService = problemaService;
        this.posicaoPatioService = posicaoPatioService;
    }

    @GetMapping
    public String listarMotos(Model model) {
        model.addAttribute("motos", motoService.buscarTodas());
        return "motos/list";
    }

    @GetMapping("/new")
    public String mostrarFormulario(Model model) {
        model.addAttribute("moto", new Moto());
        model.addAttribute("filiais", filialRepository.findAll());
        return "motos/form";
    }

    @PostMapping
    public String salvarMoto(@Valid @ModelAttribute("moto") Moto moto, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("filiais", filialRepository.findAll());
            return "motos/form";
        }
        motoService.salvar(moto);
        redirectAttributes.addFlashAttribute("successMessage", "Moto salva com sucesso!");
        return "redirect:/motos";
    }

    @GetMapping("/edit/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {
        model.addAttribute("moto", motoService.buscarPorId(id));
        model.addAttribute("filiais", filialRepository.findAll());
        return "motos/form";
    }

    @GetMapping("/delete/{id}")
    public String deletarMoto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        motoService.deletarPorId(id);
        redirectAttributes.addFlashAttribute("successMessage", "Moto deletada com sucesso!");
        return "redirect:/motos";
    }

    @GetMapping("/{id}")
    public String verDetalhesDaMoto(@PathVariable Long id, Model model) {
        model.addAttribute("moto", motoService.buscarPorId(id));
        model.addAttribute("problemas", problemaService.buscarPorMotoId(id));

        posicaoPatioService.buscarPorMotoId(id).ifPresent(posicao -> {
            model.addAttribute("posicaoPatio", posicao);
        });

        return "motos/details";
    }

    @GetMapping("/pendentes")
    @PreAuthorize("hasAuthority('COL_MECANICO')")
    public String listarMotosPendentes(Model model, @AuthenticationPrincipal Usuario usuarioLogado) {
        if (usuarioLogado.getFilial() == null) {
            return "redirect:/?error=no_filial";
        }

        Long filialId = usuarioLogado.getFilial().getId();
        List<Moto> motosPendentes = motoService.buscarMotosComProblemasNaoResolvidos(filialId);

        model.addAttribute("motos", motosPendentes);
        return "motos/pendentes";
    }

    @GetMapping("/sem-posicao")
    @PreAuthorize("hasAnyAuthority('COL_PATIO', 'ADM_GERAL', 'ADM_LOCAL')") // Permissão para pátio e admins
    public String listarMotosSemPosicao(Model model, @AuthenticationPrincipal Usuario usuarioLogado) {
        if (usuarioLogado.getFilial() == null) {
            return "redirect:/?error=no_filial";
        }

        Long filialId = usuarioLogado.getFilial().getId();
        List<Moto> motosSemPosicao = motoService.buscarMotosSemPosicao(filialId);

        model.addAttribute("motos", motosSemPosicao);
        model.addAttribute("filialId", filialId);
        return "motos/sem-posicao";
    }
}