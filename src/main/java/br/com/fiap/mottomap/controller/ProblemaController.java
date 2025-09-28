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

// Controller para as ações relacionadas a Problemas de uma moto
@Controller
@RequestMapping("/problemas")
public class ProblemaController {

    private final ProblemaService problemaService;
    private final MotoService motoService;

    // Injeção de dependências
    public ProblemaController(ProblemaService problemaService, MotoService motoService) {
        this.problemaService = problemaService;
        this.motoService = motoService;
    }

    // Exibe o formulário para registrar um novo problema para uma moto específica
    @GetMapping("/new/{motoId}")
    public String mostrarFormulario(@PathVariable Long motoId, Model model) {
        model.addAttribute("problema", new Problema());
        model.addAttribute("moto", motoService.buscarPorId(motoId));
        return "problemas/form";
    }

    // Processa o formulário e salva o novo problema
    @PostMapping
    public String salvarProblema(@ModelAttribute Problema problema,
                                 @RequestParam Long motoId,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {

        // Delega a lógica de negócio para o serviço
        problemaService.registrarNovoProblema(problema, motoId, userDetails);

        redirectAttributes.addFlashAttribute("successMessage", "Problema registrado com sucesso!");
        // Redireciona de volta para a lista principal de motos
        return "redirect:/motos";
    }

    // Ação para o mecânico marcar um problema como resolvido
    @PreAuthorize("hasAuthority('COL_MECANICO')")
    @GetMapping("/resolver/{id}")
    public String marcarComoResolvido(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        problemaService.marcarComoResolvido(id);

        // Pega o ID da moto para redirecionar de volta para a página de detalhes correta
        Long motoId = problemaService.buscarPorId(id).getMoto().getId();

        redirectAttributes.addFlashAttribute("successMessage", "Problema marcado como resolvido!");
        return "redirect:/motos/" + motoId;
    }

    // Ação para o ADM_LOCAL deletar um registro de problema
    // Uma melhoria futura poderia ser permitir que o ADM_GERAL também delete
    @PreAuthorize("hasAuthority('ADM_LOCAL')")
    @GetMapping("/delete/{id}")
    public String deletarProblema(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // Pega o ID da moto ANTES de deletar para saber para onde voltar
        Long motoId = problemaService.buscarPorId(id).getMoto().getId();

        problemaService.deletar(id);

        redirectAttributes.addFlashAttribute("successMessage", "Problema excluído com sucesso!");
        return "redirect:/motos/" + motoId;
    }
}