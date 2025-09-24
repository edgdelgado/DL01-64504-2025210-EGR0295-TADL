-- Schema do Sistema de Gestão de Projetos e Equipes
CREATE DATABASE IF NOT EXISTS gestao_projetos;
USE gestao_projetos;

-- Tabela de Usuários
CREATE TABLE usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome_completo VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    cargo VARCHAR(50) NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    senha VARCHAR(100) NOT NULL,
    perfil ENUM('administrador', 'gerente', 'colaborador') NOT NULL,
    ativo BOOLEAN DEFAULT TRUE
);

-- Tabela de Projetos
CREATE TABLE projetos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    data_inicio DATE NOT NULL,
    data_fim_prevista DATE NOT NULL,
    data_fim_real DATE,
    status ENUM('planejado', 'em_andamento', 'concluido', 'cancelado') DEFAULT 'planejado',
    gerente_id INT NOT NULL,
    FOREIGN KEY (gerente_id) REFERENCES usuarios(id)
);

-- Tabela de Equipes
CREATE TABLE equipes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT
);

-- Tabela de relacionamento Usuário-Equipe (N:N)
CREATE TABLE usuario_equipe (
    usuario_id INT,
    equipe_id INT,
    PRIMARY KEY (usuario_id, equipe_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (equipe_id) REFERENCES equipes(id)
);

-- Tabela de relacionamento Projeto-Equipe (N:N)
CREATE TABLE projeto_equipe (
    projeto_id INT,
    equipe_id INT,
    PRIMARY KEY (projeto_id, equipe_id),
    FOREIGN KEY (projeto_id) REFERENCES projetos(id),
    FOREIGN KEY (equipe_id) REFERENCES equipes(id)
);

-- Tabela de Tarefas
CREATE TABLE tarefas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    descricao TEXT,
    projeto_id INT NOT NULL,
    responsavel_id INT NOT NULL,
    status ENUM('pendente', 'em_execucao', 'concluida') DEFAULT 'pendente',
    data_inicio_prevista DATE,
    data_fim_prevista DATE,
    data_inicio_real DATE,
    data_fim_real DATE,
    FOREIGN KEY (projeto_id) REFERENCES projetos(id),
    FOREIGN KEY (responsavel_id) REFERENCES usuarios(id)
);

-- Inserir usuário administrador padrão
INSERT INTO usuarios (nome_completo, cpf, email, cargo, login, senha, perfil) 
VALUES ('Administrador', '00000000000', 'admin@oracle.com', 'Administrador', 'admin', 'admin123', 'administrador');