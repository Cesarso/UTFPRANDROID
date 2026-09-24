package br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.persistencia;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.modelo.ItemPedidoEntity;
import br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.modelo.Pedido;
import br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.utils.Converters;

@Database(
        entities = {
                Pedido.class,
                ItemPedidoEntity.class
        },
        version = 1,
        exportSchema = true
)
@TypeConverters(Converters.class)
public abstract class PedidoDataBase extends RoomDatabase {

    public abstract PedidoDao getPedidoDao();

    public abstract ItemPedidoDao getItemPedidoDao();

    private static PedidoDataBase INSTANCE;

    public static PedidoDataBase getInstance(final Context context) {

        if (INSTANCE == null) {

            synchronized (PedidoDataBase.class) {

                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(
                                    context,
                                    PedidoDataBase.class,
                                    "pedido.db"
                            )
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
