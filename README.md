# Sistema de Gestão de Projetos e Equipes

## 📋 Descrição do Projeto

Sistema desktop desenvolvido em Java para gerenciamento de projetos, equipes e tarefas, atendendo às necessidades de empresas orientadas a projetos. O sistema oferece controle efetivo sobre o desenvolvimento de projetos, distribuição de tarefas e acompanhamento de prazos.

## 🎯 Objetivos

- Gestão eficaz de projetos e equipes
- Controle de tarefas e prazos
- Diferentes perfis de acesso (Administrador, Gerente, Colaborador)
- Relatórios de desempenho e dashboards
- Interface amigável e intuitiva

## 🛠️ Tecnologias Utilizadas

- **Linguagem**: Java SE 8+
- **Interface Gráfica**: Java Swing
- **Banco de Dados**: MySQL 8.0
- **Driver JDBC**: MySQL Connector/J 8.2.0
- **Arquitetura**: MVC (Model-View-Controller)
- **Containerização**: Docker (para banco de dados)

## 📁 Estrutura do Projeto

```
DL01-64504-2025210-EGR0295-TADL/
├── src/main/java/com/oracle/gestao/
│   ├── model/              # Entidades de dados
│   │   ├── Usuario.java
│   │   ├── Projeto.java
│   │   ├── Equipe.java
│   └── └── Tarefa.java
│   ├── dao/                # Acesso a dados
│   │   ├── UsuarioDAO.java
│   │   ├── ProjetoDAO.java
│   │   ├── EquipeDAO.java
│   └── └── TarefaDAO.java
│   ├── view/               # Interface gráfica
│   │   ├── LoginFrame.java
│   │   ├── MainFrame.java
│   │   ├── UsuarioPanel.java
│   │   ├── ProjetoPanel.java
│   │   ├── EquipePanel.java
│   │   ├── TarefaPanel.java
│   └── └── DashboardPanel.java
│   ├── controller/         # Lógica de controle
│   │   └── LoginController.java
│   ├── util/               # Utilitários
│   │   └── DatabaseConnection.java
│   └── Main.java           # Classe principal
├── lib/                    # Bibliotecas externas
│   └── mysql-connector-j-8.2.0.jar
├── sql/                    # Scripts de banco
│   └── schema.sql
├── compile.bat             # Script de compilação
├── run.bat                 # Script de execução
└── README.md               # Documentação
```

## 🐳 Configuração do Banco de Dados com Docker

### 1. Criar e executar container MySQL

```bash
# Criar e executar container MySQL
docker run --name mysql-gestao \
  -e MYSQL_ROOT_PASSWORD=root123 \
  -e MYSQL_DATABASE=gestao_projetos \
  -p 3306:3306 \
  -d mysql:8.0
```

### 2. Importar schema do banco

```bash
# Copiar arquivo SQL para o container
docker cp sql/schema.sql mysql-gestao:/schema.sql

# Executar o script SQL
docker exec -i mysql-gestao mysql -uroot -proot123 gestao_projetos < /schema.sql
```

### 3. Verificar se o banco foi criado

```bash
# Acessar MySQL no container
docker exec -it mysql-gestao mysql -uroot -proot123

# Verificar banco e tabelas
USE gestao_projetos;
SHOW TABLES;
```

### 4. Comandos úteis do Docker

```bash
# Parar container
docker stop mysql-gestao

# Iniciar container existente
docker start mysql-gestao

# Remover container
docker rm mysql-gestao

# Ver logs do container
docker logs mysql-gestao
```

## 🚀 Como Executar o Projeto

### Pré-requisitos

- Java JDK 8 ou superior
- Docker (para banco de dados)
- Git (opcional)

### Passos para execução

1. **Clone o repositório** (se aplicável)
```bash
git clone <url-do-repositorio>
cd DL01-64504-2025210-EGR0295-TADL
```

2. **Configure o banco de dados**
```bash
# Execute os comandos Docker mencionados acima
docker run --name mysql-gestao -e MYSQL_ROOT_PASSWORD=root123 -e MYSQL_DATABASE=gestao_projetos -p 3306:3306 -d mysql:8.0
docker cp sql/schema.sql mysql-gestao:/schema.sql
docker exec -i mysql-gestao mysql -uroot -proot123 gestao_projetos < /schema.sql
```

