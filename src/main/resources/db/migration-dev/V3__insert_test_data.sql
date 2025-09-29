-- Inserindo uma filial de teste adicional
INSERT INTO filial
(nome, endereco, cidade, sigla_estado, numero_coluna, numero_linha, capacidade_maxima) VALUES
('Filial Rio de Janeiro', 'Av. Copacabana, 456', 'Rio de Janeiro', 'RJ', 8, 4, 32);

-- Insere outros usuários de teste
INSERT INTO usuario
(nome, username, password, cargo_usuario, filial_id, ativo) VALUES
('Admin Local SP', 'local@mottomap.com', '$2a$10$Ttw9WKwx/edi2vo1A5dNr.3.Q5q.biyEKMUsKdLLnY2mtk7.NlxbS', 'ADM_LOCAL', 1, true), --Senha: local123
('Colaborador Pátio SP', 'patio@mottomap.com', '$2a$10$oxQHbr0CRyx6hQI4OKC8JOjIdnkUMizW2JbH0K93HH0mAvBCdp.Ba', 'COL_PATIO', 1, true), --Senha: patio123
('Mecânico Chefe SP', 'mecanico@mottomap.com', '$2a$10$HxEyBnJd7gBazPRf3tc4k.DpS.An2Z5rswqntT/mr9PICl2Mwl2wm', 'COL_MECANICO', 1, true); --Senha: mec123

-- Insere motos de teste
INSERT INTO moto
(placa, chassi, modelo_moto, ano, status_moto, filial_id) VALUES
('BRA2E19', '9C2JC4110GR111111', 'MOTTU_SPORT', 2024, 'ATIVA', 1),
 ('PIN1A12', '9C2JC4110GR222222', 'POP_110I', 2023, 'ATIVA', 1),
('FIX0A00', '9C2JC4110GR444444', 'MOTTU_SPORT_ESD', 2024, 'ATIVA', 1),
('BUG4U44', '9C2JC4110GR555555', 'POP_110I', 2022, 'ATIVA', 1);

-- Insere posições de pátio para a Sede Principal
INSERT INTO posicao_patio (identificacao, numero_linha, numero_coluna, area, ocupado, filial_id) VALUES
('A1', 1, 1, 'PRONTAS', false, 1), ('A2', 1, 2, 'PRONTAS', false, 1), ('A3', 1, 3, 'PRONTAS', false, 1),
('B1', 2, 1, 'MINHA_MOTTU', false, 1), ('B2', 2, 2, 'MINHA_MOTTU', false, 1),
('C1', 3, 1, 'PROBLEMAS_SIMPLES', false, 1), ('C2', 3, 2, 'PROBLEMAS_GRAVES', false, 1),
('D1', 4, 1, 'IRRECUPERAVEIS', false, 1);