package com.edulivre.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.edulivre.model.Curso;

public class CursoDAO extends BaseDAO {

    public List<String> listarCursosComEstatisticas() {
        List<String> resultados = new ArrayList<>();
        String sql = """
            SELECT 
                c.titulo,
                c.descricao,
                COALESCE((c.avaliacao->>'media')::decimal, 0) as media_avaliacao,
                COUNT(m.id) as total_alunos
            FROM curso c
            LEFT JOIN matricula m ON c.id = m.curso_id
            GROUP BY c.id, c.titulo, c.descricao, c.avaliacao
            ORDER BY c.titulo
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String resultado = String.format(
                    "Curso: %s | Descrição: %s | Média: %.1f | Alunos: %d",
                    rs.getString("titulo"),
                    rs.getString("descricao"),
                    rs.getDouble("media_avaliacao"),
                    rs.getInt("total_alunos")
                );
                resultados.add(resultado);
            }

        } catch (SQLException e) {
            handleSQLException(e, "listar cursos com estatísticas");
        }

        return resultados;
    }

    // Listar todos os cursos (retornando objetos Curso)
    public List<Curso> listarTodos() {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT id, titulo, descricao, data_criacao, avaliacao, professor_id FROM curso ORDER BY titulo";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Curso curso = new Curso();
                curso.setId((UUID) rs.getObject("id"));
                curso.setTitulo(rs.getString("titulo"));
                curso.setDescricao(rs.getString("descricao"));
                curso.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
                curso.setAvaliacao(rs.getString("avaliacao"));
                curso.setProfessorId((UUID) rs.getObject("professor_id"));

                cursos.add(curso);
            }

        } catch (SQLException e) {
            handleSQLException(e, "listar todos cursos");
        }

        return cursos;
    }

    // d) Adicionar comentário no JSONB de avaliação do curso
    public boolean adicionarComentario(UUID cursoId, UUID usuarioId, int nota, String comentario) {
        String sqlSelect = "SELECT avaliacao FROM curso WHERE id = ?";
        // Adicione o cast ::jsonb aqui
        String sqlUpdate = "UPDATE curso SET avaliacao = ?::jsonb WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {

            stmtSelect.setObject(1, cursoId);
            ResultSet rs = stmtSelect.executeQuery();

            if (rs.next()) {
                String avaliacaoJson = rs.getString("avaliacao");

                String novoComentario = String.format(
                    "{\"usuario_id\":\"%s\",\"nota\":%d,\"comentario\":\"%s\",\"data\":\"%s\"}",
                    usuarioId.toString(),
                    nota,
                    comentario.replace("\"", "\\\""),
                    java.time.LocalDate.now().toString()
                );

                String novaAvaliacaoJson;

                if (avaliacaoJson == null || avaliacaoJson.trim().isEmpty()) {
                    novaAvaliacaoJson = String.format(
                        "{\"media\": %d, \"comentarios\": [%s]}",
                        nota, novoComentario
                    );
                } else {
                    int pos = avaliacaoJson.lastIndexOf("]");
                    if (pos == -1) {
                        novaAvaliacaoJson = String.format(
                            "{\"media\": %d, \"comentarios\": [%s]}",
                            nota, novoComentario
                        );
                    } else {
                        String prefixo = avaliacaoJson.substring(0, pos);
                        String sufixo = avaliacaoJson.substring(pos);
                        if (prefixo.trim().endsWith("[")) {
                            novaAvaliacaoJson = prefixo + novoComentario + sufixo;
                        } else {
                            novaAvaliacaoJson = prefixo + "," + novoComentario + sufixo;
                        }
                    }
                }

                try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                    stmtUpdate.setString(1, novaAvaliacaoJson);
                    stmtUpdate.setObject(2, cursoId);
                    int rows = stmtUpdate.executeUpdate();
                    return rows > 0;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    protected void handleSQLException(SQLException e, String contexto) {
        System.err.println("Erro no contexto: " + contexto);
        e.printStackTrace();

        
    }

   
}
