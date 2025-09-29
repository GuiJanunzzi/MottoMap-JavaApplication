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

// Controller para a página "Minha Conta" do usuário logado
@Controller
@RequestMapping("/minha-conta")
public class ContaController {
    private final UsuarioService usuarioService;

    public ContaController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Exibe o formulário para o usuário alterar sua senha
    @GetMapping
    public String mostrarFormulario(Model model) {
        model.addAttribute("passwordChangeDTO", new PasswordChangeDTO());
        return "conta/form";
    }

    // Processa a tentativa de alteração de senha vinda do formulário
    @PostMapping("/alterar-senha")
    public String alterarSenha(@Valid @ModelAttribute PasswordChangeDTO dto,
                               BindingResult result,
                               @AuthenticationPrincipal UserDetails userDetails,
                               RedirectAttributes attrs) {

        // Se houver erros de validação no DTO, volta para o formulário
        if (result.hasErrors()) {
            return "conta/form";
        }

        try {
            // Chama o serviço que contém a lógica de negócio para a troca de senha
            usuarioService.alterarSenha(userDetails.getUsername(), dto);
            attrs.addFlashAttribute("successMessage", "Senha alterada com sucesso! Por favor, faça o login novamente.");
        } catch (IllegalArgumentException e) {
            // Captura erros de negócio (ex: senha atual incorreta) e exibe ao usuário
            attrs.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/minha-conta"; // Volta para o formulário com o erro
        }

        // Força o logout para que o usuário precise entrar com a nova senha
        return "redirect:/login?logout";
    }
}