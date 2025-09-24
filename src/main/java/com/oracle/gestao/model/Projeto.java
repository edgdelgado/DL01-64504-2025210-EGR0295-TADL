package com.oracle.gestao.model;

import java.time.LocalDate;

public class Projeto {
    private int id;
    private String nome;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataFimPrevista;
    private LocalDate dataFimReal;
    private String status;
    private int gerenteId;
    private String nomeGerente;
    
    public Projeto() {}
    
    public Projeto(String nome, String descricao, LocalDate dataInicio, 
                   LocalDate dataFimPrevista, int gerenteId) {
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFimPrevista = dataFimPrevista;
        this.gerenteId = gerenteId;
        this.status = "planejado";
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    
    public LocalDate getDataFimPrevista() { return dataFimPrevista; }
    public void setDataFimPrevista(LocalDate dataFimPrevista) { this.dataFimPrevista = dataFimPrevista; }
    
    public LocalDate getDataFimReal() { return dataFimReal; }
    public void setDataFimReal(LocalDate dataFimReal) { this.dataFimReal = dataFimReal; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public int getGerenteId() { return gerenteId; }
    public void setGerenteId(int gerenteId) { this.gerenteId = gerenteId; }
    
    public String getNomeGerente() { return nomeGerente; }
    public void setNomeGerente(String nomeGerente) { this.nomeGerente = nomeGerente; }
    
    @Override
    public String toString() {
        return nome;
    }
}