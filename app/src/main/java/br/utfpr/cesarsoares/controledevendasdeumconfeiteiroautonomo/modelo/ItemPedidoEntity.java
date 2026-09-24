package br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.modelo;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "ITEM_PEDIDO",
        foreignKeys = {
                @ForeignKey(
                        entity = Pedido.class,
                        parentColumns = "idPedido",
                        childColumns = "idPedido",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("idPedido"),
                @Index("idProduto")
        }
)
public class ItemPedidoEntity {

    @PrimaryKey(autoGenerate = true)
    private long idItemPedido;

    private long idPedido;

    private long idProduto;

    private String descricao;

    private double valorUnitario;

    private int quantidade;

    public ItemPedidoEntity() {
    }

    public long getIdItemPedido() {
        return idItemPedido;
    }

    public void setIdItemPedido(long idItemPedido) {
        this.idItemPedido = idItemPedido;
    }

    public long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(long idPedido) {
        this.idPedido = idPedido;
    }

    public long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(long idProduto) {
        this.idProduto = idProduto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
