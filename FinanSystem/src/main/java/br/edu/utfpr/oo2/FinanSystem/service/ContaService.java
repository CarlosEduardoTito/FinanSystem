package br.edu.utfpr.oo2.FinanSystem.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import br.edu.utfpr.oo2.FinanSystem.dao.ContaDAO;
import br.edu.utfpr.oo2.FinanSystem.entities.Conta;
import br.edu.utfpr.oo2.FinanSystem.dao.BancoDados;
import java.sql.Connection;

public class ContaService {

    public void cadastrarConta(Conta conta) throws Exception {
        validar(conta);
        try (Connection conn = BancoDados.conectar()) {
            ContaDAO dao = new ContaDAO(conn);
            if (dao.buscarPorNumero(conta.getNumeroConta()) != null) {
                throw new Exception("Já existe uma conta com esse número.");
            }
            dao.cadastrar(conta);
        }
    }

    public void atualizarConta(Conta conta) throws Exception {
        validar(conta);
        try (Connection conn = BancoDados.conectar()) {
            ContaDAO dao = new ContaDAO(conn);
            dao.atualizar(conta);
        }
    }

    public void excluirConta(Integer id) throws SQLException, IOException {
        try (Connection conn = BancoDados.conectar()) {
            ContaDAO dao = new ContaDAO(conn);
            dao.excluir(id);
        }
    }

    public Conta buscarPorId(Integer id) throws SQLException, IOException {
        try (Connection conn = BancoDados.conectar()) {
            ContaDAO dao = new ContaDAO(conn);
            return dao.buscarPorId(id);
        }
    }

    public List<Conta> listarContas() throws SQLException, IOException {
        try (Connection conn = BancoDados.conectar()) {
            ContaDAO dao = new ContaDAO(conn);
            return dao.buscarTodos();
        }
    }

    private void validar(Conta c) throws Exception {
        if (c.getNomeBanco() == null || c.getNomeBanco().isBlank())
            throw new Exception("O nome do banco é obrigatório.");
        if (c.getAgencia() == null || c.getAgencia().isBlank())
            throw new Exception("A agência é obrigatória.");
        if (c.getNumeroConta() == null)
            throw new Exception("Número da conta é obrigatório.");
        if (c.getSaldoInicial() == null)
            throw new Exception("Saldo inicial é obrigatório.");
        if (c.getSaldoInicial() < 0)
            throw new Exception("Saldo inicial não pode ser negativo.");
        if (c.getTipoConta() == null || c.getTipoConta().isBlank())
            throw new Exception("O tipo da conta é obrigatório.");
    }
}
