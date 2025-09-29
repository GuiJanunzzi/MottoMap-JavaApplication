-- Inserindo uma filial padrão para o admin poder ser associado
INSERT INTO filial
(id, nome, endereco, cidade, sigla_estado, numero_coluna, numero_linha, capacidade_maxima) VALUES
    (1, 'Sede Principal', 'Av. Principal, 1', 'São Paulo', 'SP', 5, 10, 50)
    ON CONFLICT (id) DO NOTHING;

-- Inserindo o usuário ADM_GERAL (senha: "admin")
INSERT INTO usuario
(id, nome, username, password, cargo_usuario, filial_id, ativo) VALUES
    (1, 'Admin Geral', 'admin@mottomap.com', '$2a$10$xSAFeotUwNG7FQ555d.q1etGWtyyaiR.EebJ/YStDAZl1zA9Qbg4i', 'ADM_GERAL', 1, true)
    ON CONFLICT (id) DO NOTHING;

-- Recalculando as sequences para evitar conflitos
SELECT setval('filial_id_seq', (SELECT MAX(id) FROM filial));
SELECT setval('usuario_id_seq', (SELECT MAX(id) FROM usuario));