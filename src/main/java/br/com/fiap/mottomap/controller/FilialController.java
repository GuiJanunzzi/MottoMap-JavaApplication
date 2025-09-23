package br.com.fiap.mottomap.controller;

import br.com.fiap.mottomap.model.Filial;
import br.com.fiap.mottomap.service.FilialService; // Importe o service
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/filiais")
public class FilialController {

    private final FilialService filialService;

    public FilialController(FilialService filialService) {
        this.filialService = filialService;
    }

    @GetMapping
    public String listarFiliais(Model model) {
        model.addAttribute("filiais", filialService.buscarTodas()); // Usa o service
        return "filiais/list";
    }

    @GetMapping("/new")
    public String mostrarFormulario(Model model) {
        model.addAttribute("filial", new Filial());
        return "filiais/form";
    }

    @PostMapping
    public String salvarFilial(@Valid @ModelAttribute Filial filial, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "filiais/form";
        }
        filialService.salvar(filial); // Usa o service
        redirectAttributes.addFlashAttribute("successMessage", "Filial salva com sucesso!");
        return "redirect:/filiais";
    }

    @GetMapping("/edit/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {
        model.addAttribute("filial", filialService.buscarPorId(id)); // Usa o service
        return "filiais/form";
    }

    @GetMapping("/delete/{id}")
    public String deletarFilial(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            filialService.deletarPorId(id);
            redirectAttributes.addFlashAttribute("successMessage", "Filial deletada com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/filiais";
    }
}