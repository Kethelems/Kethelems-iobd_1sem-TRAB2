package com.edulivre;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import com.edulivre.dao.ConteudoDAO;
import com.edulivre.model.Conteudo;

public class ConteudoUtils {
    
public static void inserirConteudoArquivoClasspath(
    String recurso,
    String titulo,
    String tipo,
    String descricao,
    UUID cursoId,
    ConteudoDAO conteudoDAO
) {
    try (InputStream is = Main.class.getResourceAsStream(recurso)) {
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