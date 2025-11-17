package br.edu.utfpr.oo2.FinanSystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.oo2.FinanSystem.entities.MetaFinanceira;

public class MetaFinanceiraDAO implements DAO<MetaFinanceira, Integer>{
	
	private Connection conn;

	public MetaFinanceiraDAO(Connection conn) {
		this.conn = conn;
	}

	@Override
	public int cadastrar(MetaFinanceira meta) throws SQLException {
		PreparedStatement st = null;
		try {
			
			st = conn.prepareStatement("INSERT INTO meta_financeira (usuarioId, descricao, valorMensal, tipoMeta) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			st.setInt(1, meta.getUsuarioId());
			st.setString(2, meta.getDescricao());
			st.setDouble(3, meta.getValorMensal());
			st.setString(4, meta.getTipoMeta());
			
			int linhasAfetadas = st.executeUpdate();
			
			if (linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					meta.setId(rs.getInt(1));
				}
				BancoDados.finalizarResultSet(rs);
			}
			return linhasAfetadas;
		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
		
	}

	@Override
	public int atualizar(MetaFinanceira meta) throws SQLException {
		
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("UPDATE meta_financeira SET usuarioId = ?, descricao = ?, valorMensal = ?, tipoMeta = ? WHERE id = ?");
			
			st.setInt(1, meta.getUsuarioId());
			st.setString(2, meta.getDescricao());
			st.setDouble(3, meta.getValorMensal());
			st.setString(4, meta.getTipoMeta());
			st.setInt(5, meta.getId());
			
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
			
			st = conn.prepareStatement("DELETE FROM meta_financeira WHERE id = ?");
			
			st.setInt(1, id);
			
			return st.executeUpdate();
			
		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}

	@Override
	public MetaFinanceira buscarPorId(Integer id) throws SQLException {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement("SELECT * FROM meta_finaceira where id = ?");
			
			st.setInt(1, id);
			
			rs = st.executeQuery();
			
			if (rs.next()) {
				return mapear(rs);
			}
			
			return null;
			
		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			BancoDados.desconectar();
		}
	}

	@Override
	public List<MetaFinanceira> buscarTodos() throws SQLException {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		List<MetaFinanceira> lista = new ArrayList<>();
		
		try {
			st = conn.prepareStatement("SELECT * FROM meta_financeira ORDER BY descricao");
			rs = st.executeQuery();
			
			while(rs.next()) {
				
				lista.add(mapear(rs));
				
			}
			
			return lista;
			
		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			BancoDados.desconectar();
		}
		
	}

	public List<MetaFinanceira> buscarPorUsuarioId (Integer usuarioId) throws SQLException {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		List<MetaFinanceira> lista = new ArrayList<>();
		
		try {
			
			st = conn.prepareStatement("SELECT * FROM meta_financeira WHERE usuarioid = ? ORDER BY descricao");
			
			st.setInt(1, usuarioId);
			rs = st.executeQuery();
			
			while(rs.next()) {
				
				lista.add(mapear(rs));
				
			}
			
			return lista;
			
		} finally {
			
			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			BancoDados.desconectar();
		}
	}
	
	private MetaFinanceira mapear(ResultSet rs) throws SQLException {
		
		MetaFinanceira meta = new MetaFinanceira();
		
		meta.setId(rs.getInt("id"));
		meta.setUsuarioId(rs.getInt("usuarioId"));
		meta.setDescricao(rs.getString("descricao"));
		meta.setValorMensal(rs.getDouble("valorMensal"));
		meta.setTipoMeta(rs.getString("tipoMeta"));
		return meta;
		
	}
}
