package br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.persistencia;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.modelo.Pedido;

@Dao
public interface PedidoDao {
    @Insert
    long insert(Pedido pedido);

    @Delete
    int delete(Pedido pedido);

    @Update
    int update(Pedido pedido);

    @Query("SELECT * FROM PEDIDO WHERE idPedido=:idPedido")
    List<Pedido> queryForId(long idPedido);

    @Query("SELECT * FROM PEDIDO ORDER BY TOTAL ASC")
    List<Pedido> queryAllAscending();
    @Query("SELECT * FROM PEDIDO ORDER BY TOTAL DESC")
    List<Pedido> queryAllDownward();
}