3. **Compile o projeto**
```bash
# Windows
compile.bat
```

4. **Execute a aplicação**
```bash
# Windows
run.bat
```

## 👤 Usuário Padrão

O sistema vem com um usuário administrador pré-cadastrado:

- **Login**: admin
- **Senha**: admin123

## 🔧 Funcionalidades Implementadas

### 🔐 Sistema de Autenticação
- Login com validação no banco de dados
- Controle de acesso por perfil de usuário
- Sessão de usuário logado

### 👥 Gestão de Usuários
- Cadastro de usuários (nome, CPF, email, cargo, login, senha)
- Três perfis: Administrador, Gerente, Colaborador
- Listagem e consulta de usuários

### 📊 Gestão de Projetos
- Cadastro de projetos (nome, descrição, datas, status)
- Status: Planejado, Em Andamento, Concluído, Cancelado
- Vinculação de gerente responsável
- Alocação de equipes aos projetos

### 👨‍💼 Gestão de Equipes
- Cadastro de equipes (nome, descrição)
- Vinculação de membros às equipes
- Relacionamento N:N entre usuários e equipes
- Alocação de equipes a múltiplos projetos

### ✅ Gestão de Tarefas
- Cadastro de tarefas (título, descrição, datas)
- Status: Pendente, Em Execução, Concluída
- Vinculação a projetos e responsáveis
- Controle de datas previstas vs reais

### 📈 Dashboard e Relatórios
- Resumo de andamento dos projetos
- Desempenho de colaboradores
- Identificação de projetos em risco de atraso

## 🏗️ Arquitetura do Sistema

### Padrão MVC (Model-View-Controller)

- **Model**: Classes de entidade (Usuario, Projeto, Equipe, Tarefa)
- **View**: Interfaces gráficas em Swing
- **Controller**: Lógica de negócio e controle de fluxo

### Padrão DAO (Data Access Object)

- Separação da lógica de acesso a dados
- Classes DAO para cada entidade
- Reutilização de código e facilidade de manutenção

## 🗄️ Modelo de Dados

### Principais Entidades

- **usuarios**: Dados dos usuários do sistema
- **projetos**: Informações dos projetos
- **equipes**: Dados das equipes
- **tarefas**: Informações das tarefas
- **usuario_equipe**: Relacionamento N:N usuários-equipes
- **projeto_equipe**: Relacionamento N:N projetos-equipes

### Relacionamentos

- Um projeto tem um gerente (1:N)
- Uma tarefa pertence a um projeto (N:1)
- Uma tarefa tem um responsável (N:1)
- Usuários podem estar em várias equipes (N:N)
- Equipes podem atuar em vários projetos (N:N)

## 🔒 Controle de Acesso

### Administrador
- Acesso completo ao sistema
- Cadastro de usuários
- Visualização de todos os dados

### Gerente
- Gestão de projetos sob sua responsabilidade
- Cadastro de equipes e tarefas
- Relatórios de seus projetos

### Colaborador
- Visualização de suas tarefas
- Atualização de status das tarefas
- Consulta de projetos em que participa

## 🧪 Testes

### Testes Funcionais
- Login com credenciais válidas/inválidas
- Cadastro de usuários, projetos, equipes e tarefas
- Navegação entre telas
- Validação de campos obrigatórios

### Testes de Integração
- Conexão com banco de dados
- Operações CRUD em todas as entidades
- Relacionamentos entre tabelas

## 📝 Melhorias Futuras

- Criptografia de senhas
- Logs de auditoria
- Notificações de prazos
- Exportação de relatórios
- Interface web
- API REST

## 📄 Licença

Este projeto foi desenvolvido para fins acadêmicos.

---

**Versão**: 1.0  
**Data**: Setembro 2025  
**Curso**: EGR0295 - Desenvolvimento de Sistemas  
**Instituição**: [Anhembi Morumbi]