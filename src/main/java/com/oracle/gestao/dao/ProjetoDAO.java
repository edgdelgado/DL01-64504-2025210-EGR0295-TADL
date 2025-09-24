package com.oracle.gestao.dao;

import com.oracle.gestao.model.Projeto;
import com.oracle.gestao.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjetoDAO {
    
    public boolean inserir(Projeto projeto) {
        String sql = "INSERT INTO projetos (nome, descricao, data_inicio, data_fim_prevista, gerente_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, projeto.getNome());
            stmt.setString(2, projeto.getDescricao());
            stmt.setDate(3, Date.valueOf(projeto.getDataInicio()));
            stmt.setDate(4, Date.valueOf(projeto.getDataFimPrevista()));
            stmt.setInt(5, projeto.getGerenteId());
            
            int result = stmt.executeUpdate();
            if (result > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    projeto.setId(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean atualizar(Projeto projeto) {
        String sql = "UPDATE projetos SET nome = ?, descricao = ?, data_inicio = ?, data_fim_prevista = ?, status = ?, gerente_id = ?, data_fim_real = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, projeto.getNome());
            stmt.setString(2, projeto.getDescricao());
            stmt.setDate(3, Date.valueOf(projeto.getDataInicio()));
            stmt.setDate(4, Date.valueOf(projeto.getDataFimPrevista()));
            stmt.setString(5, projeto.getStatus());
            stmt.setInt(6, projeto.getGerenteId());
            stmt.setDate(7, projeto.getDataFimReal() != null ? Date.valueOf(projeto.getDataFimReal()) : null);
            stmt.setInt(8, projeto.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Projeto> listarTodos() {
        List<Projeto> projetos = new ArrayList<>();
        String sql = "SELECT p.*, u.nome_completo as nome_gerente FROM projetos p " +
                    "JOIN usuarios u ON p.gerente_id = u.id ORDER BY p.nome";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                projetos.add(criarProjeto(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projetos;
    }
    
    public List<Projeto> listarComRiscoAtraso() {
        List<Projeto> projetos = new ArrayList<>();
        String sql = "SELECT p.*, u.nome_completo as nome_gerente FROM projetos p " +
                    "JOIN usuarios u ON p.gerente_id = u.id " +
                    "WHERE p.status IN ('planejado', 'em_andamento') AND p.data_fim_prevista < CURDATE()";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                projetos.add(criarProjeto(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projetos;
    }
    
    public List<String> listarEquipesProjeto(int projetoId) {
        List<String> equipes = new ArrayList<>();
        String sql = "SELECT e.nome FROM equipes e " +
                    "JOIN projeto_equipe pe ON e.id = pe.equipe_id " +
                    "WHERE pe.projeto_id = ? ORDER BY e.nome";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, projetoId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                equipes.add(rs.getString("nome"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return equipes;
    }
    
    private Projeto criarProjeto(ResultSet rs) throws SQLException {
        Projeto projeto = new Projeto();
        projeto.setId(rs.getInt("id"));
        projeto.setNome(rs.getString("nome"));
        projeto.setDescricao(rs.getString("descricao"));
        projeto.setDataInicio(rs.getDate("data_inicio").toLocalDate());
        projeto.setDataFimPrevista(rs.getDate("data_fim_prevista").toLocalDate());
        
        Date dataFimReal = rs.getDate("data_fim_real");
        if (dataFimReal != null) {
            projeto.setDataFimReal(dataFimReal.toLocalDate());
        }
        
        projeto.setStatus(rs.getString("status"));
        projeto.setGerenteId(rs.getInt("gerente_id"));
        projeto.setNomeGerente(rs.getString("nome_gerente"));
        return projeto;
    }
    
    public boolean excluir(int id) {
        String sql = "DELETE FROM projetos WHERE id = ?";
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