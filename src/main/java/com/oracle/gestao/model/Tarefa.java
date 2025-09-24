package com.oracle.gestao.model;

import java.time.LocalDate;

public class Tarefa {
    private int id;
    private String titulo;
    private String descricao;
    private int projetoId;
    private String nomeProjeto;
    private int responsavelId;
    private String nomeResponsavel;
    private String status;
    private LocalDate dataInicioPrevista;
    private LocalDate dataFimPrevista;
    private LocalDate dataInicioReal;
    private LocalDate dataFimReal;
    
    public Tarefa() {}
    
    public Tarefa(String titulo, String descricao, int projetoId, int responsavelId,
                  LocalDate dataInicioPrevista, LocalDate dataFimPrevista) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.projetoId = projetoId;
        this.responsavelId = responsavelId;
        this.dataInicioPrevista = dataInicioPrevista;
        this.dataFimPrevista = dataFimPrevista;
        this.status = "pendente";
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public int getProjetoId() { return projetoId; }
    public void setProjetoId(int projetoId) { this.projetoId = projetoId; }
    
    public String getNomeProjeto() { return nomeProjeto; }
    public void setNomeProjeto(String nomeProjeto) { this.nomeProjeto = nomeProjeto; }
    
    public int getResponsavelId() { return responsavelId; }
    public void setResponsavelId(int responsavelId) { this.responsavelId = responsavelId; }
    
    public String getNomeResponsavel() { return nomeResponsavel; }
    public void setNomeResponsavel(String nomeResponsavel) { this.nomeResponsavel = nomeResponsavel; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDate getDataInicioPrevista() { return dataInicioPrevista; }
    public void setDataInicioPrevista(LocalDate dataInicioPrevista) { this.dataInicioPrevista = dataInicioPrevista; }
    
    public LocalDate getDataFimPrevista() { return dataFimPrevista; }
    public void setDataFimPrevista(LocalDate dataFimPrevista) { this.dataFimPrevista = dataFimPrevista; }
    
    public LocalDate getDataInicioReal() { return dataInicioReal; }
    public void setDataInicioReal(LocalDate dataInicioReal) { this.dataInicioReal = dataInicioReal; }
    
    public LocalDate getDataFimReal() { return dataFimReal; }
    public void setDataFimReal(LocalDate dataFimReal) { this.dataFimReal = dataFimReal; }
    
    @Override
    public String toString() {
        return titulo;
    }
}