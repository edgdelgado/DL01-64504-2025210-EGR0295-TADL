package com.oracle.gestao.dao;

import com.oracle.gestao.model.Equipe;
import com.oracle.gestao.model.Usuario;
import com.oracle.gestao.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipeDAO {
    
    public boolean inserir(Equipe equipe) {
        String sql = "INSERT INTO equipes (nome, descricao) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, equipe.getNome());
            stmt.setString(2, equipe.getDescricao());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Equipe> listarTodas() {
        List<Equipe> equipes = new ArrayList<>();
        String sql = "SELECT * FROM equipes ORDER BY nome";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                equipes.add(criarEquipe(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return equipes;
    }
    
    public boolean atualizar(Equipe equipe) {
        String sql = "UPDATE equipes SET nome = ?, descricao = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, equipe.getNome());
            stmt.setString(2, equipe.getDescricao());
            stmt.setInt(3, equipe.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean adicionarMembro(int equipeId, int usuarioId) {
        String sql = "INSERT IGNORE INTO usuario_equipe (usuario_id, equipe_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, usuarioId);
            stmt.setInt(2, equipeId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean removerMembro(int equipeId, int usuarioId) {
        String sql = "DELETE FROM usuario_equipe WHERE equipe_id = ? AND usuario_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, equipeId);
            stmt.setInt(2, usuarioId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean alocarEquipeProjeto(int projetoId, int equipeId) {
        String sql = "INSERT IGNORE INTO projeto_equipe (projeto_id, equipe_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, projetoId);
            stmt.setInt(2, equipeId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Usuario> listarMembros(int equipeId) {
        List<Usuario> membros = new ArrayList<>();
        String sql = "SELECT u.* FROM usuarios u " +
                    "JOIN usuario_equipe ue ON u.id = ue.usuario_id " +
                    "WHERE ue.equipe_id = ? ORDER BY u.nome_completo";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, equipeId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNomeCompleto(rs.getString("nome_completo"));
                usuario.setCpf(rs.getString("cpf"));
                usuario.setEmail(rs.getString("email"));
                usuario.setCargo(rs.getString("cargo"));
                usuario.setLogin(rs.getString("login"));
                usuario.setPerfil(rs.getString("perfil"));
                membros.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return membros;
    }
    
    private Equipe criarEquipe(ResultSet rs) throws SQLException {
        Equipe equipe = new Equipe();
        equipe.setId(rs.getInt("id"));
        equipe.setNome(rs.getString("nome"));
        equipe.setDescricao(rs.getString("descricao"));
        return equipe;
    }
    
    public boolean excluir(int id) {
        String sql = "DELETE FROM equipes WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean removerAlocacao(int projetoId, int equipeId) {
        String sql = "DELETE FROM projeto_equipe WHERE projeto_id = ? AND equipe_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, projetoId);
            stmt.setInt(2, equipeId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}