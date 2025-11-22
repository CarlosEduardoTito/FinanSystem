package br.edu.utfpr.oo2.FinanSystem.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import br.edu.utfpr.oo2.FinanSystem.entities.Transacao;

public class TransacaoDAO implements DAO<Transacao, Integer> {

    private final Connection conn;

    public TransacaoDAO(Connection conn) {
        this.conn = conn;
    }

    public int cadastrar(Transacao t) throws SQLException {
        String sql = "INSERT INTO transacao (idConta, idCategoria, descricao, valor, data) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, t.getContaId());
            st.setInt(2, t.getCategoriaId());
            st.setString(3, t.getDescricao());
            st.setDouble(4, t.getValor());
            st.setDate(5, Date.valueOf(t.getData()));
            int rows = st.executeUpdate();
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    t.setId(rs.getInt(1));
                }
            }
            return rows;
        }
    }

    public int atualizar(Transacao t) throws SQLException {
        String sql = "UPDATE transacao SET idConta=?, idCategoria=?, descricao=?, valor=?, data=? WHERE id=?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, t.getContaId());
            st.setInt(2, t.getCategoriaId());
            st.setString(3, t.getDescricao());
            st.setDouble(4, t.getValor());
            st.setDate(5, Date.valueOf(t.getData()));
            st.setInt(6, t.getId());
            return st.executeUpdate();
        }
    }

    public int excluir(Integer id) throws SQLException {
        String sql = "DELETE FROM transacao WHERE id=?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate();
        }
    }

    public Transacao buscarPorId(Integer id) throws SQLException {
        String sql = "SELECT * FROM transacao WHERE id=?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    public List<Transacao> buscarTodos() throws SQLException {
        String sql = "SELECT * FROM transacao ORDER BY data DESC";
        List<Transacao> lista = new ArrayList<>();
        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public List<Transacao> buscarPorConta(int contaId) throws SQLException {
        String sql = "SELECT * FROM transacao WHERE idConta = ? ORDER BY data DESC";
        List<Transacao> lista = new ArrayList<>();
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, contaId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public List<Transacao> buscarPorCategoria(int categoriaId) throws SQLException {
        String sql = "SELECT * FROM transacao WHERE idCategoria = ? ORDER BY data DESC";
        List<Transacao> lista = new ArrayList<>();
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, categoriaId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public List<Transacao> buscarPorPeriodo(Date inicio, Date fim) throws SQLException {
        String sql = "SELECT * FROM transacao WHERE data BETWEEN ? AND ? ORDER BY data DESC";
        List<Transacao> lista = new ArrayList<>();
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setDate(1, inicio);
            st.setDate(2, fim);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    private Transacao mapear(ResultSet rs) throws SQLException {
        return new Transacao(
                rs.getInt("id"),
                rs.getInt("idConta"),
                rs.getInt("idCategoria"),
                rs.getString("descricao"),
                rs.getDouble("valor"),
                rs.getDate("data").toLocalDate()
        );

    }

}
