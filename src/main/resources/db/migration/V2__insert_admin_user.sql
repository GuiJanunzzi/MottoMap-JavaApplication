-- Habilita a inserção explícita de ID (PK) para garantir que o ID=1
SET IDENTITY_INSERT filial ON;
INSERT INTO filial
(id, nome, endereco, cidade, sigla_estado, numero_coluna, numero_linha, capacidade_maxima) VALUES
    (1, 'Sede Principal', 'Av. Principal, 1', 'São Paulo', 'SP', 5, 10, 50);
SET IDENTITY_INSERT filial OFF;

-- Habilita a inserção explícita de ID (PK) para o usuário admin
SET IDENTITY_INSERT usuario ON;
INSERT INTO usuario
(id, nome, username, password, cargo_usuario, filial_id, ativo) VALUES
    (1, 'Admin Geral', 'admin@mottomap.com', '$2a$10$xSAFeotUwNG7FQ555d.q1etGWtyyaiR.EebJ/YStDAZl1zA9Qbg4i', 'ADM_GERAL', 1, 1);
SET IDENTITY_INSERT usuario OFF;