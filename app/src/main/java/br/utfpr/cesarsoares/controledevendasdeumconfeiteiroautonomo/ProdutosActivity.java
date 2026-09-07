package br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.databinding.ActivityProdutosBinding;

public class ProdutosActivity extends AppCompatActivity {

    private ActivityProdutosBinding binding;
    private Produto produto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityProdutosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        configurarSpinner();

        // Listeners dos Botões
        binding.btnSalvar.setOnClickListener(v -> salvar());
        binding.btnLimpar.setOnClickListener(v -> limpar());
    }

    private void configurarSpinner() {
        String[] unidades = {"Unidade", "Kg", "Grama", "Litro", "Caixa"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, unidades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spUnidade.setAdapter(adapter);
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

        // Cria o objeto Produto
        Produto produto = new Produto(descricao, quantidade, valor, unidade, status, isDisponivel);

        // Devolve o resultado para a Activity de Listagem
        Intent intent = new Intent();
        intent.putExtra("produto", produto);
        setResult(RESULT_OK, intent);
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