-- Inserir Cidades (sem IDs para auto-incremento funcionar)
INSERT INTO cidade (nome, estado) VALUES ('São Paulo', 'SP');
INSERT INTO cidade (nome, estado) VALUES ('Rio de Janeiro', 'RJ');

-- Inserir Pagamentos
-- Removido temporariamente para evitar erros de chave estrangeira com associados

-- Inserir Eventos
INSERT INTO evento (id, nome, data, descricao) VALUES
(1, 'Baile de Aniversário', '2025-10-28', 'Celebração do aniversário de 40 anos do clube com música ao vivo, bolo e sorteios.'),
(2, 'Bingo Beneficente', '2025-11-05', 'Tradicional bingo com prêmios incríveis. Toda a renda será revertida para a manutenção da sede.');

-- Inserir Usuarios
-- ATENCAO: Substitua os placeholders de senha pelos hashes BCrypt gerados (ex: $2a$10$...)
INSERT INTO users (id, username, password, role) VALUES
(1, 'admin', '$2a$10$tRv3dgEGebQ9gsCy2mC8VOxkGb/z1ZD67tBCWpfBBFGDhm.wQFUnS', 'ADMIN'),
(2, 'secretario', '$2a$10$tRv3dgEGebQ9gsCy2mC8VOxkGb/z1ZD67tBCWpfBBFGDhm.wQFUnS', 'SECRETARIO');
