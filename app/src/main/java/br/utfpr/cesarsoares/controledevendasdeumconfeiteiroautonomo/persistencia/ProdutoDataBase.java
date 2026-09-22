package br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.persistencia;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.modelo.Produto;

@Database(entities = Produto.class, version = 1, exportSchema = false)
public abstract class ProdutoDataBase extends RoomDatabase  {
    public abstract ProdutoDao getProdutoDao();
    private static ProdutoDataBase INSTANCE;

    public static ProdutoDataBase getInstance(final Context context){
        if (INSTANCE == null){
            synchronized (ProdutoDataBase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context, ProdutoDataBase.class, "produto.db").allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}
