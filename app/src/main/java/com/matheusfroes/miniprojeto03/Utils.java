package com.matheusfroes.miniprojeto03;

import android.content.Context;
import android.content.SharedPreferences;

import com.matheusfroes.miniprojeto03.db.FuncionarioDAO;
import com.matheusfroes.miniprojeto03.models.EnumCargos;

public class Utils {


    public static double calcularBonusFuncionario(Context context, EnumCargos cargo, float lucroTotal) {
        FuncionarioDAO dao = new FuncionarioDAO(context);
        int qtdFuncionarios = dao.getQuantidadeFuncionariosPorCargo(cargo);

        double bonus = 0.0;
        switch (cargo) {

            case ATENDIMENTO:
                bonus = 1.0;
                break;
            case DESIGNER:
                bonus = 1.5;
                break;
            case GERENTE:
                bonus = 3.0;
                break;
            case PROGRAMADOR:
                bonus = 1.5;
                break;
        }

        double bonusTotal = bonus / qtdFuncionarios;

        bonusTotal *= lucroTotal;

        return bonusTotal;
    }
}
