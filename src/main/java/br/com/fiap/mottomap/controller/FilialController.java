package br.com.fiap.mottomap.controller;

import br.com.fiap.mottomap.model.Filial;
import br.com.fiap.mottomap.service.FilialService;
import br.com.fiap.mottomap.service.PatioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Controller para gerenciar as operações de CRUD da entidade Filial
@Controller
@RequestMapping("/filiais")
public class FilialController {

    private final FilialService filialService;
    private final PatioService patioService;

    // Injeção de dependências dos serviços necessários
    public FilialController(FilialService filialService, PatioService patioService) {
        this.filialService = filialService;
        this.patioService = patioService;
    }

    // Metodo para exibir a lista de todas as filiais
    @GetMapping
    public String listarFiliais(Model model) {
        model.addAttribute("filiais", filialService.buscarTodas());
        return "filiais/list";
    }

    // Metodo para exibir o formulario de cadastro de uma nova filial
    @GetMapping("/new")
    public String mostrarFormulario(Model model) {
        model.addAttribute("filial", new Filial());
        return "filiais/form";
    }

    // Metodo para processar o salvamento de uma nova filial ou a atualização de uma existente
    @PostMapping
    public String salvarFilial(@Valid @ModelAttribute Filial filial, BindingResult result, RedirectAttributes redirectAttributes) {
        // Se houver erros de validação, retorna para o formulário
        if (result.hasErrors()) {
            return "filiais/form";
        }
        // Chama o serviço para salvar os dados
        filialService.salvar(filial);
        redirectAttributes.addFlashAttribute("successMessage", "Filial salva com sucesso!");
        return "redirect:/filiais";
    }

    // Metodo para exibir o formulario de edição de uma filial
    @GetMapping("/edit/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {
        model.addAttribute("filial", filialService.buscarPorId(id));
        return "filiais/form";
    }

    // Metodo para deletar uma filial
    @GetMapping("/delete/{id}")
    public String deletarFilial(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Tenta deletar a filial através do serviço
            filialService.deletarPorId(id);
            redirectAttributes.addFlashAttribute("successMessage", "Filial deletada com sucesso!");
        } catch (RuntimeException e) {
            // Se o serviço lançar um erro (ex: filial com usuários), captura e exibe na tela
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/filiais";
    }

    // Metodo para exibir o mapa visual do pátio de uma filial especifica
    @GetMapping("/{id}/patio")
    public String verPatio(@PathVariable Long id, Model model) {
        Filial filial = filialService.buscarPorId(id);
        model.addAttribute("filial", filial);
        // Chama o serviço para montar a grade de posições do pátio
        model.addAttribute("gradePatio", patioService.montarGradePatio(filial));
        return "filiais/patio";
    }
}