package br.com.fiap.mottomap.controller;

import br.com.fiap.mottomap.model.Moto;
import br.com.fiap.mottomap.repository.FilialRepository;
import br.com.fiap.mottomap.repository.MotoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/motos")
public class MotoController {

    @Autowired
    private MotoRepository motoRepository;

    @Autowired
    private FilialRepository filialRepository;

    //Metodo para listar as motos
    @GetMapping
    public String listarMotos(Model model) {
        model.addAttribute("motos", motoRepository.findAll());
        return "motos/list"; // -> Aponta para o arquivo /templates/motos/list.html
    }

    //Metodo para cadastrar nova moto
    @GetMapping("/new")
    public String mostrarFormulario(Model model) {
        model.addAttribute("moto", new Moto());
        model.addAttribute("filiais", filialRepository.findAll());
        return "motos/form"; // -> Aponta para o arquivo /templates/motos/form.html
    }

    //Metodo para salvar (tanto nova quanto editada)
    @PostMapping
    public String salvarMoto(@Valid @ModelAttribute("moto") Moto moto, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            //Se tiver erros de validação, volta para o formulario
            return "motos/form";
        }
        motoRepository.save(moto);
        redirectAttributes.addFlashAttribute("successMessage", "Moto salva com sucesso!");
        return "redirect:/motos";
    }

    // Metodo para o formulario de edição
    @GetMapping("/edit/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {
        model.addAttribute("moto", motoRepository.findById(id).orElseThrow());
        model.addAttribute("filiais", filialRepository.findAll());
        return "motos/form";
    }

    // Metodo para deletar uma moto
    @GetMapping("/delete/{id}")
    public String deletarMoto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        motoRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Moto deletada com sucesso!");
        return "redirect:/motos";
    }
}