package com.edulivre;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import com.edulivre.dao.ConteudoDAO;
import com.edulivre.dao.CursoDAO;
import com.edulivre.dao.MatriculaDAO;
import com.edulivre.dao.UsuarioDAO;
import com.edulivre.model.Conteudo;
import com.edulivre.model.Curso;
import com.edulivre.model.Usuario;

public class Main {
    public static void main(String[] args) {
        CursoDAO cursoDAO = new CursoDAO();
        ConteudoDAO conteudoDAO = new ConteudoDAO();
        MatriculaDAO matriculaDAO = new MatriculaDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        System.out.println(" ***SISTEMA EDULIVRE*** \n");

        System.out.println("1. CURSOS COM ESTATÍSTICAS:");
        System.out.println("-".repeat(50));
        List<String> cursosEstatisticas = cursoDAO.listarCursosComEstatisticas();
        cursosEstatisticas.forEach(System.out::println);

        System.out.println("\n2. CONTEÚDOS DO PRIMEIRO CURSO:");
        System.out.println("-".repeat(50));
        List<Curso> cursos = cursoDAO.listarTodos();
        if (!cursos.isEmpty()) {
            UUID primeiroCursoId = cursos.get(0).getId();
            List<String> conteudos = conteudoDAO.buscarConteudosPorCurso(primeiroCursoId);
            conteudos.forEach(System.out::println);

            inserirConteudoArquivoClasspath(
                "/imagem-teste.jpg",
                "Imagem Exemplo",
                "imagem",
                "Imagem de teste enviada via Java",
                primeiroCursoId,
                conteudoDAO
            );

            inserirConteudoArquivoClasspath(
                "/exemplo.pdf",
                "PDF Exemplo",
                "pdf",
                "Arquivo PDF enviado via Java",
                primeiroCursoId,
                conteudoDAO
            );

            inserirConteudoArquivoClasspath(
                "/slides.pptx",
                "Slides Aula 2",
                "slide",
                "Material em slide",
                primeiroCursoId,
                conteudoDAO
            );
        }

        System.out.println("\n3. TESTANDO NOVA MATRÍCULA:");
        System.out.println("-".repeat(50));
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        if (!usuarios.isEmpty() && !cursos.isEmpty()) {
            Usuario aluno = usuarios.stream()
                .filter(u -> "aluno".equals(u.getPerfil()))
                .findFirst()
                .orElse(null);

            if (aluno != null) {
                boolean sucesso = matriculaDAO.inserirMatricula(
                    aluno.getId(),
                    cursos.get(0).getId()
                );
                System.out.println("Resultado da matrícula: " +
                    (sucesso ? "DEU BOM" : "DEU RUIM"));
            }
        }

        System.out.println("\n4. TESTANDO DE NOVO COMENTÁRIO:");
        System.out.println("-".repeat(50));
        if (!cursos.isEmpty() && !usuarios.isEmpty()) {
            boolean comentarioAdicionado = cursoDAO.adicionarComentario(
                cursos.get(0).getId(),
                usuarios.get(0).getId(),
                5,
                "Curso topp, muito bom!"
            );
            System.out.println("Resultado do comentário: " +
                (comentarioAdicionado ? "DEU BOM" : "DEU RUIM"));
        }

        System.out.println("\n=== FIM DOS TESTES ===");
    }


    private static void inserirConteudoArquivo(
        String caminho,
        String titulo,
        String tipo,
        String descricao,
        UUID cursoId,
        ConteudoDAO conteudoDAO
    ) {
        File arquivo = new File(caminho);
        if (arquivo.exists()) {
            try (FileInputStream fis = new FileInputStream(arquivo)) {
                byte[] dados = fis.readAllBytes();

                Conteudo conteudo = new Conteudo();
                conteudo.setCursoId(cursoId);
                conteudo.setTitulo(titulo);
                conteudo.setDescricao(descricao);
                conteudo.setTipo(tipo);
                conteudo.setArquivo(dados);

                boolean inserido = conteudoDAO.inserir(conteudo);
                System.out.printf("\nInserção do conteúdo '%s': %s\n", titulo, (inserido ? "DEU BOM" : "DEU RUIM"));
            } catch (IOException e) {
                System.err.println("Erro ao ler o arquivo " + caminho + ": " + e.getMessage());
            }
        } else {
            System.out.println("Arquivo não encontrado: " + arquivo.getAbsolutePath());
        }
    }

    private static void inserirConteudoArquivoClasspath(
        String recurso,
        String titulo,
        String tipo,
        String descricao,
        UUID cursoId,
        ConteudoDAO conteudoDAO
    ) {
        if (conteudoDAO.existeConteudo(titulo, tipo, cursoId)) {
        System.out.println("Conteúdo já existente, não será inserido: " + titulo);
        return;
        }

        try (InputStream is = ConteudoUtils.class.getResourceAsStream(recurso)) {
            if (is == null) {
                System.out.println("Arquivo não encontrado no classpath: " + recurso);
                return;
            }
            byte[] dados = is.readAllBytes();

            Conteudo conteudo = new Conteudo();
            conteudo.setCursoId(cursoId);
            conteudo.setTitulo(titulo);
            conteudo.setDescricao(descricao);
            conteudo.setTipo(tipo);
            conteudo.setArquivo(dados);

            boolean inserido = conteudoDAO.inserir(conteudo);
            System.out.printf("\nInserção do conteúdo '%s': %s\n", titulo, (inserido ? "DEU BOM" : "DEU RUIM"));
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo " + recurso + ": " + e.getMessage());
        }
    }
}
