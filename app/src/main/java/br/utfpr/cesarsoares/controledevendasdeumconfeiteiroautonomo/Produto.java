package br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo;

import java.io.Serializable;

public class Produto implements Serializable {
    private String descricao;
    private double quantidade;
    private double valor;
    private String unidade;
    private String status;
    private boolean disponivel;

    // Constructor
    public Produto(String descricao, double quantidade, double valor, String unidade, String status, boolean disponivel) {
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.valor = valor;
        this.unidade = unidade;
        this.status = status;
        this.disponivel = disponivel;
    }

    // Getters and Setters
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public double getQuantidade() { return quantidade; }
    public void setQuantidade(double quantidade) { this.quantidade = quantidade; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }

    @Override
    public String toString() {
        return descricao + " - " + unidade;
    }
}