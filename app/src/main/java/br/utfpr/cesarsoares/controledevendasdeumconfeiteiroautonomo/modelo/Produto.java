package br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.modelo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

@Entity
public class Produto implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long idProduto;
    @ColumnInfo(index = true)
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

    public long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(long idProduto) {
        this.idProduto = idProduto;
    }

    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }

    //Comparator auxilia na ordenação da lista.
    public static final Comparator<Produto> COMPARADOR_DESCRICAO = new Comparator<Produto>() {
        private final br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.utils.NaturalOrderComparator naturalOrderComparator = 
            new br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.utils.NaturalOrderComparator();

        @Override
        public int compare(Produto p1, Produto p2) {
            return naturalOrderComparator.compare(p1.getDescricao(), p2.getDescricao());
        }
    };

    public static final Comparator<Produto> COMPARADOR_VALOR = new Comparator<Produto>() {
        @Override
        public int compare(Produto p1, Produto p2) {
            return Double.compare(p1.getValor(), p2.getValor());
        }
    };

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Double.compare(quantidade, produto.quantidade) == 0 && Double.compare(valor, produto.valor) == 0 && disponivel == produto.disponivel && Objects.equals(descricao, produto.descricao) && Objects.equals(unidade, produto.unidade) && Objects.equals(status, produto.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(descricao, quantidade, valor, unidade, status, disponivel);
    }

    @Override
    public String toString() {
        return descricao + " - " + unidade;
    }
}