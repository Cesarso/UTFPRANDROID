package br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.databinding.ActivityProdutosBinding;

public class ProdutosActivity extends AppCompatActivity {

    private ActivityProdutosBinding binding;
    private int posicaoEdicao = -1;

    //Ordem está pela unidade mais utilizada no cadastro.
    private final String[] unidades = {"Unidade", "Kg", "Grama", "Litro", "Caixa"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProdutosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        configurarSpinner();

        // Verificar se está em modo de edição
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("produto")) {
            setTitle("Editar Produto");
            Produto produto = (Produto) intent.getSerializableExtra("produto");
            posicaoEdicao = intent.getIntExtra("posicao", -1);
            if (produto != null) {
                preencherCampos(produto);
            }
        } else {
            setTitle("Cadastro de Produto");
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

        if ("Ativo".equalsIgnoreCase(produto.getStatus())) {
            binding.rbAtivo.setChecked(true);
        } else if ("Inativo".equalsIgnoreCase(produto.getStatus())) {
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
            mostrarErro(getString(R.string.erro_descricao));
            binding.etDescricao.requestFocus();
            return;
        }

        if (qtdStr.isEmpty()) {
            mostrarErro(getString(R.string.erro_quantidade));
            binding.etQuantidade.requestFocus();
            return;
        }

        if (valorStr.isEmpty()) {
            mostrarErro(getString(R.string.erro_valor));
            binding.etValor.requestFocus();
            return;
        }

        // Validação do RadioButton
        if (selectedRadioId == -1) {
            mostrarErro(getString(R.string.erro_status));
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

        // Devolve o resultado para a Activity de Listagem
        Intent intentResposta = new Intent();
        intentResposta.putExtra("produto", produto);
        intentResposta.putExtra("posicao", posicaoEdicao);
        setResult(RESULT_OK, intentResposta);
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

        Toast.makeText(this, R.string.dados_limpos, Toast.LENGTH_SHORT).show();
    }

    private void mostrarErro(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }
}