package com.oracle.gestao.view;

import com.oracle.gestao.dao.ProjetoDAO;
import com.oracle.gestao.dao.UsuarioDAO;
import com.oracle.gestao.dao.EquipeDAO;
import com.oracle.gestao.model.Projeto;
import com.oracle.gestao.model.Usuario;
import com.oracle.gestao.model.Equipe;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ProjetoPanel extends JPanel {
    private JTextField txtNome, txtDescricao, txtDataInicio, txtDataFim;
    private JComboBox<Usuario> cbGerente;
    private JComboBox<String> cbStatus;
    private JList<Equipe> listEquipes;
    private JTable table;
    private DefaultTableModel tableModel;
    private ProjetoDAO projetoDAO;
    private UsuarioDAO usuarioDAO;
    private EquipeDAO equipeDAO;
    private int projetoEditandoId = -1;
    
    public ProjetoPanel() {
        projetoDAO = new ProjetoDAO();
        usuarioDAO = new UsuarioDAO();
        equipeDAO = new EquipeDAO();
        initComponents();
        carregarGerentes();
        carregarEquipes();
        carregarProjetos();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Painel de formulário
        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setBorder(BorderFactory.createTitledBorder("Cadastro de Projeto"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Campos do formulário
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
        
        gbc.gridx = 0; gbc.gridy = 2;
        painelForm.add(new JLabel("Data Início (yyyy-mm-dd):"), gbc);
        txtDataInicio = new JTextField(20);
        gbc.gridx = 1;
        painelForm.add(txtDataInicio, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        painelForm.add(new JLabel("Data Fim Prevista (yyyy-mm-dd):"), gbc);
        txtDataFim = new JTextField(20);
        gbc.gridx = 1;
        painelForm.add(txtDataFim, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        painelForm.add(new JLabel("Gerente:"), gbc);
        cbGerente = new JComboBox<>();
        gbc.gridx = 1;
        painelForm.add(cbGerente, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        painelForm.add(new JLabel("Status:"), gbc);
        cbStatus = new JComboBox<>(new String[]{"planejado", "em_andamento", "concluido", "cancelado"});
        gbc.gridx = 1;
        painelForm.add(cbStatus, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        painelForm.add(new JLabel("<html>Equipes:<br><small>(Ctrl+Click para múltiplas)</small></html>"), gbc);
        listEquipes = new JList<>();
        listEquipes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listEquipes.setVisibleRowCount(4);
        JScrollPane scrollEquipes = new JScrollPane(listEquipes);
        scrollEquipes.setPreferredSize(new Dimension(200, 80));
        gbc.gridx = 1;
        painelForm.add(scrollEquipes, gbc);
        
        // Botões
        JPanel painelBotoes = new JPanel();
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvarProjeto());
        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> editarProjeto());
        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(e -> excluirProjeto());
        JButton btnRefresh = new JButton("Atualizar");
        btnRefresh.addActionListener(e -> atualizarDados());
        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(e -> limparCampos());
        
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnRefresh);
        painelBotoes.add(btnLimpar);
        
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        painelForm.add(painelBotoes, gbc);
        
        add(painelForm, BorderLayout.NORTH);
        
        // Tabela
        String[] colunas = {"ID", "Nome", "Descrição", "Data Início", "Data Fim Prevista", "Status", "Gerente"};
        tableModel = new DefaultTableModel(colunas, 0);
        table = new JTable(tableModel);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Projetos Cadastrados"));
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void carregarGerentes() {
        cbGerente.removeAllItems();
        List<Usuario> gerentes = usuarioDAO.listarPorPerfil("gerente");
        gerentes.addAll(usuarioDAO.listarPorPerfil("administrador"));
        
        for (Usuario gerente : gerentes) {
            cbGerente.addItem(gerente);
        }
    }
    
    private void carregarEquipes() {
        List<Equipe> equipes = equipeDAO.listarTodas();
        DefaultListModel<Equipe> listModel = new DefaultListModel<>();
        for (Equipe equipe : equipes) {
            listModel.addElement(equipe);
        }
        listEquipes.setModel(listModel);
    }
    
    private void salvarProjeto() {
        try {
            String nome = txtNome.getText().trim();
            String descricao = txtDescricao.getText().trim();
            String dataInicioStr = txtDataInicio.getText().trim();
            String dataFimStr = txtDataFim.getText().trim();
            Usuario gerente = (Usuario) cbGerente.getSelectedItem();
            
            if (nome.isEmpty() || descricao.isEmpty() || dataInicioStr.isEmpty() || 
                dataFimStr.isEmpty() || gerente == null) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
                return;
            }
            
            LocalDate dataInicio = LocalDate.parse(dataInicioStr);
            LocalDate dataFim = LocalDate.parse(dataFimStr);
            
            Projeto projeto = new Projeto(nome, descricao, dataInicio, dataFim, gerente.getId());
            projeto.setStatus((String) cbStatus.getSelectedItem());
            
            boolean sucesso;
            String mensagem;
            
            if (projetoEditandoId == -1) {
                // Novo projeto
                sucesso = projetoDAO.inserir(projeto);
                mensagem = sucesso ? "Projeto cadastrado com sucesso!" : "Erro ao cadastrar projeto!";
            } else {
                // Editando projeto existente
                projeto.setId(projetoEditandoId);
                sucesso = projetoDAO.atualizar(projeto);
                mensagem = sucesso ? "Projeto atualizado com sucesso!" : "Erro ao atualizar projeto!";
            }
            
            if (sucesso) {
                // Alocar equipes selecionadas
                List<Equipe> equipesSelecionadas = listEquipes.getSelectedValuesList();
                if (!equipesSelecionadas.isEmpty()) {
                    int projetoId = projetoEditandoId == -1 ? projeto.getId() : projetoEditandoId;
                    for (Equipe equipe : equipesSelecionadas) {
                        equipeDAO.alocarEquipeProjeto(projetoId, equipe.getId());
                    }
                }
                
                JOptionPane.showMessageDialog(this, mensagem);
                limparCampos();
                carregarEquipes(); // Recarregar equipes
                carregarProjetos();
            } else {
                JOptionPane.showMessageDialog(this, mensagem);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }
    

    
    private void limparCampos() {
        txtNome.setText("");
        txtDescricao.setText("");
        txtDataInicio.setText("");
        txtDataFim.setText("");
        cbStatus.setSelectedIndex(0);
        if (cbGerente.getItemCount() > 0) {
            cbGerente.setSelectedIndex(0);
        }
        listEquipes.clearSelection();
        projetoEditandoId = -1; // Reset do modo de edição
    }
    
    private void editarProjeto() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um projeto na tabela para editar!");
            return;
        }
        
        projetoEditandoId = (Integer) tableModel.getValueAt(selectedRow, 0);
        
        // Preencher campos com dados do projeto selecionado
        txtNome.setText((String) tableModel.getValueAt(selectedRow, 1));
        txtDescricao.setText((String) tableModel.getValueAt(selectedRow, 2));
        
        // Converter data de volta para formato yyyy-mm-dd
        String dataInicioStr = (String) tableModel.getValueAt(selectedRow, 3);
        String dataFimStr = (String) tableModel.getValueAt(selectedRow, 4);
        
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        try {
            LocalDate dataInicio = LocalDate.parse(dataInicioStr, displayFormatter);
            LocalDate dataFim = LocalDate.parse(dataFimStr, displayFormatter);
            
            txtDataInicio.setText(dataInicio.format(inputFormatter));
            txtDataFim.setText(dataFim.format(inputFormatter));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar datas: " + e.getMessage());
        }
        
        // Selecionar status
        String status = (String) tableModel.getValueAt(selectedRow, 5);
        cbStatus.setSelectedItem(status);
        
        // Selecionar gerente
        String nomeGerente = (String) tableModel.getValueAt(selectedRow, 6);
        for (int i = 0; i < cbGerente.getItemCount(); i++) {
            Usuario usuario = cbGerente.getItemAt(i);
            if (usuario.getNomeCompleto().equals(nomeGerente)) {
                cbGerente.setSelectedIndex(i);
                break;
            }
        }
        
        // Carregar equipes já alocadas ao projeto
        carregarEquipesAlocadas(projetoEditandoId);
    }
    
    private void carregarEquipesAlocadas(int projetoId) {
        List<String> nomesEquipesAlocadas = projetoDAO.listarEquipesProjeto(projetoId);
        DefaultListModel<Equipe> model = (DefaultListModel<Equipe>) listEquipes.getModel();
        
        listEquipes.clearSelection();
        
        for (int i = 0; i < model.getSize(); i++) {
            Equipe equipe = model.getElementAt(i);
            if (nomesEquipesAlocadas.contains(equipe.getNome())) {
                listEquipes.addSelectionInterval(i, i);
            }
        }
    }
    
    private void excluirProjeto() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um projeto para excluir!");
            return;
        }
        
        int projetoId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String nomeProjeto = (String) tableModel.getValueAt(selectedRow, 1);
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente excluir o projeto " + nomeProjeto + "?\nIsto removerá todas as tarefas associadas!", 
            "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION);
            
        if (opcao == JOptionPane.YES_OPTION) {
            if (projetoDAO.excluir(projetoId)) {
                JOptionPane.showMessageDialog(this, "Projeto excluído com sucesso!");
                carregarProjetos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir projeto!");
            }
        }
    }
    
    private void carregarProjetos() {
        tableModel.setRowCount(0);
        List<Projeto> projetos = projetoDAO.listarTodos();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (Projeto projeto : projetos) {
            Object[] row = {
                projeto.getId(),
                projeto.getNome(),
                projeto.getDescricao(),
                projeto.getDataInicio().format(formatter),
                projeto.getDataFimPrevista().format(formatter),
                projeto.getStatus(),
                projeto.getNomeGerente()
            };
            tableModel.addRow(row);
        }
    }
    
    private void atualizarDados() {
        carregarGerentes();
        carregarEquipes();
        carregarProjetos();
    }
}