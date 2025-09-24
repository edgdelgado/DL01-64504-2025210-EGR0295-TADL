package com.oracle.gestao.view;

import com.oracle.gestao.model.Usuario;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private Usuario usuarioLogado;
    private JTabbedPane tabbedPane;
    
    public MainFrame(Usuario usuario) {
        this.usuarioLogado = usuario;
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Sistema de Gestão de Projetos - " + usuarioLogado.getNomeCompleto());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Menu
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menuCadastros = new JMenu("Cadastros");
        if (usuarioLogado.getPerfil().equals("administrador")) {
            JMenuItem itemUsuarios = new JMenuItem("Usuários");
            itemUsuarios.addActionListener(e -> abrirCadastroUsuarios());
            menuCadastros.add(itemUsuarios);
        }
        
        if (!usuarioLogado.getPerfil().equals("colaborador")) {
            JMenuItem itemProjetos = new JMenuItem("Projetos");
            itemProjetos.addActionListener(e -> abrirCadastroProjetos());
            menuCadastros.add(itemProjetos);
            
            JMenuItem itemEquipes = new JMenuItem("Equipes");
            itemEquipes.addActionListener(e -> abrirCadastroEquipes());
            menuCadastros.add(itemEquipes);
        }
        
        JMenuItem itemTarefas = new JMenuItem("Tarefas");
        itemTarefas.addActionListener(e -> abrirCadastroTarefas());
        menuCadastros.add(itemTarefas);
        
        menuBar.add(menuCadastros);
        
        JMenu menuRelatorios = new JMenu("Relatórios");
        JMenuItem itemDashboard = new JMenuItem("Dashboard");
        itemDashboard.addActionListener(e -> abrirDashboard());
        menuRelatorios.add(itemDashboard);
        
        menuBar.add(menuRelatorios);
        
        JMenu menuSistema = new JMenu("Sistema");
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> sair());
        menuSistema.add(itemSair);
        
        menuBar.add(menuSistema);
        
        setJMenuBar(menuBar);
        
        // Painel principal
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);
        
        // Painel de boas-vindas
        JPanel painelInicial = new JPanel(new BorderLayout());
        JLabel lblBoasVindas = new JLabel("<html><h1>Bem-vindo, " + usuarioLogado.getNomeCompleto() + "!</h1>" +
                                         "<p>Perfil: " + usuarioLogado.getPerfil().toUpperCase() + "</p></html>");
        lblBoasVindas.setHorizontalAlignment(SwingConstants.CENTER);
        painelInicial.add(lblBoasVindas, BorderLayout.CENTER);
        
        tabbedPane.addTab("Início", painelInicial);
    }
    
    private void abrirCadastroUsuarios() {
        abrirOuFocarAba("Usuários", () -> new UsuarioPanel());
    }
    
    private void abrirCadastroProjetos() {
        abrirOuFocarAba("Projetos", () -> new ProjetoPanel());
    }
    
    private void abrirCadastroEquipes() {
        abrirOuFocarAba("Equipes", () -> new EquipePanel());
    }
    
    private void abrirCadastroTarefas() {
        abrirOuFocarAba("Tarefas", () -> new TarefaPanel(usuarioLogado));
    }
    
    private void abrirDashboard() {
        abrirOuFocarAba("Dashboard", () -> new DashboardPanel());
    }
    
    private void abrirOuFocarAba(String titulo, java.util.function.Supplier<JPanel> criarPanel) {
        // Procurar se a aba já existe
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (tabbedPane.getTitleAt(i).equals(titulo)) {
                tabbedPane.setSelectedIndex(i);
                return;
            }
        }
        
        // Se não existe, criar nova aba
        JPanel panel = criarPanel.get();
        tabbedPane.addTab(titulo, panel);
        tabbedPane.setSelectedComponent(panel);
    }
    
    private void sair() {
        int opcao = JOptionPane.showConfirmDialog(this, "Deseja realmente sair?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcao == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}