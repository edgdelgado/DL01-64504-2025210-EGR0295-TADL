package com.oracle.gestao.view;

import com.oracle.gestao.controller.LoginController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private LoginController controller;
    
    public LoginFrame() {
        controller = new LoginController(this);
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Sistema de Gestão de Projetos - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Título
        JLabel lblTitulo = new JLabel("Sistema de Gestão de Projetos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitulo, gbc);
        
        // Login
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Login:"), gbc);
        
        txtLogin = new JTextField(15);
        gbc.gridx = 1;
        add(txtLogin, gbc);
        
        // Senha
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Senha:"), gbc);
        
        txtSenha = new JPasswordField(15);
        gbc.gridx = 1;
        add(txtSenha, gbc);
        
        // Botão Login
        JButton btnLogin = new JButton("Entrar");
        btnLogin.addActionListener(this::btnLoginClick);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(btnLogin, gbc);
        
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void btnLoginClick(ActionEvent e) {
        String login = txtLogin.getText();
        String senha = new String(txtSenha.getPassword());
        
        if (login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }
        
        controller.autenticar(login, senha);
    }
    
    public void limparCampos() {
        txtLogin.setText("");
        txtSenha.setText("");
    }
    
    public void exibirMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem);
    }
}