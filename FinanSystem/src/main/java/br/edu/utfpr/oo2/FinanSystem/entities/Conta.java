package br.edu.utfpr.oo2.FinanSystem.entities;

public class Conta {

    private Integer id;
    private Integer userId;
    private String nomeBanco;
    private String agencia;
    private Integer numeroConta;
    private Double saldoInicial;
    private String tipoConta;

    public Conta() {}

    public Conta(Integer id, Integer userId, String nomeBanco, String agencia, Integer numeroConta, Double saldoInicial, String tipoConta) {
        this.id = id;
        this.userId = userId;
        this.nomeBanco = nomeBanco;
        this.agencia = agencia;
        this.numeroConta = numeroConta;
        this.saldoInicial = saldoInicial;
        this.tipoConta = tipoConta;
    }

    public Conta(Integer userId, String nomeBanco, String agencia, Integer numeroConta, Double saldoInicial, String tipoConta) {
        this.userId = userId;
        this.nomeBanco = nomeBanco;
        this.agencia = agencia;
        this.numeroConta = numeroConta;
        this.saldoInicial = saldoInicial;
        this.tipoConta = tipoConta;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getNomeBanco() { return nomeBanco; }
    public void setNomeBanco(String nomeBanco) { this.nomeBanco = nomeBanco; }

    public String getAgencia() { return agencia; }
    public void setAgencia(String agencia) { this.agencia = agencia; }

    public Integer getNumeroConta() { return numeroConta; }
    public void setNumeroConta(Integer numeroConta) { this.numeroConta = numeroConta; }

    public Double getSaldoInicial() { return saldoInicial; }
    public void setSaldoInicial(Double saldoInicial) { this.saldoInicial = saldoInicial; }

    public String getTipoConta() { return tipoConta; }
    public void setTipoConta(String tipoConta) { this.tipoConta = tipoConta; }

    @Override
    public String toString() {
        return nomeBanco + " - " + numeroConta;
    }

}
