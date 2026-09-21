package br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.utils.UtilsAlert;

public class ConfiguracoesActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "ConfigPrefs";
    public static final String KEY_NIGHT_MODE = "nightMode";
    public static final String KEY_SORT_ORDER = "sortOrder";

    public static final int SORT_DESCRIPTION = 0;
    public static final int SORT_PRICE = 1;

    private SwitchCompat switchModoNoturno;
    private RadioGroup rgOrdenacao;
    private Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        setTitle(R.string.title_configuracoes);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        switchModoNoturno = findViewById(R.id.switchModoNoturno);
        rgOrdenacao = findViewById(R.id.rgOrdenacao);
        btnSalvar = findViewById(R.id.btnSalvarConfig);

        carregarPreferencias();

        btnSalvar.setOnClickListener(v -> {
            salvarPreferencias();
            //finish();
        });
    }

    private void carregarPreferencias() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean(KEY_NIGHT_MODE, false);
        int sortOrder = sharedPreferences.getInt(KEY_SORT_ORDER, SORT_DESCRIPTION);

        switchModoNoturno.setChecked(nightMode);

        if (sortOrder == SORT_PRICE) {
            rgOrdenacao.check(R.id.rbOrdemPreco);
        } else {
            rgOrdenacao.check(R.id.rbOrdemDescricao);
        }
    }

    private void salvarPreferencias() {
        boolean nightMode = switchModoNoturno.isChecked();
        int sortOrder = rgOrdenacao.getCheckedRadioButtonId() == R.id.rbOrdemPreco ? SORT_PRICE : SORT_DESCRIPTION;

        String mensagem = getString(R.string.deseja_salvar);

        DialogInterface.OnClickListener listenerSim = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(KEY_NIGHT_MODE, nightMode);
                editor.putInt(KEY_SORT_ORDER, sortOrder);
                editor.apply();

                // Aplicar modo noturno imediatamente
                // Agora aplica modo noturno após salvar confirgurações
                if (nightMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                finish();
            }

        };
        UtilsAlert.confirmarAcao(this,mensagem, listenerSim, null);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}