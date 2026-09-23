package br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.modelo;

public class ItemPedido {
    private final Produto produto;
    private int quantidade;

    public ItemPedido(Produto produto) {
        this.produto = produto;
        this.quantidade = 1;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void incrementar() {
        quantidade++;
    }

    public double getSubtotal() {
        return quantidade * produto.getValor();
    }
}
