package br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.databinding.ActivityProdutosBinding;
import br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.modelo.Produto;
import br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.persistencia.ProdutoDataBase;
import br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.utils.UtilsAlert;

public class ProdutosActivity extends AppCompatActivity {

    private ActivityProdutosBinding binding;

    public static final String KEY_ID = "IDPRODUTO";
    private Produto produtoOriginal = null;
    private String[] unidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProdutosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        unidades = getResources().getStringArray(R.array.unidades_array);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        configurarSpinner();

        // Verificar se está em modo de edição
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("produto")) {
            setTitle(R.string.title_editar_produto);
            produtoOriginal = (Produto) intent.getSerializableExtra("produto");
            if (produtoOriginal != null) {
                preencherCampos(produtoOriginal);
            }
        } else {
            setTitle(R.string.title_cadastro_produto);
        }
    }

    private void configurarSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, unidades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spUnidade.setAdapter(adapter);
    }

    private void preencherCampos(Produto produto) {
        binding.etDescricao.setText(produto.getDescricao());
        binding.etQuantidade.setText(String.valueOf(produto.getQuantidade()));
        binding.etValor.setText(String.valueOf(produto.getValor()));
        binding.cbDisponivel.setChecked(produto.isDisponivel());

        // Para acessar os textos do arquivo string.xml usamos getString(R.string.ativo)
        // Para o arquivo string.xml, comparamos com os valores
        if (getString(R.string.ativo).equalsIgnoreCase(produto.getStatus())) {
            binding.rbAtivo.setChecked(true);
        } else if (getString(R.string.inativo).equalsIgnoreCase(produto.getStatus())) {
            binding.rbInativo.setChecked(true);
        }

        for (int i = 0; i < unidades.length; i++) {
            if (unidades[i].equalsIgnoreCase(produto.getUnidade())) {
                binding.spUnidade.setSelection(i);
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.menu_salvar) {
            salvar();
            return true;
        } else if (id == R.id.menu_limpar) {
            limpar();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void salvar() {
        String descricao = binding.etDescricao.getText().toString().trim();
        String qtdStr = binding.etQuantidade.getText().toString().trim();
        String valorStr = binding.etValor.getText().toString().trim();
        int selectedRadioId = binding.rgStatus.getCheckedRadioButtonId();

        // Validação dos EditTexts
        if (descricao.isEmpty()) {
            UtilsAlert.mostrarAviso(this, R.string.erro_descricao);
            binding.etDescricao.requestFocus();
            return;
        }

        if (qtdStr.isEmpty()) {
            UtilsAlert.mostrarAviso(this, R.string.erro_quantidade);
            binding.etQuantidade.requestFocus();
            return;
        }

        if (valorStr.isEmpty()) {
            UtilsAlert.mostrarAviso(this, R.string.erro_valor);
            binding.etValor.requestFocus();
            return;
        }

        // Validação do RadioButton
        if (selectedRadioId == -1) {
            UtilsAlert.mostrarAviso(this, R.string.erro_status);
            return;
        }

        // Se passar nas validações, coleta os dados restantes
        RadioButton rbSelecionado = findViewById(selectedRadioId);
        String status = rbSelecionado.getText().toString();
        boolean isDisponivel = binding.cbDisponivel.isChecked();
        String unidade = binding.spUnidade.getSelectedItem().toString();
        double quantidade = Double.parseDouble(qtdStr);
        double valor = Double.parseDouble(valorStr);

        // Cria/Atualiza o objeto Produto
        Produto produto = new Produto(descricao, quantidade, valor, unidade, status, isDisponivel);

        if (produtoOriginal != null) {
            produto.setIdProduto(produtoOriginal.getIdProduto());
            ProdutoDataBase.getInstance(this).getProdutoDao().update(produto);
        } else {
            ProdutoDataBase.getInstance(this).getProdutoDao().insert(produto);
        }

        setResult(RESULT_OK);
        finish();
    }

    private void limpar() {
        // Limpa EditTexts
        binding.etDescricao.setText("");
        binding.etQuantidade.setText("");
        binding.etValor.setText("");

        // Reseta RadioGroup e CheckBox
        binding.rgStatus.clearCheck();
        binding.cbDisponivel.setChecked(false);

        // Reseta Spinner
        binding.spUnidade.setSelection(0);

        // Foco inicial
        binding.etDescricao.requestFocus();

        //Toast.makeText(this, R.string.dados_limpos, Toast.LENGTH_SHORT).show();
        UtilsAlert.mostrarAviso(this, R.string.dados_limpos);
    }

    /* Trocado pelo AlertDialog
    private void mostrarErro(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

     */
}