package com.matheusfroes.miniprojeto03.models;

import java.io.Serializable;

public class Funcionario implements Serializable {
    private int id;
    private String nome, cpf;
    private double salario;
    private EnumCargos cargo;

    public Funcionario() {
    }

    public Funcionario(String nome, String cpf, double salario, EnumCargos cargo) {
        this.nome = nome;
        this.cpf = cpf;
        this.salario = salario;
        this.cargo = cargo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public EnumCargos getCargo() {
        return cargo;
    }

    public void setCargo(EnumCargos cargo) {
        this.cargo = cargo;
    }
}
