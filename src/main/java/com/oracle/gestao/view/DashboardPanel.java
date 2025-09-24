package com.oracle.gestao.view;

import com.oracle.gestao.dao.ProjetoDAO;
import com.oracle.gestao.dao.TarefaDAO;
import com.oracle.gestao.dao.UsuarioDAO;
import com.oracle.gestao.dao.EquipeDAO;
import com.oracle.gestao.model.Projeto;
import com.oracle.gestao.model.Tarefa;
import com.oracle.gestao.model.Usuario;
import com.oracle.gestao.model.Equipe;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class DashboardPanel extends JPanel {
    private ProjetoDAO projetoDAO;
    private TarefaDAO tarefaDAO;
    private UsuarioDAO usuarioDAO;
    private EquipeDAO equipeDAO;
    
    public DashboardPanel() {
        projetoDAO = new ProjetoDAO();
        tarefaDAO = new TarefaDAO();
        usuarioDAO = new UsuarioDAO();
        equipeDAO = new EquipeDAO();
        initComponents();
    }
    
    private JTabbedPane tabbedPane;
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Botão de atualizar
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAtualizar = new JButton("Atualizar Dashboard");
        btnAtualizar.addActionListener(e -> atualizarDashboard());
        painelTopo.add(btnAtualizar);
        add(painelTopo, BorderLayout.NORTH);
        
        tabbedPane = new JTabbedPane();
        
        // Aba Resumo de Projetos
        JPanel painelResumo = criarPainelResumoProjetos();
        tabbedPane.addTab("Resumo Projetos", painelResumo);
        
        // Aba Desempenho Colaboradores
        JPanel painelDesempenho = criarPainelDesempenho();
        tabbedPane.addTab("Desempenho", painelDesempenho);
        
        // Aba Projetos com Risco
        JPanel painelRisco = criarPainelRisco();
        tabbedPane.addTab("Projetos em Risco", painelRisco);
        
        // Aba Equipes
        JPanel painelEquipes = criarPainelEquipes();
        tabbedPane.addTab("Equipes", painelEquipes);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel criarPainelResumoProjetos() {
        JPanel painel = new JPanel(new BorderLayout());
        
        // Estatísticas gerais
        JPanel painelStats = new JPanel(new GridLayout(1, 5, 10, 10));
        painelStats.setBorder(BorderFactory.createTitledBorder("Estatísticas Gerais"));
        
        List<Projeto> projetos = projetoDAO.listarTodos();
        
        int totalProjetos = projetos.size();
        int planejados = 0, emAndamento = 0, concluidos = 0, cancelados = 0;
        
        for (Projeto projeto : projetos) {
            switch (projeto.getStatus()) {
                case "planejado": planejados++; break;
                case "em_andamento": emAndamento++; break;
                case "concluido": concluidos++; break;
                case "cancelado": cancelados++; break;
            }
        }
        
        painelStats.add(new JLabel("Total de Projetos: " + totalProjetos));
        painelStats.add(new JLabel("Planejados: " + planejados));
        painelStats.add(new JLabel("Em Andamento: " + emAndamento));
        painelStats.add(new JLabel("Concluídos: " + concluidos));
        painelStats.add(new JLabel("Cancelados: " + cancelados));
        
        painel.add(painelStats, BorderLayout.NORTH);
        
        // Tabela de projetos
        String[] colunas = {"Nome", "Status", "Gerente", "Data Início", "Data Fim Prevista"};
        DefaultTableModel tableModel = new DefaultTableModel(colunas, 0);
        JTable table = new JTable(tableModel);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (Projeto projeto : projetos) {
            Object[] row = {
                projeto.getNome(),
                projeto.getStatus(),
                projeto.getNomeGerente(),
                projeto.getDataInicio().format(formatter),
                projeto.getDataFimPrevista().format(formatter)
            };
            tableModel.addRow(row);
        }
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Todos os Projetos"));
        painel.add(scrollPane, BorderLayout.CENTER);
        
        return painel;
    }
    
    private JPanel criarPainelDesempenho() {
        JPanel painel = new JPanel(new BorderLayout());
        
        // Calcular desempenho por colaborador
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        List<Tarefa> todasTarefas = tarefaDAO.listarTodas();
        
        Map<Integer, Integer> tarefasAtribuidas = new HashMap<>();
        Map<Integer, Integer> tarefasConcluidas = new HashMap<>();
        
        for (Tarefa tarefa : todasTarefas) {
            int responsavelId = tarefa.getResponsavelId();
            tarefasAtribuidas.put(responsavelId, tarefasAtribuidas.getOrDefault(responsavelId, 0) + 1);
            
            if ("concluida".equals(tarefa.getStatus())) {
                tarefasConcluidas.put(responsavelId, tarefasConcluidas.getOrDefault(responsavelId, 0) + 1);
            }
        }
        
        String[] colunas = {"Colaborador", "Cargo", "Tarefas Atribuídas", "Tarefas Concluídas", "% Conclusão"};
        DefaultTableModel tableModel = new DefaultTableModel(colunas, 0);
        JTable table = new JTable(tableModel);
        
        for (Usuario usuario : usuarios) {
            int atribuidas = tarefasAtribuidas.getOrDefault(usuario.getId(), 0);
            int concluidas = tarefasConcluidas.getOrDefault(usuario.getId(), 0);
            double percentual = atribuidas > 0 ? (concluidas * 100.0 / atribuidas) : 0;
            
            Object[] row = {
                usuario.getNomeCompleto(),
                usuario.getCargo(),
                atribuidas,
                concluidas,
                String.format("%.1f%%", percentual)
            };
            tableModel.addRow(row);
        }
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Desempenho dos Colaboradores"));
        painel.add(scrollPane, BorderLayout.CENTER);
        
        return painel;
    }
    
    private JPanel criarPainelRisco() {
        JPanel painel = new JPanel(new BorderLayout());
        
        List<Projeto> projetosRisco = projetoDAO.listarComRiscoAtraso();
        
        JLabel lblInfo = new JLabel("<html><h3>Projetos com Risco de Atraso</h3>" +
                                   "<p>Projetos em andamento ou planejados com data de término prevista já ultrapassada.</p></html>");
        painel.add(lblInfo, BorderLayout.NORTH);
        
        String[] colunas = {"Nome", "Status", "Gerente", "Data Fim Prevista", "Dias em Atraso"};
        DefaultTableModel tableModel = new DefaultTableModel(colunas, 0);
        JTable table = new JTable(tableModel);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (Projeto projeto : projetosRisco) {
            long diasAtraso = java.time.temporal.ChronoUnit.DAYS.between(
                projeto.getDataFimPrevista(), 
                java.time.LocalDate.now()
            );
            
            Object[] row = {
                projeto.getNome(),
                projeto.getStatus(),
                projeto.getNomeGerente(),
                projeto.getDataFimPrevista().format(formatter),
                diasAtraso + " dias"
            };
            tableModel.addRow(row);
        }
        
        JScrollPane scrollPane = new JScrollPane(table);
        painel.add(scrollPane, BorderLayout.CENTER);
        
        if (projetosRisco.isEmpty()) {
            JLabel lblSemRisco = new JLabel("Nenhum projeto com risco de atraso encontrado!");
            lblSemRisco.setHorizontalAlignment(SwingConstants.CENTER);
            lblSemRisco.setFont(new Font("Arial", Font.BOLD, 16));
            lblSemRisco.setForeground(Color.GREEN);
            painel.add(lblSemRisco, BorderLayout.CENTER);
        }
        
        return painel;
    }
    
    private JPanel criarPainelEquipes() {
        JPanel painel = new JPanel(new BorderLayout());
        
        JLabel lblInfo = new JLabel("<html><h3>Resumo das Equipes</h3></html>");
        painel.add(lblInfo, BorderLayout.NORTH);
        
        List<Equipe> equipes = equipeDAO.listarTodas();
        
        String[] colunas = {"Equipe", "Descrição", "Nº Membros", "Projetos Alocados"};
        DefaultTableModel tableModel = new DefaultTableModel(colunas, 0);
        JTable table = new JTable(tableModel);
        
        for (Equipe equipe : equipes) {
            List<Usuario> membros = equipeDAO.listarMembros(equipe.getId());
            int numMembros = membros.size();
            
            // Contar projetos (seria necessário um método no EquipeDAO)
            String projetos = "N/A";
            
            Object[] row = {
                equipe.getNome(),
                equipe.getDescricao(),
                numMembros,
                projetos
            };
            tableModel.addRow(row);
        }
        
        JScrollPane scrollPane = new JScrollPane(table);
        painel.add(scrollPane, BorderLayout.CENTER);
        
        if (equipes.isEmpty()) {
            JLabel lblSemEquipes = new JLabel("Nenhuma equipe cadastrada!");
            lblSemEquipes.setHorizontalAlignment(SwingConstants.CENTER);
            lblSemEquipes.setFont(new Font("Arial", Font.BOLD, 16));
            painel.add(lblSemEquipes, BorderLayout.CENTER);
        }
        
        return painel;
    }
    
    private void atualizarDashboard() {
        // Remover todas as abas
        tabbedPane.removeAll();
        
        // Recriar todas as abas com dados atualizados
        JPanel painelResumo = criarPainelResumoProjetos();
        tabbedPane.addTab("Resumo Projetos", painelResumo);
        
        JPanel painelDesempenho = criarPainelDesempenho();
        tabbedPane.addTab("Desempenho", painelDesempenho);
        
        JPanel painelRisco = criarPainelRisco();
        tabbedPane.addTab("Projetos em Risco", painelRisco);
        
        JPanel painelEquipes = criarPainelEquipes();
        tabbedPane.addTab("Equipes", painelEquipes);
        
        // Revalidar e repintar
        tabbedPane.revalidate();
        tabbedPane.repaint();
    }
}