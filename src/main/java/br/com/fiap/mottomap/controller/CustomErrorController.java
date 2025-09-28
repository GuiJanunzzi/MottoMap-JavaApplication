package br.com.fiap.mottomap.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// Controller para personalizar as páginas de erro da aplicação
@Controller
public class CustomErrorController implements ErrorController {

    // Mapeia todas as requisições que resultam em erro para este metodo
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        // Pega o código de status do erro (ex: 404, 403, 500) da requisição
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            // Verifica o código do erro e direciona para a página correspondente
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/404";
            }
            else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "error/403";
            }
            else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error/500";
            }
        }

        // Para qualquer outro erro não especificado, mostra uma página genérica
        return "error/error";
    }
}