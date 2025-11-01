-- Cria a tabela de filiais
CREATE TABLE filial (
                        id BIGINT IDENTITY(1,1) NOT NULL,
                        nome VARCHAR(100) NOT NULL,
                        endereco VARCHAR(200) NOT NULL,
                        cidade VARCHAR(50) NOT NULL,
                        sigla_estado VARCHAR(2) NOT NULL,
                        numero_coluna INT NOT NULL,
                        numero_linha INT NOT NULL,
                        capacidade_maxima INT NOT NULL,
                        CONSTRAINT filial_pkey PRIMARY KEY (id)
);

-- Cria a tabela de usuarios
CREATE TABLE usuario (
                         id BIGINT IDENTITY(1,1) NOT NULL,
                         nome VARCHAR(150) NOT NULL,
                         username VARCHAR(255) NOT NULL,
                         password VARCHAR(255) NOT NULL,
                         cargo_usuario VARCHAR(50) NOT NULL,
                         ativo BIT NOT NULL DEFAULT 1,
                         filial_id BIGINT,
                         CONSTRAINT usuario_pkey PRIMARY KEY (id),
                         CONSTRAINT usuario_username_key UNIQUE (username),
                         FOREIGN KEY (filial_id) REFERENCES filial(id)
);

-- Cria a tabela de motos
CREATE TABLE moto (
                      id BIGINT IDENTITY(1,1) NOT NULL,
                      placa VARCHAR(7) NOT NULL,
                      chassi VARCHAR(17) NOT NULL,
                      modelo_moto VARCHAR(50) NOT NULL,
                      ano INT NOT NULL,
                      status_moto VARCHAR(50) NOT NULL,
                      filial_id BIGINT NOT NULL,
                      CONSTRAINT moto_pkey PRIMARY KEY (id),
                      CONSTRAINT moto_placa_key UNIQUE (placa),
                      CONSTRAINT moto_chassi_key UNIQUE (chassi),
                      FOREIGN KEY (filial_id) REFERENCES filial(id)
);

-- Cria a tabela de posicoes do patio
CREATE TABLE posicao_patio (
                               id BIGINT IDENTITY(1,1) NOT NULL,
                               identificacao VARCHAR(255) NOT NULL,
                               numero_linha INT NOT NULL,
                               numero_coluna INT NOT NULL,
                               area VARCHAR(50) NOT NULL,
                               ocupado BIT NOT NULL,
                               moto_id BIGINT,
                               filial_id BIGINT NOT NULL,
                               CONSTRAINT posicao_patio_pkey PRIMARY KEY (id),
                               CONSTRAINT fk_posicao_patio_filial FOREIGN KEY (filial_id) REFERENCES filial(id),
                               CONSTRAINT fk_posicao_patio_moto FOREIGN KEY (moto_id) REFERENCES moto(id)
);

-- Cria a tabela de problemas
CREATE TABLE problema (
                          id BIGINT IDENTITY(1,1) NOT NULL,
                          tipo_problema VARCHAR(50) NOT NULL,
                          descricao VARCHAR(255) NOT NULL,
                          data_registro DATE NOT NULL,
                          resolvido BIT NOT NULL,
                          moto_id BIGINT NOT NULL,
                          usuario_id BIGINT NOT NULL,
                          CONSTRAINT problema_pkey PRIMARY KEY (id),
                          FOREIGN KEY (moto_id) REFERENCES moto(id),
                          FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

-- ÍNDICE FILTRADO(Configuração para conexão com o SQL Server)
-- Permite multiplos valores NULL (vagas vazias). Ainda garantindo que um ID de moto só possa aparecer uma vez.
CREATE UNIQUE INDEX idx_posicao_patio_moto_id_not_null
    ON posicao_patio(moto_id)
    WHERE moto_id IS NOT NULL;