package br.com.fiap.mottomap.model;

// Enum que representa as diferentes áreas físicas de um pátio.
public enum Area {
    PRONTAS,            // Motos prontas para aluguel ou uso
    MINHA_MOTTU,        // Área destinada a motos do plano minha mottu
    PROBLEMAS_SIMPLEs, // Motos com defeitos de fácil resolução
    PROBLEMAS_GRAVES,   // Motos que necessitam de manutenção complexa
    IRRECUPERAVEIS      // Motos que foram baixadas ou não têm conserto
}