package br.edu.utfpr.oo2.FinanSystem.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.oo2.FinanSystem.entities.Usuario;

public class UsuarioDAO {
	public void inserir(Usuario usuario) throws SQLException, IOException {
        String sql = "INSERT INTO usuario "
                + "(nome_completo, data_nascimento, sexo, username, senha) "
                + "VALUES (?, ?, ?, ?, ?)";

        PreparedStatement st = null;
        
        try {
            Connection conn = BancoDados.conectar();
            
            st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, usuario.getNomeCompleto());
            st.setDate(2, Date.valueOf(usuario.getDataNascimento()));
            st.setString(3, usuario.getSexo());
            st.setString(4, usuario.getNomeUsuario());
            st.setString(5, usuario.getSenha());

            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                usuario.setId(rs.getInt(1));
            }
            BancoDados.finalizarResultSet(rs);
        } finally {
            BancoDados.finalizarStatement(st);
        }
    }

    public void atualizar(Usuario usuario) throws SQLException, IOException {
        String sql = "UPDATE usuario SET "
                + "nome_completo = ?, data_nascimento = ?, sexo = ?, "
                + "username = ?, senha = ? "
                + "WHERE id = ?";

        PreparedStatement st = null;
        try {
            Connection conn = BancoDados.conectar();
            st = conn.prepareStatement(sql);
            st.setString(1, usuario.getNomeCompleto());
            st.setDate(2, Date.valueOf(usuario.getDataNascimento()));
            st.setString(3, usuario.getSexo());
            st.setString(4, usuario.getNomeUsuario());
            st.setString(5, usuario.getSenha());
            st.setInt(6, usuario.getId());

            st.executeUpdate();
        } finally {
            BancoDados.finalizarStatement(st);
        }
    }

    public void excluir(Integer id) throws SQLException, IOException {
        String sql = "DELETE FROM usuario WHERE id = ?";

        PreparedStatement st = null;
        try {
            Connection conn = BancoDados.conectar();
            st = conn.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();
        } finally {
            BancoDados.finalizarStatement(st);
        }
    }

    public Usuario buscarPorId(Integer id) throws SQLException, IOException {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            Connection conn = BancoDados.conectar();
            st = conn.prepareStatement(sql);
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                return mapearUsuario(rs);
            }
            return null;
        } finally {
            BancoDados.finalizarResultSet(rs);
            BancoDados.finalizarStatement(st);
        }
    }

    public Usuario buscarPorNomeUsuario(String nomeUsuario) throws SQLException, IOException {
        String sql = "SELECT * FROM usuario WHERE username = ?";
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            Connection conn = BancoDados.conectar();
            st = conn.prepareStatement(sql);
            st.setString(1, nomeUsuario);
            rs = st.executeQuery();

            if (rs.next()) {
                return mapearUsuario(rs);
            }
            return null;
        } finally {
            BancoDados.finalizarResultSet(rs);
            BancoDados.finalizarStatement(st);
        }
    }

    public Usuario autenticar(String nomeUsuario, String senha) throws SQLException, IOException {
        String sql = "SELECT * FROM usuario WHERE username = ? AND senha = ?";
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            Connection conn = BancoDados.conectar();
            st = conn.prepareStatement(sql);
            st.setString(1, nomeUsuario);
            st.setString(2, senha);
            rs = st.executeQuery();

            if (rs.next()) {
                return mapearUsuario(rs);
            }
            return null;
        } finally {
            BancoDados.finalizarResultSet(rs);
            BancoDados.finalizarStatement(st);
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String nomeCompleto = rs.getString("nome_completo");
        LocalDate dataNascimento = rs.getDate("data_nascimento").toLocalDate();
        String sexo = rs.getString("sexo");
        String nomeUsuario = rs.getString("username");
        String senha = rs.getString("senha");

        return new Usuario(id, nomeCompleto, dataNascimento, sexo, nomeUsuario, senha);
    }
}
