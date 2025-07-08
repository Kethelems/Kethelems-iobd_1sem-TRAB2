package com.edulivre.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Conteudo {
    private Long id;
    private UUID cursoId;
    private String titulo;
    private String descricao;
    private String tipo;
    private byte[] arquivo;
    private LocalDateTime dataCriacao;

    public Conteudo() {}

    public Conteudo(UUID cursoId, String titulo, String descricao, String tipo, byte[] arquivo) {
        this.cursoId = cursoId;
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
        this.arquivo = arquivo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UUID getCursoId() { return cursoId; }
    public void setCursoId(UUID cursoId) { this.cursoId = cursoId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public byte[] getArquivo() { return arquivo; }
    public void setArquivo(byte[] arquivo) { this.arquivo = arquivo; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    @Override
public String toString() {
    return String.format(
        "Conteudo{id=%s, cursoId=%s, titulo='%s', descricao='%s', tipo='%s', arquivo=%s, dataCriacao=%s}",
        id,
        cursoId,
        titulo,
        descricao,
        tipo,
        arquivo != null ? (arquivo.length + " bytes") : "null",
        dataCriacao != null ? dataCriacao.toString() : "null"
    );
}

}
