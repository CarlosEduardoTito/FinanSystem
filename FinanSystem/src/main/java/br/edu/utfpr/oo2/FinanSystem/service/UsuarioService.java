package br.edu.utfpr.oo2.FinanSystem.service;

import java.io.IOException;
import java.sql.SQLException;
// TODO: Adicionar imports quando implementar relacionamento com Conta
// import java.util.List;
// import br.edu.utfpr.oo2.FinanSystem.entities.Conta;
// import br.edu.utfpr.oo2.FinanSystem.dao.ContaDAO;
// import br.edu.utfpr.oo2.FinanSystem.dao.BancoDados;
// import java.sql.Connection;

import br.edu.utfpr.oo2.FinanSystem.dao.UsuarioDAO;
import br.edu.utfpr.oo2.FinanSystem.entities.Usuario;

public class UsuarioService {
	
	private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public void cadastrar(Usuario usuario) throws Exception {
        try {
            Usuario existente = usuarioDAO.buscarPorNomeUsuario(usuario.getNomeUsuario());
            if (existente != null) {
            	
                throw new Exception("Nome de usuário já está em uso!");
            }
            
            usuarioDAO.inserir(usuario);

        } catch (SQLException | IOException e) {
            throw new Exception("Erro ao cadastrar usuário: " + e.getMessage(), e);
        }
    }

    public void atualizar(Usuario usuario) throws Exception {
        try {
            if (usuario.getId() == null) {
                throw new Exception("ID do usuário não pode ser nulo para atualização!");
            }
            
            Usuario existente = usuarioDAO.buscarPorNomeUsuario(usuario.getNomeUsuario());
            if (existente != null && !existente.getId().equals(usuario.getId())) {
                throw new Exception("Nome de usuário já está em uso!");
            }
            usuarioDAO.atualizar(usuario);

        } catch (SQLException | IOException e) {
            throw new Exception("Erro ao atualizar usuário: " + e.getMessage(), e);
        }
    }

    public void excluir(Integer id) throws Exception {
        try {
            
            usuarioDAO.excluir(id);
        } catch (SQLException | IOException e) {
            throw new Exception("Erro ao excluir usuário: " + e.getMessage(), e);
        }
    }

    public Usuario login(String nomeUsuario, String senha) throws Exception {
        try {
            Usuario usuario = usuarioDAO.autenticar(nomeUsuario, senha);

            if (usuario == null) {
                throw new Exception("Usuário ou senha inválidos.");
            }
            return usuario;

        } catch (SQLException | IOException e) {
            throw new Exception("Erro ao autenticar usuário: " + e.getMessage(), e);
        }
    }
}
