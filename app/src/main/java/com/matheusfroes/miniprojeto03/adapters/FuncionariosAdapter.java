package com.matheusfroes.miniprojeto03.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.matheusfroes.miniprojeto03.R;
import com.matheusfroes.miniprojeto03.db.DbHelper;
import com.matheusfroes.miniprojeto03.db.FuncionarioDAO;

public class FuncionariosAdapter extends CursorAdapter {


    public FuncionariosAdapter(Context context) {
        super(context, new FuncionarioDAO(context).getFuncionarioCursor(), true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.funcionario_view, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String nome = cursor.getString(cursor.getColumnIndex(DbHelper.FUNCIONARIO_NOME));
        String cpf = cursor.getString(cursor.getColumnIndex(DbHelper.FUNCIONARIO_CPF));

        TextView tvNome = (TextView) view.findViewById(R.id.tv_nome_funcionario);
        TextView tvCpf = (TextView) view.findViewById(R.id.tv_cpf_funcionario);

        tvNome.setText(nome);
        tvCpf.setText(cpf);
    }
}
