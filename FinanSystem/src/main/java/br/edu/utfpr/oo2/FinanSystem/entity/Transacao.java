package br.edu.utfpr.oo2.FinanSystem.entity;

import java.time.LocalDate;

public class Transacao {


    private int id;
    private int contaId;
    private int categoriaId;
    private double valor;
    private LocalDate data;
    private String tipo;
    private String descricao;

    public Transacao() {

    }

    public Transacao(int id, int contaId, int categoriaId, double valor, LocalDate data, String tipo,
                     String descricao) {
        super();
        this.id = id;
        this.contaId = contaId;
        this.categoriaId = categoriaId;
        this.valor = valor;
        this.data = data;
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContaId() {
        return contaId;
    }

    public void setContaId(int contaId) {
        this.contaId = contaId;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}
