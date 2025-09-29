package br.com.fiap.mottomap.controller;

import br.com.fiap.mottomap.model.Problema;
import br.com.fiap.mottomap.service.MotoService;
import br.com.fiap.mottomap.service.ProblemaService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    // Processa os dados recebidos do formulário para registrar um novo problema
    @PostMapping
    public String salvarProblema(@Valid @ModelAttribute("problema") Problema problema,
                                 BindingResult result,
                                 @RequestParam Long motoId,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        // Se a validação dos campos do formulário falhar, retorna para a mesma tela
        if (result.hasErrors()) {
            // Recarrega os dados da moto, necessários para renderizar o formulário novamente
            model.addAttribute("moto", motoService.buscarPorId(motoId));
            return "problemas/form";
        }

        // Se a validação passar, chama o serviço para executar a lógica de negócio
        problemaService.registrarNovoProblema(problema, motoId, userDetails);

        // Adiciona uma mensagem de sucesso para ser exibida na próxima página
        redirectAttributes.addFlashAttribute("successMessage", "Problema registrado com sucesso!");

        // Redireciona o usuário para a lista de motos
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

    // Ação para deletar um registro de problema
    @PreAuthorize("hasAnyAuthority('ADM_GERAL', 'ADM_LOCAL')")
    @GetMapping("/delete/{id}")
    public String deletarProblema(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // Pega o ID da moto ANTES de deletar para saber para onde voltar
        Long motoId = problemaService.buscarPorId(id).getMoto().getId();

        problemaService.deletar(id);

        redirectAttributes.addFlashAttribute("successMessage", "Problema excluído com sucesso!");
        return "redirect:/motos/" + motoId;
    }
}