package com.edulivre.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Curso {
    private UUID id;
    private String titulo;
    private String descricao;
    private LocalDateTime dataCriacao;
    private String avaliacao; 
    private UUID professorId;

    public Curso() {}
    
    public Curso(String titulo, String descricao, UUID professorId) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.professorId = professorId;
    }
    
  
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public String getAvaliacao() { return avaliacao; }
    public void setAvaliacao(String avaliacao) { this.avaliacao = avaliacao; }
    
    public UUID getProfessorId() { return professorId; }
    public void setProfessorId(UUID professorId) { this.professorId = professorId; }

    @Override
public String toString() {
    return String.format(
        "Curso{id=%s, titulo='%s', descricao='%s', dataCriacao=%s, avaliacao='%s', professorId=%s}",
        id,
        titulo,
        descricao,
        dataCriacao != null ? dataCriacao.toString() : "null",
        avaliacao,
        professorId
    );
}

}
