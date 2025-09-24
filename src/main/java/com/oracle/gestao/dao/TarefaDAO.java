package com.oracle.gestao.dao;

import com.oracle.gestao.model.Tarefa;
import com.oracle.gestao.util.DatabaseConnection;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class TarefaDAO {
    
    public boolean inserir(Tarefa tarefa) {
        String sql = "INSERT INTO tarefas (titulo, descricao, projeto_id, responsavel_id, data_inicio_prevista, data_fim_prevista) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setInt(3, tarefa.getProjetoId());
            stmt.setInt(4, tarefa.getResponsavelId());
            stmt.setDate(5, Date.valueOf(tarefa.getDataInicioPrevista()));
            stmt.setDate(6, Date.valueOf(tarefa.getDataFimPrevista()));
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean atualizar(Tarefa tarefa) {
        String sql = "UPDATE tarefas SET titulo = ?, descricao = ?, status = ?, data_inicio_real = ?, data_fim_real = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setString(3, tarefa.getStatus());
            stmt.setDate(4, tarefa.getDataInicioReal() != null ? Date.valueOf(tarefa.getDataInicioReal()) : null);
            stmt.setDate(5, tarefa.getDataFimReal() != null ? Date.valueOf(tarefa.getDataFimReal()) : null);
            stmt.setInt(6, tarefa.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean atualizarCompleta(Tarefa tarefa) {
        String sql = "UPDATE tarefas SET titulo = ?, descricao = ?, projeto_id = ?, responsavel_id = ?, status = ?, data_inicio_prevista = ?, data_fim_prevista = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setInt(3, tarefa.getProjetoId());
            stmt.setInt(4, tarefa.getResponsavelId());
            stmt.setString(5, tarefa.getStatus());
            stmt.setDate(6, Date.valueOf(tarefa.getDataInicioPrevista()));
            stmt.setDate(7, Date.valueOf(tarefa.getDataFimPrevista()));
            stmt.setInt(8, tarefa.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Tarefa> listarTodas() {
        List<Tarefa> tarefas = new ArrayList<>();
        String sql = "SELECT t.*, p.nome as nome_projeto, u.nome_completo as nome_responsavel " +
                    "FROM tarefas t " +
                    "JOIN projetos p ON t.projeto_id = p.id " +
                    "JOIN usuarios u ON t.responsavel_id = u.id " +
                    "ORDER BY t.titulo";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                tarefas.add(criarTarefa(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tarefas;
    }
    
    public List<Tarefa> listarPorResponsavel(int responsavelId) {
        List<Tarefa> tarefas = new ArrayList<>();
        String sql = "SELECT t.*, p.nome as nome_projeto, u.nome_completo as nome_responsavel " +
                    "FROM tarefas t " +
                    "JOIN projetos p ON t.projeto_id = p.id " +
                    "JOIN usuarios u ON t.responsavel_id = u.id " +
                    "WHERE t.responsavel_id = ? ORDER BY t.titulo";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, responsavelId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                tarefas.add(criarTarefa(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tarefas;
    }
    
    private Tarefa criarTarefa(ResultSet rs) throws SQLException {
        Tarefa tarefa = new Tarefa();
        tarefa.setId(rs.getInt("id"));
        tarefa.setTitulo(rs.getString("titulo"));
        tarefa.setDescricao(rs.getString("descricao"));
        tarefa.setProjetoId(rs.getInt("projeto_id"));
        tarefa.setNomeProjeto(rs.getString("nome_projeto"));
        tarefa.setResponsavelId(rs.getInt("responsavel_id"));
        tarefa.setNomeResponsavel(rs.getString("nome_responsavel"));
        tarefa.setStatus(rs.getString("status"));
        
        Date dataInicioPrevista = rs.getDate("data_inicio_prevista");
        if (dataInicioPrevista != null) {
            tarefa.setDataInicioPrevista(dataInicioPrevista.toLocalDate());
        }
        
        Date dataFimPrevista = rs.getDate("data_fim_prevista");
        if (dataFimPrevista != null) {
            tarefa.setDataFimPrevista(dataFimPrevista.toLocalDate());
        }
        
        Date dataInicioReal = rs.getDate("data_inicio_real");
        if (dataInicioReal != null) {
            tarefa.setDataInicioReal(dataInicioReal.toLocalDate());
        }
        
        Date dataFimReal = rs.getDate("data_fim_real");
        if (dataFimReal != null) {
            tarefa.setDataFimReal(dataFimReal.toLocalDate());
        }
        
        return tarefa;
    }
    
    public boolean excluir(int id) {
        String sql = "DELETE FROM tarefas WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}