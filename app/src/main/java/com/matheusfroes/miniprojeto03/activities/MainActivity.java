package com.matheusfroes.miniprojeto03.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.matheusfroes.miniprojeto03.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    CardView cardFuncionarios;
    CardView cardLucro;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        cardFuncionarios = (CardView) findViewById(R.id.card_funcionarios);
        cardFuncionarios.setOnClickListener(this);

        cardLucro = (CardView) findViewById(R.id.card_lucro);
        cardLucro.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.card_funcionarios:
                startActivity(new Intent(this, ListaFuncionariosActivity.class));
                break;
            case R.id.card_lucro:
                dialogLucroAnual();
                break;
        }
    }

    private void dialogLucroAnual() {
        View view = getLayoutInflater().inflate(R.layout.lucro_anual_view, null, false);

        final CurrencyEditText etLucroAnual = (CurrencyEditText) view.findViewById(R.id.et_lucro);
        String lucro = etLucroAnual.formatCurrency((getLucroAnual() * 100) + "");
        etLucroAnual.setText(lucro);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Lucro Anual")
                .setPositiveButton("Aplicar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setLucroAnual(etLucroAnual.getRawValue());
                    }
                })
                .setView(view);

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    private void setLucroAnual(long rawValue) {
        float lucroAnual = Float.parseFloat((rawValue / 100.00) + "");
        preferences.edit().putFloat("lucro_anual", lucroAnual).apply();
    }

    public float getLucroAnual() {
        return preferences.getFloat("lucro_anual", 0);
    }
}
