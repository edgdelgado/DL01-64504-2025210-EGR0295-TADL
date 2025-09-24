package com.oracle.gestao.view;

import com.oracle.gestao.dao.EquipeDAO;
import com.oracle.gestao.dao.UsuarioDAO;
import com.oracle.gestao.dao.ProjetoDAO;
import com.oracle.gestao.model.Equipe;
import com.oracle.gestao.model.Usuario;
import com.oracle.gestao.model.Projeto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EquipePanel extends JPanel {
    private JTextField txtNome, txtDescricao;
    private JComboBox<Usuario> cbMembros;
    private JComboBox<Projeto> cbProjetos;
    private JComboBox<Equipe> cbEquipes;
    private JComboBox<Equipe> cbEquipesAlocacao;
    private JTable tableEquipes, tableMembros, tableAlocacao;
    private DefaultTableModel tableModelEquipes, tableModelMembros, tableModelAlocacao;
    private EquipeDAO equipeDAO;
    private UsuarioDAO usuarioDAO;
    private ProjetoDAO projetoDAO;
    private int equipeEditandoId = -1;
    
    public EquipePanel() {
        equipeDAO = new EquipeDAO();
        usuarioDAO = new UsuarioDAO();
        projetoDAO = new ProjetoDAO();
        initComponents();
        carregarDados();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Aba Cadastro de Equipes
        JPanel painelCadastro = new JPanel(new BorderLayout());
        
        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setBorder(BorderFactory.createTitledBorder("Cadastro de Equipe"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        painelForm.add(new JLabel("Nome:"), gbc);
        txtNome = new JTextField(20);
        gbc.gridx = 1;
        painelForm.add(txtNome, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        painelForm.add(new JLabel("Descrição:"), gbc);
        txtDescricao = new JTextField(20);
        gbc.gridx = 1;
        painelForm.add(txtDescricao, gbc);
        
        JPanel painelBotoes = new JPanel();
        JButton btnSalvar = new JButton("Salvar Equipe");
        btnSalvar.addActionListener(e -> salvarEquipe());
        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> editarEquipe());
        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(e -> excluirEquipe());
        JButton btnRefresh = new JButton("Atualizar");
        btnRefresh.addActionListener(e -> carregarDados());
        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(e -> limparCampos());
        
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnRefresh);
        painelBotoes.add(btnLimpar);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        painelForm.add(painelBotoes, gbc);
        
        painelCadastro.add(painelForm, BorderLayout.NORTH);
        
        String[] colunasEquipes = {"ID", "Nome", "Descrição"};
        tableModelEquipes = new DefaultTableModel(colunasEquipes, 0);
        tableEquipes = new JTable(tableModelEquipes);
        
        JScrollPane scrollEquipes = new JScrollPane(tableEquipes);
        scrollEquipes.setBorder(BorderFactory.createTitledBorder("Equipes Cadastradas"));
        painelCadastro.add(scrollEquipes, BorderLayout.CENTER);
        
        tabbedPane.addTab("Cadastro", painelCadastro);
        
        // Aba Gerenciar Membros
        JPanel painelMembros = new JPanel(new BorderLayout());
        
        JPanel painelFormMembros = new JPanel(new GridBagLayout());
        painelFormMembros.setBorder(BorderFactory.createTitledBorder("Adicionar Membro à Equipe"));
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        painelFormMembros.add(new JLabel("Equipe:"), gbc);
        cbEquipes = new JComboBox<>();
        cbEquipes.addActionListener(e -> carregarMembrosEquipe());
        gbc.gridx = 1;
        painelFormMembros.add(cbEquipes, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        painelFormMembros.add(new JLabel("Usuário:"), gbc);
        cbMembros = new JComboBox<>();
        gbc.gridx = 1;
        painelFormMembros.add(cbMembros, gbc);
        
        JPanel painelBotoesMembros = new JPanel();
        JButton btnAdicionarMembro = new JButton("Adicionar Membro");
        btnAdicionarMembro.addActionListener(e -> adicionarMembro());
        JButton btnRemoverMembro = new JButton("Remover Selecionado");
        btnRemoverMembro.addActionListener(e -> removerMembro());
        
        painelBotoesMembros.add(btnAdicionarMembro);
        painelBotoesMembros.add(btnRemoverMembro);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        painelFormMembros.add(painelBotoesMembros, gbc);
        
        painelMembros.add(painelFormMembros, BorderLayout.NORTH);
        
        String[] colunasMembros = {"ID", "Nome", "Cargo", "Email"};
        tableModelMembros = new DefaultTableModel(colunasMembros, 0);
        tableMembros = new JTable(tableModelMembros);
        
        JScrollPane scrollMembros = new JScrollPane(tableMembros);
        scrollMembros.setBorder(BorderFactory.createTitledBorder("Membros da Equipe"));
        painelMembros.add(scrollMembros, BorderLayout.CENTER);
        
        tabbedPane.addTab("Membros", painelMembros);
        
        // Aba Alocar Equipes
        JPanel painelAlocacao = new JPanel(new BorderLayout());
        
        JPanel painelFormAlocacao = new JPanel(new GridBagLayout());
        painelFormAlocacao.setBorder(BorderFactory.createTitledBorder("Alocar Equipe a Projeto"));
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0; gbc.gridy = 0;
        painelFormAlocacao.add(new JLabel("Projeto:"), gbc);
        cbProjetos = new JComboBox<>();
        gbc.gridx = 1;
        painelFormAlocacao.add(cbProjetos, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        painelFormAlocacao.add(new JLabel("Equipe:"), gbc);
        cbEquipesAlocacao = new JComboBox<>();
        gbc.gridx = 1;
        painelFormAlocacao.add(cbEquipesAlocacao, gbc);
        
        // Carregar equipes na aba de alocação
        List<Equipe> equipesAlocacao = equipeDAO.listarTodas();
        for (Equipe equipe : equipesAlocacao) {
            cbEquipesAlocacao.addItem(equipe);
        }
        
        JPanel painelBotoesAlocacao = new JPanel();
        JButton btnAlocar = new JButton("Alocar Equipe");
        btnAlocar.addActionListener(e -> {
            Projeto projeto = (Projeto) cbProjetos.getSelectedItem();
            Equipe equipe = (Equipe) cbEquipesAlocacao.getSelectedItem();
            if (projeto != null && equipe != null) {
                if (equipeDAO.alocarEquipeProjeto(projeto.getId(), equipe.getId())) {
                    JOptionPane.showMessageDialog(this, "Equipe alocada com sucesso!");
                    carregarAlocacoes();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao alocar equipe ou alocação já existe!");
                }
            }
        });
        
        JButton btnRemoverAlocacao = new JButton("Remover Alocação");
        btnRemoverAlocacao.addActionListener(e -> removerAlocacao());
        
        JButton btnAtualizarAlocacao = new JButton("Atualizar");
        btnAtualizarAlocacao.addActionListener(e -> atualizarAlocacao());
        
        painelBotoesAlocacao.add(btnAlocar);
        painelBotoesAlocacao.add(btnRemoverAlocacao);
        painelBotoesAlocacao.add(btnAtualizarAlocacao);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        painelFormAlocacao.add(painelBotoesAlocacao, gbc);
        
        painelAlocacao.add(painelFormAlocacao, BorderLayout.NORTH);
        
        // Tabela de alocações
        String[] colunasAlocacao = {"Projeto ID", "Equipe ID", "Projeto", "Equipe", "Gerente", "Status"};
        tableModelAlocacao = new DefaultTableModel(colunasAlocacao, 0);
        tableAlocacao = new JTable(tableModelAlocacao);
        
        // Ocultar colunas de ID
        tableAlocacao.getColumnModel().getColumn(0).setMinWidth(0);
        tableAlocacao.getColumnModel().getColumn(0).setMaxWidth(0);
        tableAlocacao.getColumnModel().getColumn(1).setMinWidth(0);
        tableAlocacao.getColumnModel().getColumn(1).setMaxWidth(0);
        
        JScrollPane scrollAlocacao = new JScrollPane(tableAlocacao);
        scrollAlocacao.setBorder(BorderFactory.createTitledBorder("Alocações Existentes"));
        painelAlocacao.add(scrollAlocacao, BorderLayout.CENTER);
        
        tabbedPane.addTab("Alocação", painelAlocacao);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private void carregarDados() {
        carregarEquipes();
        carregarUsuarios();
        carregarProjetos();
        carregarAlocacoes();
    }
    
    private void carregarAlocacoes() {
        tableModelAlocacao.setRowCount(0);
        
        List<Projeto> projetos = projetoDAO.listarTodos();
        List<Equipe> todasEquipes = equipeDAO.listarTodas();
        
        for (Projeto projeto : projetos) {
            List<String> nomesEquipesAlocadas = projetoDAO.listarEquipesProjeto(projeto.getId());
            for (String nomeEquipe : nomesEquipesAlocadas) {
                // Encontrar ID da equipe pelo nome
                int equipeId = -1;
                for (Equipe equipe : todasEquipes) {
                    if (equipe.getNome().equals(nomeEquipe)) {
                        equipeId = equipe.getId();
                        break;
                    }
                }
                
                Object[] row = {
                    projeto.getId(),      // Projeto ID (oculto)
                    equipeId,            // Equipe ID (oculto)
                    projeto.getNome(),   // Projeto
                    nomeEquipe,          // Equipe
                    projeto.getNomeGerente(), // Gerente
                    projeto.getStatus()  // Status
                };
                tableModelAlocacao.addRow(row);
            }
        }
    }
    
    private void carregarEquipes() {
        tableModelEquipes.setRowCount(0);
        cbEquipes.removeAllItems();
        
        List<Equipe> equipes = equipeDAO.listarTodas();
        for (Equipe equipe : equipes) {
            Object[] row = {equipe.getId(), equipe.getNome(), equipe.getDescricao()};
            tableModelEquipes.addRow(row);
            cbEquipes.addItem(equipe);
        }
    }
    
    private void carregarUsuarios() {
        cbMembros.removeAllItems();
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        for (Usuario usuario : usuarios) {
            cbMembros.addItem(usuario);
        }
    }
    
    private void carregarProjetos() {
        cbProjetos.removeAllItems();
        List<Projeto> projetos = projetoDAO.listarTodos();
        for (Projeto projeto : projetos) {
            cbProjetos.addItem(projeto);
        }
    }
    
    private void carregarMembrosEquipe() {
        tableModelMembros.setRowCount(0);
        Equipe equipe = (Equipe) cbEquipes.getSelectedItem();
        if (equipe != null) {
            List<Usuario> membros = equipeDAO.listarMembros(equipe.getId());
            for (Usuario membro : membros) {
                Object[] row = {membro.getId(), membro.getNomeCompleto(), membro.getCargo(), membro.getEmail()};
                tableModelMembros.addRow(row);
            }
        }
    }
    
    private void salvarEquipe() {
        try {
            String nome = txtNome.getText().trim();
            String descricao = txtDescricao.getText().trim();
            
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha o nome da equipe!");
                return;
            }
            
            Equipe equipe = new Equipe(nome, descricao);
            
            boolean sucesso;
            String mensagem;
            
            if (equipeEditandoId == -1) {
                // Nova equipe
                sucesso = equipeDAO.inserir(equipe);
                mensagem = sucesso ? "Equipe cadastrada com sucesso!" : "Erro ao cadastrar equipe!";
            } else {
                // Editando equipe existente
                equipe.setId(equipeEditandoId);
                sucesso = equipeDAO.atualizar(equipe);
                mensagem = sucesso ? "Equipe atualizada com sucesso!" : "Erro ao atualizar equipe!";
            }
            
            if (sucesso) {
                JOptionPane.showMessageDialog(this, mensagem);
                limparCampos();
                carregarEquipes();
            } else {
                JOptionPane.showMessageDialog(this, mensagem);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }
    
    private void adicionarMembro() {
        Equipe equipe = (Equipe) cbEquipes.getSelectedItem();
        Usuario usuario = (Usuario) cbMembros.getSelectedItem();
        
        if (equipe != null && usuario != null) {
            if (equipeDAO.adicionarMembro(equipe.getId(), usuario.getId())) {
                JOptionPane.showMessageDialog(this, "Membro adicionado com sucesso!");
                carregarMembrosEquipe();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar membro ou membro já existe na equipe!");
            }
        }
    }
    
    private void editarEquipe() {
        int selectedRow = tableEquipes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma equipe na tabela para editar!");
            return;
        }
        
        equipeEditandoId = (Integer) tableModelEquipes.getValueAt(selectedRow, 0);
        txtNome.setText((String) tableModelEquipes.getValueAt(selectedRow, 1));
        txtDescricao.setText((String) tableModelEquipes.getValueAt(selectedRow, 2));
    }
    
    private void removerMembro() {
        int selectedRow = tableMembros.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um membro para remover!");
            return;
        }
        
        Equipe equipe = (Equipe) cbEquipes.getSelectedItem();
        if (equipe == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma equipe primeiro!");
            return;
        }
        
        int usuarioId = (Integer) tableModelMembros.getValueAt(selectedRow, 0);
        String nomeUsuario = (String) tableModelMembros.getValueAt(selectedRow, 1);
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja remover " + nomeUsuario + " da equipe?", 
            "Confirmar Remoção", 
            JOptionPane.YES_NO_OPTION);
            
        if (opcao == JOptionPane.YES_OPTION) {
            if (equipeDAO.removerMembro(equipe.getId(), usuarioId)) {
                JOptionPane.showMessageDialog(this, "Membro removido com sucesso!");
                carregarMembrosEquipe();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao remover membro!");
            }
        }
    }
    
    private void limparCampos() {
        txtNome.setText("");
        txtDescricao.setText("");
        equipeEditandoId = -1;
    }
    
    private void excluirEquipe() {
        int selectedRow = tableEquipes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma equipe para excluir!");
            return;
        }
        
        int equipeId = (Integer) tableModelEquipes.getValueAt(selectedRow, 0);
        String nomeEquipe = (String) tableModelEquipes.getValueAt(selectedRow, 1);
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente excluir a equipe " + nomeEquipe + "?", 
            "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION);
            
        if (opcao == JOptionPane.YES_OPTION) {
            if (equipeDAO.excluir(equipeId)) {
                JOptionPane.showMessageDialog(this, "Equipe excluída com sucesso!");
                carregarEquipes();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir equipe!");
            }
        }
    }
    
    private void removerAlocacao() {
        int selectedRow = tableAlocacao.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma alocação para remover!");
            return;
        }
        
        int projetoId = (Integer) tableModelAlocacao.getValueAt(selectedRow, 0);
        int equipeId = (Integer) tableModelAlocacao.getValueAt(selectedRow, 1);
        String nomeProjeto = (String) tableModelAlocacao.getValueAt(selectedRow, 2);
        String nomeEquipe = (String) tableModelAlocacao.getValueAt(selectedRow, 3);
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja remover a equipe " + nomeEquipe + " do projeto " + nomeProjeto + "?", 
            "Confirmar Remoção", 
            JOptionPane.YES_NO_OPTION);
            
        if (opcao == JOptionPane.YES_OPTION) {
            if (equipeDAO.removerAlocacao(projetoId, equipeId)) {
                JOptionPane.showMessageDialog(this, "Alocação removida com sucesso!");
                carregarAlocacoes();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao remover alocação!");
            }
        }
    }
    
    private void atualizarAlocacao() {
        carregarProjetos();
        
        // Atualizar combo de equipes
        cbEquipesAlocacao.removeAllItems();
        List<Equipe> equipesAlocacao = equipeDAO.listarTodas();
        for (Equipe equipe : equipesAlocacao) {
            cbEquipesAlocacao.addItem(equipe);
        }
        
        carregarAlocacoes();
    }
}