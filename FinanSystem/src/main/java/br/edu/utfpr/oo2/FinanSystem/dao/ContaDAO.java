package br.edu.utfpr.oo2.FinanSystem.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import br.edu.utfpr.oo2.FinanSystem.entities.Conta;

public class ContaDAO implements DAO<Conta, Integer> {

    private final Connection conn;

    public ContaDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public int cadastrar(Conta conta) throws SQLException {
        String sql = "INSERT INTO conta (userId, nomeBanco, agencia, numeroConta, saldoInicial, tipoConta) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, conta.getUserId());
            st.setString(2, conta.getNomeBanco());
            st.setString(3, conta.getAgencia());
            st.setInt(4, conta.getNumeroConta());
            st.setDouble(5, conta.getSaldoInicial());
            st.setString(6, conta.getTipoConta());
            return st.executeUpdate();
        }
    }

    @Override
    public int atualizar(Conta conta) throws SQLException {
        String sql = "UPDATE conta SET userId=?, nomeBanco=?, agencia=?, numeroConta=?, saldoInicial=?, tipoConta=? WHERE id=?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, conta.getUserId());
            st.setString(2, conta.getNomeBanco());
            st.setString(3, conta.getAgencia());
            st.setInt(4, conta.getNumeroConta());
            st.setDouble(5, conta.getSaldoInicial());
            st.setString(6, conta.getTipoConta());
            st.setInt(7, conta.getId());
            return st.executeUpdate();
        }
    }

    @Override
    public int excluir(Integer id) throws SQLException {
        String sql = "DELETE FROM conta WHERE id=?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate();
        }
    }

    @Override
    public Conta buscarPorId(Integer id) throws SQLException {
        String sql = "SELECT * FROM conta WHERE id=?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return montarObjeto(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Conta> buscarTodos() throws SQLException {
        String sql = "SELECT * FROM conta";
        List<Conta> lista = new ArrayList<>();
        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                lista.add(montarObjeto(rs));
            }
        }
        return lista;
    }

    public Conta buscarPorNumero(int numeroConta) throws SQLException {
        String sql = "SELECT * FROM conta WHERE numeroConta=?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, numeroConta);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return montarObjeto(rs);
                }
            }
        }
        return null;
    }

    private Conta montarObjeto(ResultSet rs) throws SQLException {
        return new Conta(
                rs.getInt("id"),
                rs.getInt("userId"),
                rs.getString("nomeBanco"),
                rs.getString("agencia"),
                rs.getInt("numeroConta"),
                rs.getDouble("saldoInicial"),
                rs.getString("tipoConta")
        );
    }
}
