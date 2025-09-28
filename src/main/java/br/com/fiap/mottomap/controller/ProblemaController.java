package br.com.fiap.mottomap.controller;

import br.com.fiap.mottomap.model.Problema;
import br.com.fiap.mottomap.service.MotoService;
import br.com.fiap.mottomap.service.ProblemaService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/problemas")
public class ProblemaController {

    private final ProblemaService problemaService;
    private final MotoService motoService;

    public ProblemaController(ProblemaService problemaService, MotoService motoService) {
        this.problemaService = problemaService;
        this.motoService = motoService;
    }

    @GetMapping("/new/{motoId}")
    public String mostrarFormulario(@PathVariable Long motoId, Model model) {
        model.addAttribute("problema", new Problema());
        model.addAttribute("moto", motoService.buscarPorId(motoId));
        return "problemas/form";
    }

    @PostMapping
    public String salvarProblema(@ModelAttribute Problema problema,
                                 @RequestParam Long motoId,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {

        problemaService.registrarNovoProblema(problema, motoId, userDetails);

        redirectAttributes.addFlashAttribute("successMessage", "Problema registrado com sucesso!");
        return "redirect:/motos";
    }

    @PreAuthorize("hasAuthority('COL_MECANICO')")
    @GetMapping("/resolver/{id}")
    public String marcarComoResolvido(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        problemaService.marcarComoResolvido(id);

        Long motoId = problemaService.buscarPorId(id).getMoto().getId();

        redirectAttributes.addFlashAttribute("successMessage", "Problema marcado como resolvido!");
        return "redirect:/motos/" + motoId;
    }

    @PreAuthorize("hasAuthority('ADM_LOCAL')")
    @GetMapping("/delete/{id}")
    public String deletarProblema(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Long motoId = problemaService.buscarPorId(id).getMoto().getId();

        problemaService.deletar(id);

        redirectAttributes.addFlashAttribute("successMessage", "Problema excluído com sucesso!");
        return "redirect:/motos/" + motoId;
    }
}
