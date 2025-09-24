package com.oracle.gestao.view;

import com.oracle.gestao.dao.UsuarioDAO;
import com.oracle.gestao.model.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsuarioPanel extends JPanel {
    private JTextField txtNome, txtCpf, txtEmail, txtCargo, txtLogin, txtSenha;
    private JComboBox<String> cbPerfil;
    private JTable table;
    private DefaultTableModel tableModel;
    private UsuarioDAO usuarioDAO;
    private int usuarioEditandoId = -1;
    
    public UsuarioPanel() {
        usuarioDAO = new UsuarioDAO();
        initComponents();
        carregarUsuarios();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Painel de formulário
        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setBorder(BorderFactory.createTitledBorder("Cadastro de Usuário"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Campos do formulário
        gbc.gridx = 0; gbc.gridy = 0;
        painelForm.add(new JLabel("Nome Completo:"), gbc);
        txtNome = new JTextField(20);
        gbc.gridx = 1;
        painelForm.add(txtNome, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        painelForm.add(new JLabel("CPF:"), gbc);
        txtCpf = new JTextField(20);
        gbc.gridx = 1;
        painelForm.add(txtCpf, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        painelForm.add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField(20);
        gbc.gridx = 1;
        painelForm.add(txtEmail, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        painelForm.add(new JLabel("Cargo:"), gbc);
        txtCargo = new JTextField(20);
        gbc.gridx = 1;
        painelForm.add(txtCargo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        painelForm.add(new JLabel("Login:"), gbc);
        txtLogin = new JTextField(20);
        gbc.gridx = 1;
        painelForm.add(txtLogin, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        painelForm.add(new JLabel("Senha:"), gbc);
        txtSenha = new JPasswordField(20);
        gbc.gridx = 1;
        painelForm.add(txtSenha, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        painelForm.add(new JLabel("Perfil:"), gbc);
        cbPerfil = new JComboBox<>(new String[]{"colaborador", "gerente", "administrador"});
        gbc.gridx = 1;
        painelForm.add(cbPerfil, gbc);
        
        // Botões
        JPanel painelBotoes = new JPanel();
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvarUsuario());
        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> editarUsuario());
        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(e -> excluirUsuario());
        JButton btnRefresh = new JButton("Atualizar");
        btnRefresh.addActionListener(e -> carregarUsuarios());
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
        String[] colunas = {"ID", "Nome", "CPF", "Email", "Cargo", "Login", "Perfil"};
        tableModel = new DefaultTableModel(colunas, 0);
        table = new JTable(tableModel);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Usuários Cadastrados"));
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void salvarUsuario() {
        try {
            String nome = txtNome.getText().trim();
            String cpf = txtCpf.getText().trim();
            String email = txtEmail.getText().trim();
            String cargo = txtCargo.getText().trim();
            String login = txtLogin.getText().trim();
            String senha = txtSenha.getText().trim();
            String perfil = (String) cbPerfil.getSelectedItem();
            
            if (nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || 
                cargo.isEmpty() || login.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
                return;
            }
            
            Usuario usuario = new Usuario(nome, cpf, email, cargo, login, senha, perfil);
            
            boolean sucesso;
            String mensagem;
            
            if (usuarioEditandoId == -1) {
                // Novo usuário
                sucesso = usuarioDAO.inserir(usuario);
                mensagem = sucesso ? "Usuário cadastrado com sucesso!" : "Erro ao cadastrar usuário!";
            } else {
                // Editando usuário existente
                usuario.setId(usuarioEditandoId);
                sucesso = usuarioDAO.atualizar(usuario);
                mensagem = sucesso ? "Usuário atualizado com sucesso!" : "Erro ao atualizar usuário!";
            }
            
            if (sucesso) {
                JOptionPane.showMessageDialog(this, mensagem);
                limparCampos();
                carregarUsuarios();
            } else {
                JOptionPane.showMessageDialog(this, mensagem);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }
    
    private void editarUsuario() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário na tabela para editar!");
            return;
        }
        
        usuarioEditandoId = (Integer) tableModel.getValueAt(selectedRow, 0);
        
        // Preencher campos com dados do usuário selecionado
        txtNome.setText((String) tableModel.getValueAt(selectedRow, 1));
        txtCpf.setText((String) tableModel.getValueAt(selectedRow, 2));
        txtEmail.setText((String) tableModel.getValueAt(selectedRow, 3));
        txtCargo.setText((String) tableModel.getValueAt(selectedRow, 4));
        txtLogin.setText((String) tableModel.getValueAt(selectedRow, 5));
        
        // Buscar senha do banco (não está na tabela por segurança)
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        for (Usuario u : usuarios) {
            if (u.getId() == usuarioEditandoId) {
                txtSenha.setText(u.getSenha());
                break;
            }
        }
        
        // Selecionar perfil
        String perfil = (String) tableModel.getValueAt(selectedRow, 6);
        cbPerfil.setSelectedItem(perfil);
    }
    
    private void excluirUsuario() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para excluir!");
            return;
        }
        
        int usuarioId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String nomeUsuario = (String) tableModel.getValueAt(selectedRow, 1);
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente excluir o usuário " + nomeUsuario + "?", 
            "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION);
            
        if (opcao == JOptionPane.YES_OPTION) {
            if (usuarioDAO.desativar(usuarioId)) {
                JOptionPane.showMessageDialog(this, "Usuário excluído com sucesso!");
                carregarUsuarios();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir usuário!");
            }
        }
    }
    
    private void limparCampos() {
        txtNome.setText("");
        txtCpf.setText("");
        txtEmail.setText("");
        txtCargo.setText("");
        txtLogin.setText("");
        txtSenha.setText("");
        cbPerfil.setSelectedIndex(0);
        usuarioEditandoId = -1; // Reset do modo de edição
    }
    
    private void carregarUsuarios() {
        tableModel.setRowCount(0);
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        
        for (Usuario usuario : usuarios) {
            Object[] row = {
                usuario.getId(),
                usuario.getNomeCompleto(),
                usuario.getCpf(),
                usuario.getEmail(),
                usuario.getCargo(),
                usuario.getLogin(),
                usuario.getPerfil()
            };
            tableModel.addRow(row);
        }
    }
}