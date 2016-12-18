package com.matheusfroes.miniprojeto03.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NOME = "MiniProjeto03";
    private static final int DB_VERSAO = 1;

    public static final String TABELA_FUNCIONARIOS = "FUNCIONARIOS";
    public static final String FUNCIONARIO_ID = "_id";
    public static final String FUNCIONARIO_NOME = "NOME";
    public static final String FUNCIONARIO_CPF = "CPF";
    public static final String FUNCIONARIO_CARGO = "CARGO";
    public static final String FUNCIONARIO_SALARIO = "SALARIO";

    private static final String CREATE_TABLE_FUNCIONARIOS = "CREATE TABLE " + TABELA_FUNCIONARIOS + "(" +
            FUNCIONARIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            FUNCIONARIO_NOME + " TEXT NOT NULL," +
            FUNCIONARIO_CPF + " TEXT NOT NULL," +
            FUNCIONARIO_CARGO + " TEXT NOT NULL," +
            FUNCIONARIO_SALARIO + " FLOAT NOT NULL);";

    public DbHelper(Context context) {
        super(context, DB_NOME, null, DB_VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FUNCIONARIOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE " + TABELA_FUNCIONARIOS + ";");
        onCreate(db);
    }
}
