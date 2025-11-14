package br.edu.utfpr.oo2.FinanSystem.dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T, K> {

    int cadastrar(T obj) throws SQLException;

    int atualizar(T obj) throws SQLException;

    int excluir(K chave) throws SQLException;

    T buscarPorId(K chave) throws SQLException;

    List<T> buscarTodos() throws SQLException;
}

