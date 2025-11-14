package br.edu.utfpr.oo2.FinanSystem.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import br.edu.utfpr.oo2.FinanSystem.entity.Conta;

public class ContaDAO implements DAO<Conta, Integer> {

    private Connection conn;

    public ContaDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public int cadastrar(Conta conta) throws SQLException {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO conta (userId, nomeBanco, agencia, numeroConta, saldoInicial, tipoConta) VALUES (?, ?, ?, ?, ?, ?)"
            );
            st.setInt(1, conta.getUserId());
            st.setString(2, conta.getNomeBanco());
            st.setString(3, conta.getAgencia());
            st.setInt(4, conta.getNumeroConta());
            st.setDouble(5, conta.getSaldoInicial());
            st.setString(6, conta.getTipoConta());
            return st.executeUpdate();
        } finally {
            BancoDados.finalizarStatement(st);
            BancoDados.desconectar();
        }
    }

    @Override
    public int atualizar(Conta conta) throws SQLException {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE conta SET userId=?, nomeBanco=?, agencia=?, numeroConta=?, saldoInicial=?, tipoConta=? WHERE id=?"
            );
            st.setInt(1, conta.getUserId());
            st.setString(2, conta.getNomeBanco());
            st.setString(3, conta.getAgencia());
            st.setInt(4, conta.getNumeroConta());
            st.setDouble(5, conta.getSaldoInicial());
            st.setString(6, conta.getTipoConta());
            st.setInt(7, conta.getId());
            return st.executeUpdate();
        } finally {
            BancoDados.finalizarStatement(st);
            BancoDados.desconectar();
        }
    }

    @Override
    public int excluir(Integer id) throws SQLException {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM conta WHERE id=?");
            st.setInt(1, id);
            return st.executeUpdate();
        } finally {
            BancoDados.finalizarStatement(st);
            BancoDados.desconectar();
        }
    }

    @Override
    public Conta buscarPorId(Integer id) throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM conta WHERE id=?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                Conta c = new Conta();
                c.setId(rs.getInt("id"));
                c.setUserId(rs.getInt("userId"));
                c.setNomeBanco(rs.getString("nomeBanco"));
                c.setAgencia(rs.getString("agencia"));
                c.setNumeroConta(rs.getInt("numeroConta"));
                c.setSaldoInicial(rs.getDouble("saldoInicial"));
                c.setTipoConta(rs.getString("tipoConta"));
                return c;
            }
            return null;
        } finally {
            BancoDados.finalizarStatement(st);
            BancoDados.finalizarResultSet(rs);
            BancoDados.desconectar();
        }
    }

    @Override
    public List<Conta> buscarTodos() throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM conta");
            rs = st.executeQuery();
            List<Conta> lista = new ArrayList<>();
            while (rs.next()) {
                Conta c = new Conta();
                c.setId(rs.getInt("id"));
                c.setUserId(rs.getInt("userId"));
                c.setNomeBanco(rs.getString("nomeBanco"));
                c.setAgencia(rs.getString("agencia"));
                c.setNumeroConta(rs.getInt("numeroConta"));
                c.setSaldoInicial(rs.getDouble("saldoInicial"));
                c.setTipoConta(rs.getString("tipoConta"));
                lista.add(c);
            }
            return lista;
        } finally {
            BancoDados.finalizarStatement(st);
            BancoDados.finalizarResultSet(rs);
            BancoDados.desconectar();
        }
    }
}
