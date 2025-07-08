// UsuarioDAO.java
package com.edulivre.dao;

import com.edulivre.model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsuarioDAO extends BaseDAO {
    
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario ORDER BY nome";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(UUID.fromString(rs.getString("id")));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setPerfil(rs.getString("perfil"));
                usuario.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
                
                usuarios.add(usuario);
            }
            
        } catch (SQLException e) {
            handleSQLException(e, "listar todos os usuários");
        }
        
        return usuarios;
    }
    
    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(UUID.fromString(rs.getString("id")));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setSenha(rs.getString("senha"));
                    usuario.setPerfil(rs.getString("perfil"));
                    usuario.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
                    
                    return usuario;
                }
            }
            
        } catch (SQLException e) {
            handleSQLException(e, "buscar usuário por email");
        }
        
        return null;
    }
}