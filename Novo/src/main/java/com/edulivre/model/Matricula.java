package com.edulivre.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Matricula {
    private Long id;
    private UUID usuarioId;
    private UUID cursoId;
    private LocalDateTime dataMatricula;

    public Matricula() {}
    
    public Matricula(UUID usuarioId, UUID cursoId) {
        this.usuarioId = usuarioId;
        this.cursoId = cursoId;
    }
  
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }
    
    public UUID getCursoId() { return cursoId; }
    public void setCursoId(UUID cursoId) { this.cursoId = cursoId; }
    
    public LocalDateTime getDataMatricula() { return dataMatricula; }
    public void setDataMatricula(LocalDateTime dataMatricula) { this.dataMatricula = dataMatricula; }

@Override
public String toString() {
    return String.format("Matricula{id=%s, usuarioId=%s, cursoId=%s, dataMatricula=%s}", 
        id, 
        usuarioId, 
        cursoId, 
        dataMatricula != null ? dataMatricula.toString() : "null"
    );
}


}
