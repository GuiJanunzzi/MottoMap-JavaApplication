
INSERT INTO filial
    (nome, endereco, cidade, sigla_estado, numero_coluna, numero_linha, capacidade_maxima) VALUES
    ('Sede Principal', 'Av. Faria Lima, 123', 'São Paulo', 'SP', 20, 10, 200);

INSERT INTO usuario
    (nome, username, password, cargo_usuario, filial_id) VALUES
    ('Admin Geral', 'admin@mottomap.com', 'admin', 'ADM_GERAL', 1);