DROP DATABASE IF EXISTS edulivre;

CREATE DATABASE edulivre;

\c edulivre;

CREATE SCHEMA IF NOT EXISTS edulivre;
SET search_path TO edulivre;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE usuario (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(20) NOT NULL CHECK (perfil IN ('aluno', 'professor', 'admin')),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO usuario (nome, email, senha, perfil) VALUES
('Kehelem Socoowski', 'kethelems@email.com', '123456', 'professor'),
('Elisa Reis', 'elisar@email.com', '654321', 'aluno'),
('Fulana', 'fulana@email.com', '456123', 'admin'),
('Ciclano', 'ciclano@email.com', '456789', 'aluno');

CREATE TABLE curso (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    descricao TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    avaliacao JSONB DEFAULT '{"media": 0, "comentarios": []}'::jsonb,
    professor_id UUID REFERENCES usuario(id) ON DELETE SET NULL
);

INSERT INTO curso (titulo, descricao, professor_id, avaliacao) VALUES
('JDBC', 'Curso introdutório de JDBC',
    (SELECT id FROM usuario WHERE email = 'kethelems@email.com'),
    '{"media": 4.5, "comentarios": [{"usuario_id": "123e4567-e89b-12d3-a456-426614174000", "nota": 5, "comentario": "Perfeito!", "data": "2025-05-20"}]}'::jsonb),
('PostgreSQL Avançado', 'Curso avançado de banco de dados',
    (SELECT id FROM usuario WHERE email = 'kethelems@email.com'),
    '{"media": 4.2, "comentarios": []}'::jsonb);

CREATE TABLE matricula (
    id SERIAL PRIMARY KEY,
    usuario_id UUID NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
    curso_id UUID NOT NULL REFERENCES curso(id) ON DELETE CASCADE,
    data_matricula TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(usuario_id, curso_id) 
);

INSERT INTO matricula (usuario_id, curso_id) VALUES
((SELECT id FROM usuario WHERE email = 'elisar@email.com'),
    (SELECT id FROM curso WHERE titulo = 'JDBC')),
((SELECT id FROM usuario WHERE email = 'ciclano@email.com'),
    (SELECT id FROM curso WHERE titulo = 'PostgreSQL Avançado'));


CREATE TABLE conteudo (
    id SERIAL PRIMARY KEY,
    curso_id UUID NOT NULL REFERENCES curso(id) ON DELETE CASCADE,
    titulo VARCHAR(200) NOT NULL,
    descricao TEXT,
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('video', 'pdf', 'imagem', 'audio', 'quiz', 'slide')),
    arquivo BYTEA,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


INSERT INTO conteudo (curso_id, titulo, descricao, tipo, arquivo) VALUES
((SELECT id FROM curso WHERE titulo = 'JDBC'),
    'Introducao ao JDBC', 'Primeiro vídeo do curso', 'video',
    decode('48656c6c6f20576f726c64', 'hex')),
((SELECT id FROM curso WHERE titulo = 'JDBC'),
    'Slides Aula 1', 'Material de apoio', 'slide',
    decode('536c696465732064612061756c61', 'hex')),
((SELECT id FROM curso WHERE titulo = 'PostgreSQL Avançado'),
    'Performance e Otimização', 'Técnicas avançadas de otimizacao', 'pdf',
    decode('506f737467726553514c20416476616e636564', 'hex'));


CREATE INDEX idx_matricula_usuario ON matricula(usuario_id);
CREATE INDEX idx_matricula_curso ON matricula(curso_id);
CREATE INDEX idx_conteudo_curso ON conteudo(curso_id);
CREATE INDEX idx_curso_avaliacao ON curso USING GIN (avaliacao);