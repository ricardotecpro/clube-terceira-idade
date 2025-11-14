-- Inserir Cidades
INSERT INTO cidade (id, nome, estado) VALUES (1, 'São Paulo', 'SP');
INSERT INTO cidade (id, nome, estado) VALUES (2, 'Rio de Janeiro', 'RJ');

-- Inserir Associados
INSERT INTO associado (id, nome, cpf, data_nascimento, situacao, cidade_id) VALUES
(1001, 'Maria Oliveira da Silva', '111.222.333-44', '1960-05-15', 'Adimplente', 1),
(1002, 'José Pereira Souza', '444.555.666-77', '1955-08-20', 'Inadimplente', 2),
(1003, 'Antônio Carlos Mendes', '777.888.999-00', '1962-11-30', 'Adimplente', 1);

-- Inserir Pagamentos
INSERT INTO pagamento (id, valor, data_pagamento, tipo_pagamento, forma_pagamento, associado_id) VALUES
(1, 50.00, '2025-11-10', 'Mensalidade', 'Cartão de Crédito', 1001),
(2, 50.00, '2025-11-10', 'Mensalidade', NULL, 1002), -- Pendente
(3, 50.00, '2025-11-10', 'Mensalidade', 'Boleto', 1003);

-- Inserir Eventos
INSERT INTO evento (id, nome, data, descricao) VALUES
(1, 'Baile de Aniversário', '2025-10-28', 'Celebração do aniversário de 40 anos do clube com música ao vivo, bolo e sorteios.'),
(2, 'Bingo Beneficente', '2025-11-05', 'Tradicional bingo com prêmios incríveis. Toda a renda será revertida para a manutenção da sede.');

-- Inserir Usuarios
-- ATENCAO: Substitua os placeholders de senha pelos hashes BCrypt gerados (ex: $2a$10$...)
INSERT INTO users (id, username, password, role) VALUES
(1, 'admin', '$2a$10$tRv3dgEGebQ9gsCy2mC8VOxkGb/z1ZD67tBCWpfBBFGDhm.wQFUnS', 'ADMIN'),
(2, 'secretario', '$2a$10$tRv3dgEGebQ9gsCy2mC8VOxkGb/z1ZD67tBCWpfBBFGDhm.wQFUnS', 'SECRETARIO');
