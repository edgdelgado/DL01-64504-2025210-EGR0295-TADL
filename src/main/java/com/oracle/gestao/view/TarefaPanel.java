package com.oracle.gestao.view;

import com.oracle.gestao.dao.TarefaDAO;
import com.oracle.gestao.dao.ProjetoDAO;
import com.oracle.gestao.dao.UsuarioDAO;
import com.oracle.gestao.model.Tarefa;
import com.oracle.gestao.model.Projeto;
import com.oracle.gestao.model.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TarefaPanel extends JPanel {
    private JTextField txtTitulo, txtDescricao, txtDataInicio, txtDataFim;
    private JComboBox<Projeto> cbProjeto;
    private JComboBox<Usuario> cbResponsavel;
    private JComboBox<String> cbStatus;
    private JTable table;
    private DefaultTableModel tableModel;
    private TarefaDAO tarefaDAO;
    private ProjetoDAO projetoDAO;
    private UsuarioDAO usuarioDAO;
    private Usuario usuarioLogado;
    private int tarefaEditandoId = -1;
    
    public TarefaPanel(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        tarefaDAO = new TarefaDAO();
        projetoDAO = new ProjetoDAO();
        usuarioDAO = new UsuarioDAO();
        initComponents();
        carregarDados();
        carregarTarefas();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Painel de formulário
        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setBorder(BorderFactory.createTitledBorder("Cadastro de Tarefa"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Campos do formulário
        gbc.gridx = 0; gbc.gridy = 0;
        painelForm.add(new JLabel("Título:"), gbc);
        txtTitulo = new JTextField(20);
        gbc.gridx = 1;
        painelForm.add(txtTitulo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        painelForm.add(new JLabel("Descrição:"), gbc);
        txtDescricao = new JTextField(20);
        gbc.gridx = 1;
        painelForm.add(txtDescricao, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        painelForm.add(new JLabel("Projeto:"), gbc);
        cbProjeto = new JComboBox<>();
        gbc.gridx = 1;
        painelForm.add(cbProjeto, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        painelForm.add(new JLabel("Responsável:"), gbc);
        cbResponsavel = new JComboBox<>();
        gbc.gridx = 1;
        painelForm.add(cbResponsavel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        painelForm.add(new JLabel("Data Início (yyyy-mm-dd):"), gbc);
        txtDataInicio = new JTextField(20);
        gbc.gridx = 1;
        painelForm.add(txtDataInicio, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        painelForm.add(new JLabel("Data Fim (yyyy-mm-dd):"), gbc);
        txtDataFim = new JTextField(20);
        gbc.gridx = 1;
        painelForm.add(txtDataFim, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        painelForm.add(new JLabel("Status:"), gbc);
        cbStatus = new JComboBox<>(new String[]{"pendente", "em_execucao", "concluida"});
        gbc.gridx = 1;
        painelForm.add(cbStatus, gbc);
        
        // Botões
        JPanel painelBotoes = new JPanel();
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvarTarefa());
        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> editarTarefa());
        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(e -> excluirTarefa());
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
        String[] colunas = {"ID", "Título", "Projeto", "Responsável", "Status", "Data Início", "Data Fim"};
        tableModel = new DefaultTableModel(colunas, 0);
        table = new JTable(tableModel);

        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Tarefas"));
        add(scrollPane, BorderLayout.CENTER);
        
        // Filtros para colaborador
        if (usuarioLogado.getPerfil().equals("colaborador")) {
            JPanel painelFiltro = new JPanel();
            JButton btnMinhasTarefas = new JButton("Minhas Tarefas");
            btnMinhasTarefas.addActionListener(e -> carregarMinhasTarefas());
            JButton btnTodasTarefas = new JButton("Todas as Tarefas");
            btnTodasTarefas.addActionListener(e -> carregarTarefas());
            
            painelFiltro.add(btnMinhasTarefas);
            painelFiltro.add(btnTodasTarefas);
            add(painelFiltro, BorderLayout.SOUTH);
        }
    }
    
    private void carregarDados() {
        // Carregar projetos
        cbProjeto.removeAllItems();
        List<Projeto> projetos = projetoDAO.listarTodos();
        for (Projeto projeto : projetos) {
            cbProjeto.addItem(projeto);
        }
        
        // Carregar usuários
        cbResponsavel.removeAllItems();
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        for (Usuario usuario : usuarios) {
            cbResponsavel.addItem(usuario);
        }
    }
    
    private void salvarTarefa() {
        try {
            String titulo = txtTitulo.getText().trim();
            String descricao = txtDescricao.getText().trim();
            Projeto projeto = (Projeto) cbProjeto.getSelectedItem();
            Usuario responsavel = (Usuario) cbResponsavel.getSelectedItem();
            String dataInicioStr = txtDataInicio.getText().trim();
            String dataFimStr = txtDataFim.getText().trim();
            
            if (titulo.isEmpty() || projeto == null || responsavel == null || 
                dataInicioStr.isEmpty() || dataFimStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios!");
                return;
            }
            
            LocalDate dataInicio = LocalDate.parse(dataInicioStr);
            LocalDate dataFim = LocalDate.parse(dataFimStr);
            
            Tarefa tarefa = new Tarefa(titulo, descricao, projeto.getId(), 
                                     responsavel.getId(), dataInicio, dataFim);
            tarefa.setStatus((String) cbStatus.getSelectedItem());
            
            boolean sucesso;
            String mensagem;
            
            if (tarefaEditandoId == -1) {
                // Nova tarefa
                sucesso = tarefaDAO.inserir(tarefa);
                mensagem = sucesso ? "Tarefa cadastrada com sucesso!" : "Erro ao cadastrar tarefa!";
            } else {
                // Editando tarefa existente
                tarefa.setId(tarefaEditandoId);
                sucesso = tarefaDAO.atualizarCompleta(tarefa);
                mensagem = sucesso ? "Tarefa atualizada com sucesso!" : "Erro ao atualizar tarefa!";
            }
            
            if (sucesso) {
                JOptionPane.showMessageDialog(this, mensagem);
                limparCampos();
                carregarTarefas();
            } else {
                JOptionPane.showMessageDialog(this, mensagem);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }
    
    private void editarTarefa() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma tarefa na tabela para editar!");
            return;
        }
        
        tarefaEditandoId = (Integer) tableModel.getValueAt(selectedRow, 0);
        
        // Preencher campos com dados da tarefa selecionada
        txtTitulo.setText((String) tableModel.getValueAt(selectedRow, 1));
        
        // Buscar descrição da tarefa no banco (não está na tabela)
        try {
            List<Tarefa> todasTarefas = tarefaDAO.listarTodas();
            for (Tarefa t : todasTarefas) {
                if (t.getId() == tarefaEditandoId) {
                    txtDescricao.setText(t.getDescricao());
                    break;
                }
            }
        } catch (Exception e) {
            txtDescricao.setText("");
        }
        
        // Selecionar projeto
        String nomeProjeto = (String) tableModel.getValueAt(selectedRow, 2);
        for (int i = 0; i < cbProjeto.getItemCount(); i++) {
            Projeto projeto = cbProjeto.getItemAt(i);
            if (projeto.getNome().equals(nomeProjeto)) {
                cbProjeto.setSelectedIndex(i);
                break;
            }
        }
        
        // Selecionar responsável
        String nomeResponsavel = (String) tableModel.getValueAt(selectedRow, 3);
        for (int i = 0; i < cbResponsavel.getItemCount(); i++) {
            Usuario usuario = cbResponsavel.getItemAt(i);
            if (usuario.getNomeCompleto().equals(nomeResponsavel)) {
                cbResponsavel.setSelectedIndex(i);
                break;
            }
        }
        
        // Selecionar status
        String status = (String) tableModel.getValueAt(selectedRow, 4);
        cbStatus.setSelectedItem(status);
        
        // Converter datas de volta para formato yyyy-mm-dd
        String dataInicioStr = (String) tableModel.getValueAt(selectedRow, 5);
        String dataFimStr = (String) tableModel.getValueAt(selectedRow, 6);
        
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        try {
            if (!dataInicioStr.isEmpty()) {
                LocalDate dataInicio = LocalDate.parse(dataInicioStr, displayFormatter);
                txtDataInicio.setText(dataInicio.format(inputFormatter));
            }
            if (!dataFimStr.isEmpty()) {
                LocalDate dataFim = LocalDate.parse(dataFimStr, displayFormatter);
                txtDataFim.setText(dataFim.format(inputFormatter));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar datas: " + e.getMessage());
        }
    }
    

    
    private void limparCampos() {
        txtTitulo.setText("");
        txtDescricao.setText("");
        txtDataInicio.setText("");
        txtDataFim.setText("");
        cbStatus.setSelectedIndex(0);
        if (cbProjeto.getItemCount() > 0) {
            cbProjeto.setSelectedIndex(0);
        }
        if (cbResponsavel.getItemCount() > 0) {
            cbResponsavel.setSelectedIndex(0);
        }
        tarefaEditandoId = -1; // Reset do modo de edição
    }
    
    private void excluirTarefa() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma tarefa para excluir!");
            return;
        }
        
        int tarefaId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String tituloTarefa = (String) tableModel.getValueAt(selectedRow, 1);
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente excluir a tarefa " + tituloTarefa + "?", 
            "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION);
            
        if (opcao == JOptionPane.YES_OPTION) {
            if (tarefaDAO.excluir(tarefaId)) {
                JOptionPane.showMessageDialog(this, "Tarefa excluída com sucesso!");
                carregarTarefas();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir tarefa!");
            }
        }
    }
    
    private void carregarTarefas() {
        tableModel.setRowCount(0);
        List<Tarefa> tarefas = tarefaDAO.listarTodas();
        preencherTabela(tarefas);
    }
    
    private void carregarMinhasTarefas() {
        tableModel.setRowCount(0);
        List<Tarefa> tarefas = tarefaDAO.listarPorResponsavel(usuarioLogado.getId());
        preencherTabela(tarefas);
    }
    
    private void preencherTabela(List<Tarefa> tarefas) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (Tarefa tarefa : tarefas) {
            Object[] row = {
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getNomeProjeto(),
                tarefa.getNomeResponsavel(),
                tarefa.getStatus(),
                tarefa.getDataInicioPrevista() != null ? tarefa.getDataInicioPrevista().format(formatter) : "",
                tarefa.getDataFimPrevista() != null ? tarefa.getDataFimPrevista().format(formatter) : ""
            };
            tableModel.addRow(row);
        }
    }
    
    private void atualizarDados() {
        carregarDados();
        carregarTarefas();
    }
}