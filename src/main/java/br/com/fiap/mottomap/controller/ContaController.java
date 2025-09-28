package br.com.fiap.mottomap.controller;

import br.com.fiap.mottomap.dto.PasswordChangeDTO;
import br.com.fiap.mottomap.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/minha-conta")
public class ContaController {
    private final UsuarioService usuarioService;

    public ContaController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String mostrarFormulario(Model model) {
        model.addAttribute("passwordChangeDTO", new PasswordChangeDTO());
        return "conta/form";
    }

    @PostMapping("/alterar-senha")
    public String alterarSenha(@Valid @ModelAttribute PasswordChangeDTO dto,
                               BindingResult result,
                               @AuthenticationPrincipal UserDetails userDetails,
                               RedirectAttributes attrs) {
        if (result.hasErrors()) {
            return "conta/form";
        }

        try {
            usuarioService.alterarSenha(userDetails.getUsername(), dto);
            attrs.addFlashAttribute("successMessage", "Senha alterada com sucesso!");
        } catch (IllegalArgumentException e) {
            attrs.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/login?logout";
    }
}
