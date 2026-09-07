package br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class ListagemProdutosActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_NOVO_PRODUTO = 1;

    private ListView listViewProdutos;
    private ArrayList<Produto> listaProdutos;
    private ProdutoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_produtos);

        setTitle("Lista de Produtos");

        listViewProdutos = findViewById(R.id.listViewProdutos);
        Button btnAdicionar = findViewById(R.id.btnAdicionar);
        Button btnSobre = findViewById(R.id.btnSobre);

        listaProdutos = new ArrayList<>();
        adapter = new ProdutoAdapter(this, listaProdutos);
        listViewProdutos.setAdapter(adapter);

        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListagemProdutosActivity.this, ProdutosActivity.class);
                startActivityForResult(intent, REQUEST_CODE_NOVO_PRODUTO);
            }
        });

        btnSobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListagemProdutosActivity.this, SobreActivity.class);
                startActivity(intent);
            }
        });

        listViewProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Produto produtoClicado = listaProdutos.get(position);
                String mensagem = "Produto: " + produtoClicado.getDescricao() + 
                                 "\nValor: R$ " + String.format(Locale.getDefault(), "%.2f", produtoClicado.getValor());
                Toast.makeText(ListagemProdutosActivity.this, mensagem, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_NOVO_PRODUTO && resultCode == RESULT_OK && data != null) {
            Produto novoProduto = (Produto) data.getSerializableExtra("produto");
            if (novoProduto != null) {
                listaProdutos.add(novoProduto);
                adapter.notifyDataSetChanged();
            }
        }
    }
}