package br.edu.utfpr.oo2.FinanSystem.entities;

import java.time.LocalDate;

public class Transacao {


    private Integer id;
    private Integer contaId;
    private Integer categoriaId;
    private String descricao;
    private Double valor;
    private LocalDate data;

    public Transacao() {}

    public Transacao(Integer id, Integer contaId, Integer categoriaId, String descricao, Double valor, LocalDate data) {
        this.id = id;
        this.contaId = contaId;
        this.categoriaId = categoriaId;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
    }

    public Transacao(Integer contaId, Integer categoriaId, String descricao, Double valor, LocalDate data) {
        this.contaId = contaId;
        this.categoriaId = categoriaId;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getContaId() { return contaId; }
    public void setContaId(Integer contaId) { this.contaId = contaId; }

    public Integer getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Integer categoriaId) { this.categoriaId = categoriaId; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    @Override
    public String toString() {
        return data + " | " + descricao + " | " + valor;
    }
}
