package com.edulivre.dao;

import com.edulivre.config.ConexaoPostgreSQL; 
import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseDAO {
    
    private ConexaoPostgreSQL conexao = new ConexaoPostgreSQL(); 
    
    protected Connection getConnection() throws SQLException {
        return conexao.getConexao(); 
    }
    
    protected void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
    
    protected void handleSQLException(SQLException e, String operation) {
        System.err.println("Erro na operação " + operation + ": " + e.getMessage());
        e.printStackTrace();
    }
}