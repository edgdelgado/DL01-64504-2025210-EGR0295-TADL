package com.oracle.gestao.model;

public class Equipe {
    private int id;
    private String nome;
    private String descricao;
    
    public Equipe() {}
    
    public Equipe(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    @Override
    public String toString() {
        return nome;
    }
}