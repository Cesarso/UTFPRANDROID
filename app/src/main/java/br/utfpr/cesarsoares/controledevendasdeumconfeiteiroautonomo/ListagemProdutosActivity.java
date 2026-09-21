package br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.ActionMode;

import java.util.ArrayList;
import java.util.Collections;

import br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.utils.UtilsAlert;

public class ListagemProdutosActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_PRODUTO = 1;

    private ListView listViewProdutos;
    private ArrayList<Produto> listaProdutos;
    private ProdutoAdapter adapter;
    private ActionMode actionMode;
    private int posicaoSelecionada = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        aplicarModoNoturno();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_produtos);

        setTitle(R.string.title_lista_produto);

        listViewProdutos = findViewById(R.id.listViewProdutos);

        listaProdutos = new ArrayList<>();
        adapter = new ProdutoAdapter(this, listaProdutos);
        listViewProdutos.setAdapter(adapter);

        listViewProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Produto produtoClicado = listaProdutos.get(position);
                String mensagem = getString(R.string.mensagem_produto_clicado, 
                        produtoClicado.getDescricao(), produtoClicado.getValor());
                Toast.makeText(ListagemProdutosActivity.this, mensagem, Toast.LENGTH_SHORT).show();
            }
        });

        listViewProdutos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (actionMode != null) {
                    return false;
                }
                posicaoSelecionada = position;
                actionMode = startSupportActionMode(new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        mode.getMenuInflater().inflate(R.menu.menu_contextual_produto, menu);
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        int id = item.getItemId();

                        if (id == R.id.menu_editar) {
                            Produto produtoSelecionado = listaProdutos.get(posicaoSelecionada);
                            Intent intent = new Intent(ListagemProdutosActivity.this, ProdutosActivity.class);
                            intent.putExtra("produto", produtoSelecionado);
                            intent.putExtra("posicao", posicaoSelecionada);
                            startActivityForResult(intent, REQUEST_CODE_PRODUTO);
                            mode.finish();
                            return true;
                        } else if (id == R.id.menu_excluir) {

                        //  Troca pelo alertDialog
                        //    Toast.makeText(ListagemProdutosActivity.this, R.string.produto_excluido, Toast.LENGTH_SHORT).show();

                            Produto produto = listaProdutos.get(posicaoSelecionada);
                         //   String mensagem = getString(R.string.deseja_apagar) + " " + "\"" + produto.getDescricao() + "\"";
                            // Com passagem de parâmetro ao message
                            String mensagem = getString(R.string.deseja_apagar, produto.getDescricao());

                            DialogInterface.OnClickListener listenerSim = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    listaProdutos.remove(posicaoSelecionada);
                                    adapter.notifyDataSetChanged(); //listView
                                    mode.finish();
                                }

                            };

                            UtilsAlert.confirmarAcao(ListagemProdutosActivity.this, mensagem, listenerSim, null);

                            return true;
                        }
                        return false;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        actionMode = null;
                        posicaoSelecionada = -1;
                    }
                });
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ordenarLista();
        adapter.notifyDataSetChanged();
    }

    private void aplicarModoNoturno() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConfiguracoesActivity.PREFS_NAME, Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean(ConfiguracoesActivity.KEY_NIGHT_MODE, false);

        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void ordenarLista() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConfiguracoesActivity.PREFS_NAME, Context.MODE_PRIVATE);
        int sortOrder = sharedPreferences.getInt(ConfiguracoesActivity.KEY_SORT_ORDER, ConfiguracoesActivity.SORT_DESCRIPTION);

        if (sortOrder == ConfiguracoesActivity.SORT_PRICE) {
            Collections.sort(listaProdutos, Produto.COMPARADOR_VALOR);
        } else {
            Collections.sort(listaProdutos, Produto.COMPARADOR_DESCRICAO);
        }
    }

    // Criar o menu de opções na barra do app (Adicionar, Configurações, Sobre)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_listagem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_adicionar) {
            Intent intent = new Intent(ListagemProdutosActivity.this, ProdutosActivity.class);
            startActivityForResult(intent, REQUEST_CODE_PRODUTO);
            return true;
        } else if (id == R.id.menu_configuracoes) {
            Intent intent = new Intent(ListagemProdutosActivity.this, ConfiguracoesActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_sobre) {
            Intent intent = new Intent(ListagemProdutosActivity.this, SobreActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PRODUTO && resultCode == RESULT_OK && data != null) {
            Produto produto = (Produto) data.getSerializableExtra("produto");
            int posicao = data.getIntExtra("posicao", -1);

            if (produto != null) {
                if (posicao == -1) {
                    // Novo produto
                    listaProdutos.add(produto);
                } else {
                    // Produto editado
                    if (posicao >= 0 && posicao < listaProdutos.size()) {
                        listaProdutos.set(posicao, produto);
                    }
                }
                ordenarLista();
                adapter.notifyDataSetChanged();
            }
        }
    }
}