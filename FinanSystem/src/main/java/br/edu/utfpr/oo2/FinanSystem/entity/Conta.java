package br.edu.utfpr.oo2.FinanSystem.entity;

public class Conta {

    private int id;
    private int userId;
    private String nomeBanco;
    private String agencia;
    private int numeroConta;
    private double saldoInicial;
    private String tipoConta;

    public Conta() {

    }

    public Conta(int id, int userId, String nomeBanco, String agencia, int numeroConta, double saldoInicial,
                 String tipoConto) {
        super();
        this.id = id;
        this.userId = userId;
        this.nomeBanco = nomeBanco;
        this.agencia = agencia;
        this.numeroConta = numeroConta;
        this.saldoInicial = saldoInicial;
        this.tipoConta = tipoConto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNomeBanco() {
        return nomeBanco;
    }

    public void setNomeBanco(String nomeBanco) {
        this.nomeBanco = nomeBanco;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public int getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(int numeroConta) {
        this.numeroConta = numeroConta;
    }

    public double getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public String getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(String tipoConta) {
        this.tipoConta = tipoConta;
    }

}
