package br.edu.utfpr.oo2.FinanSystem.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import br.edu.utfpr.oo2.FinanSystem.entities.Categoria;

public class CategoriaDAO implements DAO<Categoria, Integer> {

    private final Connection conn;

    public CategoriaDAO(Connection conn) {
        this.conn = conn;
    }

    public int cadastrar(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO categoria (nome, tipo) VALUES (?, ?)";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, categoria.getNome());
            st.setString(2, categoria.getTipo());
            return st.executeUpdate();
        }
    }


    public int atualizar(Categoria categoria) throws SQLException {
        String sql = "UPDATE categoria SET nome=?, tipo=? WHERE id=?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, categoria.getNome());
            st.setString(2, categoria.getTipo());
            st.setInt(3, categoria.getId());
            return st.executeUpdate();
        }
    }


    public int excluir(Integer id) throws SQLException {
        String sql = "DELETE FROM categoria WHERE id=?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate();
        }
    }


    public Categoria buscarPorId(Integer id) throws SQLException {
        String sql = "SELECT * FROM categoria WHERE id=?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return mapearCategoria(rs);
                }
            }
        }
        return null;
    }


    public Categoria buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM categoria WHERE nome=?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, nome);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return mapearCategoria(rs);
                }
            }
        }
        return null;
    }

    public List<Categoria> buscarTodos() throws SQLException {
        String sql = "SELECT * FROM categoria ORDER BY nome";

        List<Categoria> lista = new ArrayList<>();

        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearCategoria(rs));
            }
        }

        return lista;
    }


    private Categoria mapearCategoria(ResultSet rs) throws SQLException {
        Categoria c = new Categoria();
        c.setId(rs.getInt("id"));
        c.setNome(rs.getString("nome"));
        c.setTipo(rs.getString("tipo"));
        return c;
    }
}
