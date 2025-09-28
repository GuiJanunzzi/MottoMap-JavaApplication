package br.com.fiap.mottomap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Controller responsável apenas por exibir a página de login
@Controller
public class LoginController {

    // Quando o usuário acessa a URL /login via GET, este metodo é chamado
    @GetMapping("/login")
    public String login() {
        // Retorna o nome do arquivo HTML (login.html) que deve ser renderizado
        return "login";
    }
}