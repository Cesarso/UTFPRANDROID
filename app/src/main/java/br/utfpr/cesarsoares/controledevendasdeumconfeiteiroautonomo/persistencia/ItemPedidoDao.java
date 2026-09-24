package br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.persistencia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.modelo.ItemPedidoEntity;

@Dao
public interface ItemPedidoDao {

    @Insert
    long insert(ItemPedidoEntity item);

    @Insert
    void insertAll(List<ItemPedidoEntity> itens);

    @Delete
    int delete(ItemPedidoEntity item);

    @Query("SELECT * FROM ITEM_PEDIDO WHERE idPedido = :idPedido")
    List<ItemPedidoEntity> queryByPedido(long idPedido);

    @Query("DELETE FROM ITEM_PEDIDO WHERE idPedido = :idPedido")
    int deleteByPedido(long idPedido);
}