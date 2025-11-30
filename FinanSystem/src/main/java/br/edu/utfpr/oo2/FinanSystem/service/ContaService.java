package br.edu.utfpr.oo2.FinanSystem.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import br.edu.utfpr.oo2.FinanSystem.dao.ContaDAO;
import br.edu.utfpr.oo2.FinanSystem.entities.Conta;
import br.edu.utfpr.oo2.FinanSystem.dao.BancoDados;

public class ContaService {

    public void cadastrarConta(Conta conta) throws Exception {
        validar(conta);

        Connection conn = BancoDados.conectar();
        ContaDAO dao = new ContaDAO(conn);

        if (dao.buscarPorNumero(conta.getNumeroConta()) != null)
            throw new Exception("Já existe uma conta com esse número.");

        int linhas = dao.cadastrar(conta);
        if (linhas == 0)
            throw new Exception("Erro ao cadastrar conta.");
    }

    public void atualizarConta(Conta conta) throws Exception {
        validar(conta);

        Connection conn = BancoDados.conectar();
        ContaDAO dao = new ContaDAO(conn);

        int linhas = dao.atualizar(conta);
        if (linhas == 0)
            throw new Exception("Nenhuma conta foi atualizada.");
    }

    public void excluirConta(Integer id) throws Exception {
        Connection conn = BancoDados.conectar();
        ContaDAO dao = new ContaDAO(conn);

        int linhas = dao.excluir(id);
        if (linhas == 0)
            throw new Exception("A conta não existe.");
    }

    public Conta buscarPorId(Integer id) throws SQLException, IOException {
        Connection conn = BancoDados.conectar();
        ContaDAO dao = new ContaDAO(conn);
        return dao.buscarPorId(id);
    }

    public List<Conta> listarContas() throws SQLException, IOException {
        Connection conn = BancoDados.conectar();
        ContaDAO dao = new ContaDAO(conn);
        return dao.buscarTodos();
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


    public void transferir(int idOrigem, int idDestino, double valor) throws Exception {

        if (idOrigem == idDestino)
            throw new Exception("A conta de origem e destino não podem ser iguais.");

        if (valor <= 0)
            throw new Exception("O valor da transferência deve ser maior que zero.");

        Connection conn = BancoDados.conectar();
        ContaDAO dao = new ContaDAO(conn);

        Conta origem = dao.buscarPorId(idOrigem);
        Conta destino = dao.buscarPorId(idDestino);

        if (origem == null)
            throw new Exception("Conta de origem não encontrada.");

        if (destino == null)
            throw new Exception("Conta de destino não encontrada.");

        if (origem.getSaldoInicial() < valor)
            throw new Exception("Saldo insuficiente na conta de origem.");


        origem.setSaldoInicial(origem.getSaldoInicial() - valor);
        destino.setSaldoInicial(destino.getSaldoInicial() + valor);


        dao.atualizar(origem);
        dao.atualizar(destino);
    }

}
