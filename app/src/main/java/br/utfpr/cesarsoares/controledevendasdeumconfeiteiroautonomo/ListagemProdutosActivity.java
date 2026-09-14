package br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo;

import android.content.Intent;
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
import androidx.appcompat.view.ActionMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class ListagemProdutosActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_PRODUTO = 1;

    private ListView listViewProdutos;
    private ArrayList<Produto> listaProdutos;
    private ProdutoAdapter adapter;
    private ActionMode actionMode;
    private int posicaoSelecionada = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_produtos);

        setTitle("Lista de Produtos");

        listViewProdutos = findViewById(R.id.listViewProdutos);

        listaProdutos = new ArrayList<>();
        adapter = new ProdutoAdapter(this, listaProdutos);
        listViewProdutos.setAdapter(adapter);

        listViewProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Produto produtoClicado = listaProdutos.get(position);
                String mensagem = "Produto: " + produtoClicado.getDescricao() + 
                                 "\nValor: R$ " + String.format(Locale.getDefault(), "%.2f", produtoClicado.getValor());
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
                            listaProdutos.remove(posicaoSelecionada);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(ListagemProdutosActivity.this, "Produto excluído com sucesso", Toast.LENGTH_SHORT).show();
                            mode.finish();
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

    // Criar o menu de opções na barra do app (Adicionar, Sobre)
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
                Collections.sort(listaProdutos, Produto.COMPARADOR_DESCRICAO);
                adapter.notifyDataSetChanged();
            }
        }
    }
}