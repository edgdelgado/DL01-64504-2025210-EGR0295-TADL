package com.oracle.gestao.controller;

import com.oracle.gestao.dao.UsuarioDAO;
import com.oracle.gestao.model.Usuario;
import com.oracle.gestao.view.LoginFrame;
import com.oracle.gestao.view.MainFrame;

public class LoginController {
    private LoginFrame loginFrame;
    private UsuarioDAO usuarioDAO;
    
    public LoginController(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        this.usuarioDAO = new UsuarioDAO();
    }
    
    public void autenticar(String login, String senha) {
        try {
            Usuario usuario = usuarioDAO.autenticar(login, senha);
            
            if (usuario != null) {
                loginFrame.dispose();
                MainFrame mainFrame = new MainFrame(usuario);
                mainFrame.setVisible(true);
            } else {
                loginFrame.exibirMensagem("Login ou senha inválidos!");
                loginFrame.limparCampos();
            }
        } catch (Exception e) {
            loginFrame.exibirMensagem("Erro ao conectar com o banco de dados: " + e.getMessage());
        }
    }
}