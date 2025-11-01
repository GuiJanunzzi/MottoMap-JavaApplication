-- Insere problemas de teste
INSERT INTO problema
(tipo_problema, descricao, data_registro, resolvido, moto_id, usuario_id) VALUES
                                                                              ('ELETRICO', 'Farol dianteiro não acende, possível problema no chicote.', '2025-09-27', 0, (SELECT id FROM moto WHERE placa = 'BUG4U44'), (SELECT id FROM usuario WHERE username = 'patio@mottomap.com')),
                                                                              ('MECANICO', 'Vazamento de óleo no motor, junta trocada.', '2025-09-20', 1, (SELECT id FROM moto WHERE placa = 'FIX0A00'), (SELECT id FROM usuario WHERE username = 'mecanico@mottomap.com'));

-- Aloca motos em posições para popular o mapa
UPDATE posicao_patio SET ocupado = 1, moto_id = (SELECT id FROM moto WHERE placa = 'BRA2E19') WHERE identificacao = 'A1' AND filial_id = 1;
UPDATE posicao_patio SET ocupado = 1, moto_id = (SELECT id FROM moto WHERE placa = 'FIX0A00') WHERE identificacao = 'A2' AND filial_id = 1;
UPDATE posicao_patio SET ocupado = 1, moto_id = (SELECT id FROM moto WHERE placa = 'BUG4U44') WHERE identificacao = 'C1' AND filial_id = 1;