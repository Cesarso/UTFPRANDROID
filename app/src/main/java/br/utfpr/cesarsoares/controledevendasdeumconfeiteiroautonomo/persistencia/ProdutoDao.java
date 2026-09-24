package br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.persistencia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.modelo.Produto;

@Dao
public interface ProdutoDao {
    @Insert
    long insert(Produto produto);

    @Delete
    int delete(Produto produto);

    @Update
    int update(Produto produto);

    @Query("SELECT * FROM PRODUTO WHERE idProduto=:idProduto")
    List<Produto> queryForId(long idProduto);

    @Query("SELECT * FROM PRODUTO ORDER BY DESCRICAO ASC")
    List<Produto> queryAllAscending();
    @Query("SELECT * FROM PRODUTO ORDER BY DESCRICAO DESC")
    List<Produto> queryAllDownward();
}
