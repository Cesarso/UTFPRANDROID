package br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class ProdutoAdapter extends BaseAdapter {

    private final Context context;
    private final List<Produto> produtos;

    public ProdutoAdapter(Context context, List<Produto> produtos) {
        this.context = context;
        this.produtos = produtos;
    }

    @Override
    public int getCount() {
        return produtos.size();
    }

    @Override
    public Object getItem(int position) {
        return produtos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_produto, parent, false);
        }

        Produto produto = produtos.get(position);

        TextView tvDescricao = convertView.findViewById(R.id.textViewDescricao);
        TextView tvQuantidade = convertView.findViewById(R.id.textViewQuantidade);
        TextView tvValor = convertView.findViewById(R.id.textViewValor);

        tvDescricao.setText(produto.getDescricao());
        tvQuantidade.setText(String.format(Locale.getDefault(), "Qtd: %.2f %s", produto.getQuantidade(), produto.getUnidade()));
        tvValor.setText(String.format(Locale.getDefault(), "R$ %.2f", produto.getValor()));

        return convertView;
    }
}