package br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import java.util.ArrayList;
import java.util.Date;

import br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.modelo.ItemPedido;
import br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.modelo.ItemPedidoEntity;
import br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.modelo.Pedido;
import br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.persistencia.PedidoDataBase;

public class PedidoActivity extends AppCompatActivity {

    public static final String EXTRA_ITENS_PEDIDO = "itensPedido";

    private ListView listViewItensPedido;
    private TextView textViewTotalPedido;
    private Button buttonSalvarPedido;

    private ArrayList<ItemPedido> itensPedido;
    private ItemPedidoAdapter adapter;

    private ActionMode actionMode;
    private int posicaoSelecionada = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        setTitle(R.string.title_pedido);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        listViewItensPedido = findViewById(R.id.listViewItensPedido);
        textViewTotalPedido = findViewById(R.id.textViewTotalPedido);
        buttonSalvarPedido = findViewById(R.id.buttonSalvarPedido);

        receberItensPedido();

        adapter = new ItemPedidoAdapter(this, itensPedido);
        listViewItensPedido.setAdapter(adapter);

        configurarCliqueLongo();

        buttonSalvarPedido.setOnClickListener(v -> salvarPedido());

        atualizarTotalPedido();
    }

    private void receberItensPedido() {

        Intent intent = getIntent();

        if (intent != null &&
                intent.hasExtra(EXTRA_ITENS_PEDIDO)) {

            ArrayList<ItemPedido> itensRecebidos =
                    (ArrayList<ItemPedido>)
                            intent.getSerializableExtra(EXTRA_ITENS_PEDIDO);

            if (itensRecebidos != null) {
                itensPedido = itensRecebidos;
            }
        }

        if (itensPedido == null) {
            itensPedido = new ArrayList<>();
        }
    }

    private void configurarCliqueLongo() {

        listViewItensPedido.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(
                            AdapterView<?> parent,
                            android.view.View view,
                            int position,
                            long id) {

                        if (actionMode != null) {
                            return false;
                        }

                        posicaoSelecionada = position;

                        actionMode = startSupportActionMode(
                                new ActionMode.Callback() {

                                    @Override
                                    public boolean onCreateActionMode(
                                            ActionMode mode,
                                            Menu menu) {

                                        mode.getMenuInflater().inflate(
                                                R.menu.menu_contextual_pedido,
                                                menu
                                        );

                                        return true;
                                    }

                                    @Override
                                    public boolean onPrepareActionMode(
                                            ActionMode mode,
                                            Menu menu) {

                                        return false;
                                    }

                                    @Override
                                    public boolean onActionItemClicked(
                                            ActionMode mode,
                                            MenuItem item) {

                                        if (item.getItemId()
                                                == R.id.menu_excluir_item_pedido) {

                                            excluirItemSelecionado(mode);

                                            return true;
                                        }

                                        return false;
                                    }

                                    @Override
                                    public void onDestroyActionMode(
                                            ActionMode mode) {

                                        actionMode = null;
                                        posicaoSelecionada = -1;
                                    }
                                }
                        );

                        return true;
                    }
                }
        );
    }

    private void excluirItemSelecionado(ActionMode mode) {

        if (posicaoSelecionada < 0
                || posicaoSelecionada >= itensPedido.size()) {

            return;
        }

        ItemPedido item = itensPedido.get(posicaoSelecionada);

        String mensagem = getString(
                R.string.deseja_excluir_item_pedido,
                item.getProduto().getDescricao()
        );

        DialogInterface.OnClickListener listenerSim =
                (dialog, which) -> {

                    itensPedido.remove(posicaoSelecionada);

                    adapter.notifyDataSetChanged();

                    atualizarTotalPedido();

                    // Informa à tela principal que a lista mudou.
                    devolverPedidoAtualizado();

                    mode.finish();

                    Toast.makeText(
                            PedidoActivity.this,
                            R.string.item_pedido_excluido,
                            Toast.LENGTH_SHORT
                    ).show();
                };

        new AlertDialog.Builder(this)
                .setMessage(mensagem)
                .setNegativeButton(
                        android.R.string.cancel,
                        null
                )
                .setPositiveButton(
                        android.R.string.ok,
                        listenerSim
                )
                .show();
    }

    private void limparPedido() {

        if (itensPedido.isEmpty()) {
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.limpar_pedido)
                .setMessage(R.string.deseja_limpar_pedido)
                .setNegativeButton(
                        android.R.string.cancel,
                        null
                )
                .setPositiveButton(
                        R.string.limpar_pedido,
                        (dialog, which) -> {

                            itensPedido.clear();

                            adapter.notifyDataSetChanged();

                            atualizarTotalPedido();

                            // Devolve a lista vazia para a tela principal.
                            devolverPedidoAtualizado();
                        }
                )
                .show();
    }

    private void atualizarTotalPedido() {

        double total = 0.0;

        for (ItemPedido item : itensPedido) {
            total += item.getSubtotal();
        }

        textViewTotalPedido.setText(
                getString(
                        R.string.total_pedido,
                        total
                )
        );

        buttonSalvarPedido.setEnabled(!itensPedido.isEmpty());
    }

    private void salvarPedido() {

        if (itensPedido.isEmpty()) {

            Toast.makeText(
                    this,
                    R.string.pedido_vazio,
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        double total = 0.0;

        for (ItemPedido item : itensPedido) {
            total += item.getSubtotal();
        }

        Pedido pedido = new Pedido();
        pedido.setData(new Date());
        pedido.setTotal(total);

        PedidoDataBase database =
                PedidoDataBase.getInstance(this);

        database.runInTransaction(() -> {

            // Salva o pedido e obtém o ID gerado pelo Room.
            long idPedido =
                    database.getPedidoDao().insert(pedido);

            ArrayList<ItemPedidoEntity> itensEntity =
                    new ArrayList<>();

            for (ItemPedido item : itensPedido) {

                ItemPedidoEntity itemEntity =
                        new ItemPedidoEntity();

                itemEntity.setIdPedido(idPedido);

                itemEntity.setIdProduto(
                        item.getProduto().getIdProduto()
                );

                itemEntity.setDescricao(
                        item.getProduto().getDescricao()
                );

                itemEntity.setValorUnitario(
                        item.getProduto().getValor()
                );

                itemEntity.setQuantidade(
                        item.getQuantidade()
                );

                itensEntity.add(itemEntity);
            }

            database.getItemPedidoDao().insertAll(itensEntity);
        });

        Toast.makeText(
                this,
                R.string.pedido_salvo,
                Toast.LENGTH_SHORT
        ).show();

        Toast.makeText(
                this,
                R.string.pedido_salvo,
                Toast.LENGTH_SHORT
        ).show();

        // Pedido salvo: devolve uma lista vazia.
        //itensPedido.clear();

        devolverPedidoAtualizado();

        //finish();
    }

    private void devolverPedidoAtualizado() {

        Intent intent = new Intent();

        intent.putExtra(
                EXTRA_ITENS_PEDIDO,
                itensPedido
        );

        setResult(RESULT_OK, intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(
                R.menu.menu_pedido,
                menu
        );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(
            @NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.menu_limpar_pedido) {
            limparPedido();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}