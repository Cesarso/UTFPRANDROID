package br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.modelo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Pedido implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long idPedido;
    private Date data;
    private Double total;

    public Pedido() {
    }

    public long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(long idPedido) {
        this.idPedido = idPedido;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
