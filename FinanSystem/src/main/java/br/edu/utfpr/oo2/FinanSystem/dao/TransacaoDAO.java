package br.edu.utfpr.oo2.FinanSystem.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import br.edu.utfpr.oo2.FinanSystem.entity.Transacao;

public class TransacaoDAO implements DAO<Transacao, Integer> {

    private Connection conn;

    public TransacaoDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public int cadastrar(Transacao transacao) throws SQLException {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO transacao (contaId, categoriaId, valor, data, tipo, descricao) VALUES (?, ?, ?, ?, ?, ?)"
            );
            st.setInt(1, transacao.getContaId());
            st.setInt(2, transacao.getCategoriaId());
            st.setDouble(3, transacao.getValor());
            st.setDate(4, java.sql.Date.valueOf(transacao.getData()));
            st.setString(5, transacao.getTipo());
            st.setString(6, transacao.getDescricao());
            return st.executeUpdate();
        } finally {
            BancoDados.finalizarStatement(st);
            BancoDados.desconectar();
        }
    }

    @Override
    public int atualizar(Transacao transacao) throws SQLException {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE transacao SET contaId=?, categoriaId=?, valor=?, data=?, tipo=?, descricao=? WHERE id=?"
            );
            st.setInt(1, transacao.getContaId());
            st.setInt(2, transacao.getCategoriaId());
            st.setDouble(3, transacao.getValor());
            st.setDate(4, java.sql.Date.valueOf(transacao.getData()));
            st.setString(5, transacao.getTipo());
            st.setString(6, transacao.getDescricao());
            st.setInt(7, transacao.getId());
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
            st = conn.prepareStatement("DELETE FROM transacao WHERE id = ?");
            st.setInt(1, id);
            return st.executeUpdate();
        } finally {
            BancoDados.finalizarStatement(st);
            BancoDados.desconectar();
        }
    }

    @Override
    public Transacao buscarPorId(Integer id) throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM transacao WHERE id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                Transacao t = new Transacao();
                t.setId(rs.getInt("id"));
                t.setContaId(rs.getInt("contaId"));
                t.setCategoriaId(rs.getInt("categoriaId"));
                t.setValor(rs.getDouble("valor"));
                t.setData(rs.getDate("data").toLocalDate());
                t.setTipo(rs.getString("tipo"));
                t.setDescricao(rs.getString("descricao"));
                return t;
            }
            return null;
        } finally {
            BancoDados.finalizarStatement(st);
            BancoDados.finalizarResultSet(rs);
            BancoDados.desconectar();
        }
    }

    @Override
    public List<Transacao> buscarTodos() throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM transacao ORDER BY data DESC");
            rs = st.executeQuery();
            List<Transacao> lista = new ArrayList<>();
            while (rs.next()) {
                Transacao t = new Transacao();
                t.setId(rs.getInt("id"));
                t.setContaId(rs.getInt("contaId"));
                t.setCategoriaId(rs.getInt("categoriaId"));
                t.setValor(rs.getDouble("valor"));
                t.setData(rs.getDate("data").toLocalDate());
                t.setTipo(rs.getString("tipo"));
                t.setDescricao(rs.getString("descricao"));
                lista.add(t);
            }
            return lista;
        } finally {
            BancoDados.finalizarStatement(st);
            BancoDados.finalizarResultSet(rs);
            BancoDados.desconectar();
        }
    }
}
