package com.matheusfroes.miniprojeto03.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.matheusfroes.miniprojeto03.models.EnumCargos;
import com.matheusfroes.miniprojeto03.models.Funcionario;

public class FuncionarioDAO {
    private SQLiteDatabase db;


    public FuncionarioDAO(Context context) {
        db = new DbHelper(context).getWritableDatabase();
    }

    public boolean insert(Funcionario funcionario) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.FUNCIONARIO_NOME, funcionario.getNome());
        cv.put(DbHelper.FUNCIONARIO_CPF, funcionario.getCpf());
        cv.put(DbHelper.FUNCIONARIO_CARGO, funcionario.getCargo().toString());
        cv.put(DbHelper.FUNCIONARIO_SALARIO, funcionario.getSalario());

        long inseriu = db.insert(DbHelper.TABELA_FUNCIONARIOS, null, cv);

        return inseriu != -1;
    }

    public boolean delete(long funcionarioId) {
        int rows = db.delete(DbHelper.TABELA_FUNCIONARIOS,
                DbHelper.FUNCIONARIO_ID + " = ?", new String[]{String.valueOf(funcionarioId)});

        return rows != 0;
    }

    public Funcionario get(long funcionarioId) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbHelper.TABELA_FUNCIONARIOS + " WHERE " + DbHelper.FUNCIONARIO_ID + " = ?",
                new String[]{String.valueOf(funcionarioId)});

        Funcionario funcionario = null;
        if (cursor.moveToFirst()) {
            do {
                funcionario = new Funcionario();

                funcionario.setId(cursor.getInt(cursor.getColumnIndex(DbHelper.FUNCIONARIO_ID)));
                funcionario.setNome(cursor.getString(cursor.getColumnIndex(DbHelper.FUNCIONARIO_NOME)));
                funcionario.setCpf(cursor.getString(cursor.getColumnIndex(DbHelper.FUNCIONARIO_CPF)));
                funcionario.setCargo(EnumCargos.valueOf(cursor.getString(cursor.getColumnIndex(DbHelper.FUNCIONARIO_CARGO))));
                funcionario.setSalario(cursor.getDouble(cursor.getColumnIndex(DbHelper.FUNCIONARIO_SALARIO)));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return funcionario;
    }

    public boolean update(Funcionario funcionario) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.FUNCIONARIO_NOME, funcionario.getNome());
        cv.put(DbHelper.FUNCIONARIO_CPF, funcionario.getCpf());
        cv.put(DbHelper.FUNCIONARIO_CARGO, funcionario.getCargo().toString());
        cv.put(DbHelper.FUNCIONARIO_SALARIO, funcionario.getSalario());

        int rows = db.update(DbHelper.TABELA_FUNCIONARIOS, cv, DbHelper.FUNCIONARIO_ID + " = ?",
                new String[]{String.valueOf(funcionario.getId())});

        return rows > 0;
    }

    public Cursor getFuncionarioCursor() {
        return db.rawQuery("SELECT * FROM " + DbHelper.TABELA_FUNCIONARIOS, null);
    }

    public Cursor find(String query) {
        return db.rawQuery("SELECT * FROM " + DbHelper.TABELA_FUNCIONARIOS +
                " WHERE " + DbHelper.FUNCIONARIO_NOME + " LIKE ?" + " OR " +
                DbHelper.FUNCIONARIO_CPF + " LIKE ?", new String[]{"%" + query + "%", "%" + query + "%"});
    }

    public int getQuantidadeFuncionariosPorCargo(EnumCargos cargo) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DbHelper.TABELA_FUNCIONARIOS + " WHERE " + DbHelper.FUNCIONARIO_CARGO + " = ?"
                , new String[] {cargo.toString()});

        cursor.moveToFirst();
        int quantidade = cursor.getInt(0);
        cursor.close();
        return quantidade;
    }
}
