package com.edulivre.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.edulivre.model.Conteudo;

public class ConteudoDAO extends BaseDAO {
    
    public List<String> buscarConteudosPorCurso(UUID cursoId) {
        List<String> resultados = new ArrayList<>();
        String sql = """
            SELECT 
                c.titulo,
                c.descricao,
                c.tipo,
                CASE 
                    WHEN c.arquivo IS NOT NULL THEN octet_length(c.arquivo)
                    ELSE 0
                END as tamanho_arquivo
            FROM conteudo c
            WHERE c.curso_id = ?
            ORDER BY c.data_criacao
            """;
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setObject(1, cursoId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String resultado = String.format(
                        "Título: %s | Tipo: %s | Tamanho: %d bytes | Descrição: %s",
                        rs.getString("titulo"),
                        rs.getString("tipo"),
                        rs.getLong("tamanho_arquivo"),
                        rs.getString("descricao")
                    );
                    resultados.add(resultado);
                }
            }
            
        } catch (SQLException e) {
            handleSQLException(e, "buscar conteúdos por curso");
        }
        
        return resultados;
    }
    
    public boolean inserir(Conteudo conteudo) {
        String sql = """
            INSERT INTO conteudo (curso_id, titulo, descricao, tipo, arquivo)
            VALUES (?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setObject(1, conteudo.getCursoId());
            stmt.setString(2, conteudo.getTitulo());
            stmt.setString(3, conteudo.getDescricao());
            stmt.setString(4, conteudo.getTipo());
            stmt.setBytes(5, conteudo.getArquivo());
            
            int rows = stmt.executeUpdate();
            
            if (rows > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        conteudo.setId(keys.getLong(1));
                    }
                }
                return true;
            }
            
        } catch (SQLException e) {
            handleSQLException(e, "inserir conteúdo");
        }
        
        return false;
    }
     public boolean existeConteudo(String titulo, String tipo, UUID cursoId) {
    String sql = "SELECT COUNT(*) FROM edulivre.conteudo WHERE titulo = ? AND tipo = ? AND curso_id = ?";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, titulo);
        stmt.setString(2, tipo);
        stmt.setObject(3, cursoId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

}
