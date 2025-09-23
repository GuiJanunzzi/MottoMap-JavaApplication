
INSERT INTO filial
    (nome, endereco, cidade, sigla_estado, numero_coluna, numero_linha, capacidade_maxima) VALUES
    ('Sede Principal', 'Av. Faria Lima, 123', 'São Paulo', 'SP', 20, 10, 200);

INSERT INTO usuario
    (nome, username, password, cargo_usuario, filial_id) VALUES
    ('Admin Geral', 'admin@mottomap.com', '$2a$10$xSAFeotUwNG7FQ555d.q1etGWtyyaiR.EebJ/YStDAZl1zA9Qbg4i', 'ADM_GERAL', 1),
    ('Admin Local', 'local@mottomap.com', '$2a$10$Ttw9WKwx/edi2vo1A5dNr.3.Q5q.biyEKMUsKdLLnY2mtk7.NlxbS', 'ADM_LOCAL', 1),
    ('Colaborador Patio', 'patio@mottomap.com', '$2a$10$oxQHbr0CRyx6hQI4OKC8JOjIdnkUMizW2JbH0K93HH0mAvBCdp.Ba', 'COL_PATIO', 1),
    ('Colaborador Mecanico', 'mecanico@mottomap.com', '$2a$10$HxEyBnJd7gBazPRf3tc4k.DpS.An2Z5rswqntT/mr9PICl2Mwl2wm', 'COL_MECANICO', 1);