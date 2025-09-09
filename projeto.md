# Sistema de Gestão de Projetos e Equipes

## Introdução

A Oracle possui muitos clientes que possuem suas demandas de trabalho totalmente orientada a projetos. De modo geral, a necessidade frequentemente observada diz respeito ao controle mais efetivo dos projetos cujo desenvolvimento é de responsabilidade dos colaboradores que exercem as funções de desenvolvedor, analista de sistemas, designer de interface. Este controle envolve a distribuição das etapas do projeto aos colaboradores, visando um melhor aproveitamento do corpo de colaboradores da empresa, assim como o acompanhamento do andamento do projeto, visando garantir os prazos de entrega contratados.

Estes clientes, ao utilizar as tecnologias Oracle, acabam por aumentar o seu portfólio de projetos, o que gera uma dificuldade na gestão dos mesmos e a distribuição das etapas a serem executadas por cada colaborador envolvido, necessitando de uma ferramenta mais adequada e específica para manter o controle do andamento do projeto, conseguindo proporcionar garantias para cumprir com prazos de entrega e melhor utilização dos profissionais que colaboram com a empresa.

## A Demanda

Como observado, a necessidade principal é a de construir um Sistema para Gestão de Projetos e também das Equipes, objetivando uma gestão eficaz dos projetos, das tarefas para o projeto, das equipes envolvidas, prazos de entregas para os entregáveis, assim como emitir relatórios de desempenho para cada projeto.

## Tecnologias envolvidas

O Projeto deve ser desenvolvido utilizando-se a linguagem Java com interface visual (Swing, JavaFX ou similar), conectado a um banco de dados MySQL. O sistema deverá ter diferentes perfis de acesso (ex: administrador, gerente de projeto, colaborador) e permitir requisitos explícitos e implícitos.

## Requisitos Iniciais (explícitos)

1.  **Cadastro de Usuários**

    a) Nome completo, CPF, e-mail, cargo, login, senha.

    b) Cada usuário deve ter um perfil: administrador, gerente ou colaborador.
2.  **Cadastro de Projetos**

    c) Nome do projeto, descrição, data de início, data de término prevista, status (planejado, em andamento, concluído, cancelado).

    d) Cada projeto deve ter um gerente responsável.
3.  **Cadastro de Equipes**

    e) Nome da equipe, descrição, membros (usuários vinculados).

    f) Uma equipe pode atuar em vários projetos.
4.  **Alocação de Equipes a Projetos**

    g) Um projeto pode ter mais de uma equipe envolvida.

    h) Uma equipe pode atuar em mais de um projeto.
5.  **Cadastro de Tarefas**

    i) Título, descrição, projeto vinculado, responsável (usuário), status (pendente, em execução, concluída), data de início e fim previstas e reais.

    j) Cada tarefa pertence a um único projeto.
6.  **Relatórios e Dashboards**

    k) Resumo de andamento dos projetos.

    l) Desempenho de cada colaborador (tarefas atribuídas e concluídas).

    m) Projetos com risco de atraso (data atual > data prevista de término).
7.  **Autenticação**

    n) Tela de login com validação no banco de dados.
8.  **Interface visual amigável**

    o) Prototipar telas antes de codificar.

    p) Interface deve permitir fácil navegação, cadastro, edição e visualização dos dados.

## Requisitos Implícitos (o aluno deve pensar sobre)

*   Como será feita a vinculação entre tarefas, projetos e colaboradores?
*   É possível que um colaborador esteja em mais de uma equipe?
*   O que acontece com as tarefas se o projeto for cancelado?
*   Será necessário histórico de alterações nos status das tarefas?
*   Como controlar quem pode alterar o quê, com base no perfil do usuário?
*   De que forma será feito o relacionamento entre tabelas no banco?
*   Qual a melhor forma de organizar os pacotes da aplicação Java?
*   Haverá necessidade de logs de acesso ou atividades do sistema?
*   Qual o tratamento ideal para campos obrigatórios e validações de formulário?
