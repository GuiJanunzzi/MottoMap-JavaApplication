package br.com.fiap.mottomap.controller;

import br.com.fiap.mottomap.dto.UsuarioDTO;
import br.com.fiap.mottomap.model.Usuario;
import br.com.fiap.mottomap.service.FilialService;
import br.com.fiap.mottomap.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Controller para o CRUD de Usuários, acessível apenas pelo ADM_GERAL
@Controller
@RequestMapping("/usuarios")
@PreAuthorize("hasAuthority('ADM_GERAL')") // Protege toda a classe
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final FilialService filialService;

    // Injeção de dependências dos serviços
    public UsuarioController(UsuarioService usuarioService, FilialService filialService) {
        this.usuarioService = usuarioService;
        this.filialService = filialService;
    }

    // Exibe a lista de todos os usuários do sistema
    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.buscarTodos());
        return "usuarios/list";
    }

    // Exibe o formulário para cadastrar um novo usuário
    @GetMapping("/new")
    public String mostrarFormularioCadastro(Model model) {
        // Envia um DTO vazio para o formulário ser preenchido
        model.addAttribute("usuarioDTO", new UsuarioDTO());
        // Envia a lista de filiais para o dropdown de seleção
        model.addAttribute("filiais", filialService.buscarTodas());
        return "usuarios/form";
    }

    // Processa os dados do formulário para salvar um novo usuário ou uma edição
    @PostMapping
    public String salvarUsuario(@Valid @ModelAttribute("usuarioDTO") UsuarioDTO usuarioDTO,
                                BindingResult result,
                                Model model,
                                RedirectAttributes attrs) {
        if (result.hasErrors()) {
            // Se a validação do DTO falhar, recarrega a lista de filiais e retorna ao formulário
            model.addAttribute("filiais", filialService.buscarTodas());
            return "usuarios/form";
        }

        try {
            // Chama o serviço para salvar o usuário (a lógica de senha e mapeamento está no serviço)
            usuarioService.salvar(usuarioDTO);
            attrs.addFlashAttribute("successMessage", "Usuário salvo com sucesso!");

        } catch (IllegalArgumentException e) {
            // Captura o erro "senha obrigatória" do serviço
            // e o adiciona ao BindingResult para ser exibido no formulário.
            result.rejectValue("password", "password.required", e.getMessage());
            model.addAttribute("filiais", filialService.buscarTodas());
            return "usuarios/form";
        }

        return "redirect:/usuarios";
    }

    // Exibe o formulário de edição para um usuário específico
    @GetMapping("/edit/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {
        // Busca a entidade Usuario do banco
        Usuario usuario = usuarioService.buscarPorId(id);

        // Converte a entidade para um DTO para ser enviado à view
        // Isso evita expor a senha e facilita o manuseio no formulário
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setUsername(usuario.getUsername());
        dto.setCargoUsuario(usuario.getCargoUsuario());
        dto.setFilialId(usuario.getFilial().getId());
        dto.setAtivo(usuario.isAtivo());

        model.addAttribute("usuarioDTO", dto);
        model.addAttribute("filiais", filialService.buscarTodas());
        return "usuarios/form";
    }

    // Ação para desativar um usuário
    @GetMapping("/deactivate/{id}")
    public String desativarUsuario(@PathVariable Long id, RedirectAttributes attrs) {
        usuarioService.desativar(id);
        attrs.addFlashAttribute("successMessage", "Usuário desativado com sucesso!");
        return "redirect:/usuarios";
    }

    // Ação para reativar um usuário
    @GetMapping("/activate/{id}")
    public String ativarUsuario(@PathVariable Long id, RedirectAttributes attrs) {
        usuarioService.ativar(id);
        attrs.addFlashAttribute("successMessage", "Usuário ativado com sucesso!");
        return "redirect:/usuarios";
    }
}