package br.edu.utfpr.oo2.FinanSystem.entities;

import java.time.LocalDate;

public class Usuario {
	private Integer id;
    private String nomeCompleto;
    private LocalDate dataNascimento;
    private String sexo;
    private String nomeUsuario;
    private String senha;

    public Usuario() {
    }

    public Usuario(Integer id, String nomeCompleto, LocalDate dataNascimento,
                   String sexo, String nomeUsuario, String senha) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
    }

    public Usuario(String nomeCompleto, LocalDate dataNascimento,
                   String sexo, String nomeUsuario, String senha) {
        this(null, nomeCompleto, dataNascimento, sexo, nomeUsuario, senha);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return nomeUsuario;
    }
}
