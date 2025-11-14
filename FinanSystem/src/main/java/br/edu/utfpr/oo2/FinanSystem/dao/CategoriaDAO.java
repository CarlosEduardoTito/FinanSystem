package br.edu.utfpr.oo2.FinanSystem.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import br.edu.utfpr.oo2.FinanSystem.entity.Categoria;

public class CategoriaDAO implements DAO<Categoria, Integer> {

    private Connection conn;

    public CategoriaDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public int cadastrar(Categoria categoria) throws SQLException {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO categoria (nome, tipo) VALUES (?, ?)"
            );
            st.setString(1, categoria.getNome());
            st.setString(2, categoria.getTipo());
            return st.executeUpdate();
        } finally {
            BancoDados.finalizarStatement(st);
            BancoDados.desconectar();
        }
    }

    @Override
    public int atualizar(Categoria categoria) throws SQLException {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE categoria SET nome=?, tipo=? WHERE id=?"
            );
            st.setString(1, categoria.getNome());
            st.setString(2, categoria.getTipo());
            st.setInt(3, categoria.getId());
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
            st = conn.prepareStatement("DELETE FROM categoria WHERE id=?");
            st.setInt(1, id);
            return st.executeUpdate();
        } finally {
            BancoDados.finalizarStatement(st);
            BancoDados.desconectar();
        }
    }

    @Override
    public Categoria buscarPorId(Integer id) throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM categoria WHERE id=?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                Categoria c = new Categoria();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setTipo(rs.getString("tipo"));
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
    public List<Categoria> buscarTodos() throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM categoria ORDER BY nome");
            rs = st.executeQuery();
            List<Categoria> lista = new ArrayList<>();
            while (rs.next()) {
                Categoria c = new Categoria();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setTipo(rs.getString("tipo"));
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
