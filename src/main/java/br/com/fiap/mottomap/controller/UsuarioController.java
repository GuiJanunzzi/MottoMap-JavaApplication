package br.com.fiap.mottomap.controller;

import br.com.fiap.mottomap.dto.UsuarioDTO; // Importar DTO
import br.com.fiap.mottomap.model.Usuario;
import br.com.fiap.mottomap.repository.FilialRepository;
import br.com.fiap.mottomap.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
@PreAuthorize("hasAuthority('ADM_GERAL')")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final FilialRepository filialRepository;

    public UsuarioController(UsuarioService usuarioService, FilialRepository filialRepository) {
        this.usuarioService = usuarioService;
        this.filialRepository = filialRepository;
    }

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.buscarTodos());
        return "usuarios/list";
    }

    @GetMapping("/new")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioDTO()); // Usa o DTO
        model.addAttribute("filiais", filialRepository.findAll());
        return "usuarios/form";
    }

    @PostMapping
    public String salvarUsuario(@Valid @ModelAttribute("usuarioDTO") UsuarioDTO usuarioDTO, BindingResult result, Model model, RedirectAttributes attrs) {
        if (result.hasErrors()) {
            model.addAttribute("filiais", filialRepository.findAll());
            return "usuarios/form";
        }
        usuarioService.salvar(usuarioDTO);
        attrs.addFlashAttribute("successMessage", "Usuário salvo com sucesso!");
        return "redirect:/usuarios";
    }

    @GetMapping("/edit/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {
        // Busca a entidade e converte para DTO antes de enviar para a tela
        Usuario usuario = usuarioService.buscarPorId(id);
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setUsername(usuario.getUsername());
        dto.setCargoUsuario(usuario.getCargoUsuario());
        dto.setFilialId(usuario.getFilial().getId());
        dto.setAtivo(usuario.isAtivo());

        model.addAttribute("usuarioDTO", dto);
        model.addAttribute("filiais", filialRepository.findAll());
        return "usuarios/form";
    }

    @GetMapping("/deactivate/{id}")
    public String desativarUsuario(@PathVariable Long id, RedirectAttributes attrs) {
        usuarioService.desativar(id);
        attrs.addFlashAttribute("successMessage", "Usuário desativado com sucesso!");
        return "redirect:/usuarios";
    }

    @GetMapping("/activate/{id}")
    public String ativarUsuario(@PathVariable Long id, RedirectAttributes attrs) {
        usuarioService.ativar(id);
        attrs.addFlashAttribute("successMessage", "Usuário ativado com sucesso!");
        return "redirect:/usuarios";
    }
}
