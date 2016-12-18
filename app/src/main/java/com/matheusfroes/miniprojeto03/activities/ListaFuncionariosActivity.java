package com.matheusfroes.miniprojeto03.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.blackcat.currencyedittext.CurrencyTextFormatter;
import com.matheusfroes.miniprojeto03.R;
import com.matheusfroes.miniprojeto03.adapters.FuncionariosAdapter;
import com.matheusfroes.miniprojeto03.db.FuncionarioDAO;
import com.matheusfroes.miniprojeto03.models.EnumCargos;
import com.matheusfroes.miniprojeto03.models.Funcionario;

import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class ListaFuncionariosActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    FloatingActionButton btnAdicionar;
    ListView lvFuncionarios;
    FuncionarioDAO funcionarioDAO;

    EditText etNome;
    EditText etCpf;
    EditText etCargo;
    CurrencyEditText etSalario;

    TextInputLayout tilNome;
    TextInputLayout tilCpf;
    TextInputLayout tilCargo;
    TextInputLayout tilSalario;

    FuncionariosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_funcionarios);

        funcionarioDAO = new FuncionarioDAO(this);

        btnAdicionar = (FloatingActionButton) findViewById(R.id.btn_adicionar);
        btnAdicionar.setOnClickListener(this);

        lvFuncionarios = (ListView) findViewById(R.id.lv_funcionarios);
        lvFuncionarios.setEmptyView(findViewById(R.id.empty_list_layout));
        lvFuncionarios.setOnItemClickListener(this);

        adapter = new FuncionariosAdapter(this);
        lvFuncionarios.setAdapter(adapter);
        registerForContextMenu(lvFuncionarios);
    }

    @Override
    public void onClick(View view) {
        dialogCadastrarFuncionario();
    }

    private void dialogCadastrarFuncionario() {
        View view = getLayoutInflater().inflate(R.layout.dialog_cadastrar_funcionario, null, false);

        tilNome = (TextInputLayout) view.findViewById(R.id.til_nome);
        tilCpf = (TextInputLayout) view.findViewById(R.id.til_cpf);
        tilCargo = (TextInputLayout) view.findViewById(R.id.til_cargo);
        tilSalario = (TextInputLayout) view.findViewById(R.id.til_salario);

        etNome = (EditText) view.findViewById(R.id.et_nome);
        etCpf = (EditText) view.findViewById(R.id.et_cpf);
        etCargo = (EditText) view.findViewById(R.id.et_cargo);
        etSalario = (CurrencyEditText) view.findViewById(R.id.et_salario);
        etSalario.setLocale(new Locale("pt", "BR"));

        etCargo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCargos();
            }
        });
        etCargo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    dialogCargos();
                }
            }
        });

        Button btnCancelar = (Button) view.findViewById(R.id.btn_cancelar);

        Button btnCadastrar = (Button) view.findViewById(R.id.btn_cadastrar);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.cadastrar_funcionario))
                .setView(view);

        final AlertDialog dialog = builder.create();

        dialog.show();

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checarCampos()) {
                    cadastrarFuncionario();
                    dialog.dismiss();
                }
            }
        });
    }

    private void dialogCargos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final List<EnumCargos> cargos = Arrays.asList(EnumCargos.PROGRAMADOR, EnumCargos.DESIGNER, EnumCargos.GERENTE, EnumCargos.ATENDIMENTO);
        ArrayAdapter<EnumCargos> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1, cargos);
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                etCargo.setText(cargos.get(i).toString());
            }
        });

        builder.create().show();
    }

    private boolean checarCampos() {
        String nome = etNome.getText().toString().trim();
        String cpf = etCpf.getText().toString().trim();
        String cargo = etCargo.getText().toString().trim();
        String salario = etSalario.getText().toString().trim();

        boolean check = true;

        if (nome.isEmpty()) {
            check = false;
            tilNome.setError("Campo obrigatório");
        } else {
            tilNome.setError(null);
        }

        if (cpf.isEmpty()) {
            check = false;
            tilCpf.setError("Campo obrigatório");
        } else {
            tilCpf.setError(null);
        }
        if (cargo.isEmpty()) {
            check = false;
            tilCargo.setError("Campo obrigatório");
        } else {
            tilCargo.setError(null);
        }
        if (salario.isEmpty()) {
            check = false;
            tilSalario.setError("Campo obrigatório");
        } else {
            tilSalario.setError(null);
        }

        return check;
    }

    private void cadastrarFuncionario() {
        String nome = etNome.getText().toString();
        String cpf = etCpf.getText().toString();
        String cargo = etCargo.getText().toString();
        double salario = Double.parseDouble((etSalario.getRawValue() / 100) + "");

        Funcionario funcionario = new Funcionario(nome, cpf, salario, EnumCargos.valueOf(cargo));

        boolean inseriu = funcionarioDAO.insert(funcionario);

        if (inseriu) {
            Toast.makeText(this, "Funcionário adicionado com sucesso", Toast.LENGTH_SHORT).show();
            atualizarAdapter(funcionarioDAO.getFuncionarioCursor());
        }
    }

    private void atualizarAdapter(Cursor cursor) {
        adapter.notifyDataSetChanged();
        adapter.swapCursor(cursor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_funcionarios, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.ic_pesquisar).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                atualizarAdapter(funcionarioDAO.find(query));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                atualizarAdapter(funcionarioDAO.find(newText));
                return true;
            }
        });
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add("Editar");
        menu.add("Excluir");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        long funcionarioId = adapter.getItemId(info.position);

        switch (item.getTitle().toString()) {

            case "Excluir":
                boolean excluiu = funcionarioDAO.delete(funcionarioId);
                if (excluiu) {
                    Toast.makeText(this, "Funcionário excluído com sucesso", Toast.LENGTH_SHORT).show();
                    atualizarAdapter(funcionarioDAO.getFuncionarioCursor());
                }
                return true;
            case "Editar":
                dialogEditarFuncionario(funcionarioDAO.get(funcionarioId));
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, DetalhesFuncionario.class);

        long funcionarioId = adapter.getItemId(i);
        intent.putExtra("funcionario_id", funcionarioId);

        startActivity(intent);
    }

    private void dialogEditarFuncionario(final Funcionario funcionario) {
        View view = getLayoutInflater().inflate(R.layout.dialog_atualizar_funcionario, null, false);

        tilNome = (TextInputLayout) view.findViewById(R.id.til_nome);
        tilCpf = (TextInputLayout) view.findViewById(R.id.til_cpf);
        tilCargo = (TextInputLayout) view.findViewById(R.id.til_cargo);
        tilSalario = (TextInputLayout) view.findViewById(R.id.til_salario);

        etNome = (EditText) view.findViewById(R.id.et_nome);
        etCpf = (EditText) view.findViewById(R.id.et_cpf);
        etCargo = (EditText) view.findViewById(R.id.et_cargo);
        etSalario = (CurrencyEditText) view.findViewById(R.id.et_salario);
        Locale brasil = new Locale("pt", "BR");
        etSalario.setLocale(brasil);

        Currency currency = Currency.getInstance(brasil);

        etNome.setText(funcionario.getNome());
        etCpf.setText(funcionario.getCpf());
        etCargo.setText(funcionario.getCargo().toString());
        etSalario.setText(CurrencyTextFormatter.formatText((funcionario.getSalario() * 100) + "", currency, brasil));


        etCargo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCargos();
            }
        });
        etCargo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    dialogCargos();
                }
            }
        });

        Button btnCancelar = (Button) view.findViewById(R.id.btn_cancelar);

        Button btnCadastrar = (Button) view.findViewById(R.id.btn_cadastrar);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.editar_funcionario))
                .setView(view);

        final AlertDialog dialog = builder.create();

        dialog.show();

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checarCampos()) {
                    atualizarFuncionario(funcionario);
                    dialog.dismiss();
                }
            }
        });
    }

    private void atualizarFuncionario(Funcionario funcionario) {
        String nome = etNome.getText().toString();
        String cpf = etCpf.getText().toString();
        String cargo = etCargo.getText().toString();
        double salario = Double.parseDouble((etSalario.getRawValue() / 100) + "");

        funcionario.setNome(nome);
        funcionario.setCpf(cpf);
        funcionario.setCargo(EnumCargos.valueOf(cargo));
        funcionario.setSalario(salario);

        boolean atualizou = funcionarioDAO.update(funcionario);

        if (atualizou) {
            Toast.makeText(this, "Funcionário atualizado com sucesso", Toast.LENGTH_SHORT).show();
            atualizarAdapter(funcionarioDAO.getFuncionarioCursor());
        }
    }

}
