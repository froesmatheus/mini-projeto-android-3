package com.matheusfroes.miniprojeto03.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.blackcat.currencyedittext.CurrencyTextFormatter;
import com.matheusfroes.miniprojeto03.R;
import com.matheusfroes.miniprojeto03.Utils;
import com.matheusfroes.miniprojeto03.db.FuncionarioDAO;
import com.matheusfroes.miniprojeto03.models.Funcionario;

import java.util.Currency;
import java.util.Locale;

public class DetalhesFuncionario extends AppCompatActivity {
    FuncionarioDAO funcionarioDAO;

    TextView tvNome, tvCpf, tvCargo, tvSalario, tvBonus;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_funcionario);

        funcionarioDAO = new FuncionarioDAO(this);
        preferences = getSharedPreferences("PREFERENCES", MODE_PRIVATE);

        tvNome = (TextView) findViewById(R.id.tv_nome_funcionario);
        tvCpf = (TextView) findViewById(R.id.tv_cpf_funcionario);
        tvCargo = (TextView) findViewById(R.id.tv_cargo_funcionario);
        tvSalario = (TextView) findViewById(R.id.tv_salario_funcionario);
        tvBonus = (TextView) findViewById(R.id.tv_bonus_funcionario);

        assert getIntent() != null;

        long funcionarioId = getIntent().getLongExtra("funcionario_id", -1);

        Funcionario funcionario = funcionarioDAO.get(funcionarioId);

        assert funcionario != null;

        float lucroAnual = preferences.getFloat("lucro_anual", 0);
        double bonus = Utils.calcularBonusFuncionario(this, funcionario.getCargo(), (lucroAnual / 100));

        Locale brasil = new Locale("pt", "BR");
        Currency currency = Currency.getInstance(brasil);

        tvNome.setText(funcionario.getNome());
        tvCpf.setText(funcionario.getCpf());
        tvCargo.setText(funcionario.getCargo().toString());
        tvSalario.setText(CurrencyTextFormatter.formatText(funcionario.getSalario() * 100 + "", currency, brasil));
        tvBonus.setText(CurrencyTextFormatter.formatText((bonus*100)+"", currency, brasil));

    }
}
