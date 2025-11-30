package br.edu.utfpr.oo2.FinanSystem.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import br.edu.utfpr.oo2.FinanSystem.dao.BancoDados;
import br.edu.utfpr.oo2.FinanSystem.dao.MetaFinanceiraDAO;
import br.edu.utfpr.oo2.FinanSystem.entities.MetaFinanceira;

public class MetaFinanceiraService {

	public void cadastrar(MetaFinanceira meta) throws SQLException, IOException, Exception {
		
		if(meta.getValorMensal() <= 0) {
			
			throw new Exception("O valor mensal deve ser maior que zero.");
			
		}
		if (meta.getDescricao() == null || meta.getDescricao().trim().isEmpty()) {
			
			throw new Exception("A descrição é obrigatória.");
			
		}
		
		Connection conn = BancoDados.conectar();
		
		MetaFinanceiraDAO dao = new MetaFinanceiraDAO(conn);
		dao.cadastrar(meta);
		
	}
	
	public void excluir(Integer id) throws SQLException, IOException {
		
		Connection conn = BancoDados.conectar();
		MetaFinanceiraDAO dao = new MetaFinanceiraDAO(conn);
		dao.excluir(id);
		
	}
	
	public void atualizar(MetaFinanceira meta) throws SQLException, IOException, Exception {
        if(meta.getValorMensal() <= 0) {
            throw new Exception("O valor mensal deve ser maior que zero.");
        }
        if (meta.getDescricao() == null || meta.getDescricao().trim().isEmpty()) {
            throw new Exception("A descrição é obrigatória.");
        }
        
        Connection conn = BancoDados.conectar();
        try {
            MetaFinanceiraDAO dao = new MetaFinanceiraDAO(conn);
            dao.atualizar(meta);
        } finally {
            
        	BancoDados.desconectar();
        	
        }
    }
	
	public List<MetaFinanceira> listarPorUsuario(Integer usuarioId) throws SQLException, IOException {
		
		Connection conn = BancoDados.conectar();
		MetaFinanceiraDAO dao = new MetaFinanceiraDAO(conn);
		return dao.buscarPorUsuarioId(usuarioId);
		
	}
	
}
