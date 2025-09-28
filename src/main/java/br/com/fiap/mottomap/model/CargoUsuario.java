package br.com.fiap.mottomap.model;

// Enum que define os diferentes cargos que um usuário pode ter no sistema.
public enum CargoUsuario {
    ADM_GERAL,      // Administrador geral, com acesso a todas as funcionalidades do sistema.
    ADM_LOCAL,      // Administrador de uma filial específica, com permissões limitadas à sua filial.
    COL_PATIO,      // Colaborador responsável pela organização e movimentação das motos no pátio.
    COL_MECANICO    // Mecânico responsável pela manutenção e resolução de problemas das motos.
}