
package com.edulivre.dao;

import com.edulivre.model.Matricula;
import java.sql.*;
import java.util.UUID;

public class MatriculaDAO extends BaseDAO {
    
    public boolean inserirMatricula(UUID usuarioId, UUID cursoId) {
       
        if (matriculaExiste(usuarioId, cursoId)) {
            System.out.println("Usuário já está matriculado neste curso!");
            return false;
        }
        
        if (!usuarioValido(usuarioId)) {
            System.out.println("Usuário inválido ou não é aluno!");
            return false;
        }

        if (!cursoExiste(cursoId)) {
            System.out.println("Curso não encontrado!");
            return false;
        }
        
        String sql = "INSERT INTO matricula (usuario_id, curso_id) VALUES (?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setObject(1, usuarioId);
            stmt.setObject(2, cursoId);
            
            int rows = stmt.executeUpdate();
            
            if (rows > 0) {
                System.out.println("Matrícula realizada com sucesso!");
                return true;
            }
            
        } catch (SQLException e) {
            handleSQLException(e, "inserir matrícula");
        }
        
        return false;
    }
    
    private boolean matriculaExiste(UUID usuarioId, UUID cursoId) {
        String sql = "SELECT COUNT(*) FROM matricula WHERE usuario_id = ? AND curso_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setObject(1, usuarioId);
            stmt.setObject(2, cursoId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            handleSQLException(e, "verificar matrícula existente");
        }
        
        return false;
    }
    
    private boolean usuarioValido(UUID usuarioId) {
        String sql = "SELECT perfil FROM usuario WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setObject(1, usuarioId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String perfil = rs.getString("perfil");
                    return "aluno".equals(perfil) || "admin".equals(perfil);
                }
            }
            
        } catch (SQLException e) {
            handleSQLException(e, "verificar usuário válido");
        }
        
        return false;
    }
    
    private boolean cursoExiste(UUID cursoId) {
        String sql = "SELECT COUNT(*) FROM curso WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setObject(1, cursoId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            handleSQLException(e, "verificar curso existente");
        }
        
        return false;
    }
}