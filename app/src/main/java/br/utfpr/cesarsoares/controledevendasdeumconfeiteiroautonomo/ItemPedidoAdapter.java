package br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.modelo.ItemPedido;
import br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.modelo.Produto;

public class ItemPedidoAdapter extends BaseAdapter {

    private final Context context;
    private final List<ItemPedido> itensPedido;

    public ItemPedidoAdapter(
            Context context,
            List<ItemPedido> itensPedido) {

        this.context = context;
        this.itensPedido = itensPedido;
    }

    @Override
    public int getCount() {
        return itensPedido.size();
    }

    @Override
    public Object getItem(int position) {
        return itensPedido.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(
            int position,
            View convertView,
            ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(
                            R.layout.list_item_pedido,
                            parent,
                            false
                    );
        }

        ItemPedido item = itensPedido.get(position);
        Produto produto = item.getProduto();

        TextView textViewDescricao =
                convertView.findViewById(
                        R.id.textViewDescricaoPedido
                );

        TextView textViewQuantidade =
                convertView.findViewById(
                        R.id.textViewQuantidadePedido
                );

        TextView textViewValorUnitario =
                convertView.findViewById(
                        R.id.textViewValorUnitarioPedido
                );

        TextView textViewSubtotal =
                convertView.findViewById(
                        R.id.textViewSubtotalPedido
                );

        textViewDescricao.setText(
                produto.getDescricao()
        );

        textViewQuantidade.setText(
                context.getString(
                        R.string.quantidade_item_pedido,
                        item.getQuantidade()
                )
        );

        textViewValorUnitario.setText(
                context.getString(
                        R.string.valor_unitario_item_pedido,
                        produto.getValor()
                )
        );

        textViewSubtotal.setText(
                context.getString(
                        R.string.subtotal_item_pedido,
                        item.getSubtotal()
                )
        );

        return convertView;
    }
}