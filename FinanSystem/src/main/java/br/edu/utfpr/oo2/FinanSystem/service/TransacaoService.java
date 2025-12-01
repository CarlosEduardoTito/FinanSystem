package br.edu.utfpr.oo2.FinanSystem.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import br.edu.utfpr.oo2.FinanSystem.dao.BancoDados;
import br.edu.utfpr.oo2.FinanSystem.dao.ContaDAO;
import br.edu.utfpr.oo2.FinanSystem.dao.CategoriaDAO;
import br.edu.utfpr.oo2.FinanSystem.dao.TransacaoDAO;
import br.edu.utfpr.oo2.FinanSystem.entities.Conta;
import br.edu.utfpr.oo2.FinanSystem.entities.Categoria;
import br.edu.utfpr.oo2.FinanSystem.entities.Transacao;

public class TransacaoService {

    public void cadastrarTransacao(Transacao t) throws Exception {
        validarBasico(t);
        Connection conn = BancoDados.conectar();
        try {
            conn.setAutoCommit(false);

            TransacaoDAO transDao = new TransacaoDAO(conn);
            ContaDAO contaDao = new ContaDAO(conn);
            CategoriaDAO catDao = new CategoriaDAO(conn);

            Conta conta = contaDao.buscarPorId(t.getContaId());
            if (conta == null) throw new Exception("Conta não encontrada.");

            Categoria categoria = catDao.buscarPorId(t.getCategoriaId());
            if (categoria == null) throw new Exception("Categoria não encontrada.");

            transDao.cadastrar(t);
            ajustarSaldoPorCategoria(conta, categoria, t.getValor(), contaDao);

            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public void atualizarTransacao(Transacao t) throws Exception {
        validarBasico(t);
        Connection conn = BancoDados.conectar();
        try {
            conn.setAutoCommit(false);

            TransacaoDAO transDao = new TransacaoDAO(conn);
            ContaDAO contaDao = new ContaDAO(conn);
            CategoriaDAO catDao = new CategoriaDAO(conn);

            Transacao existente = transDao.buscarPorId(t.getId());
            if (existente == null) throw new Exception("Transação não encontrada.");

            reverterEAplicar(existente, t, contaDao, catDao, transDao);

            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public void excluirTransacao(Integer id) throws Exception {
        Connection conn = BancoDados.conectar();
        try {
            conn.setAutoCommit(false);

            TransacaoDAO transDao = new TransacaoDAO(conn);
            ContaDAO contaDao = new ContaDAO(conn);
            CategoriaDAO catDao = new CategoriaDAO(conn);

            Transacao existente = transDao.buscarPorId(id);
            if (existente == null) throw new Exception("Transação não encontrada.");

            Conta conta = contaDao.buscarPorId(existente.getContaId());
            if (conta == null) throw new Exception("Conta não encontrada.");

            Categoria categoria = catDao.buscarPorId(existente.getCategoriaId());
            if (categoria == null) throw new Exception("Categoria não encontrada.");

            transDao.excluir(id);
            ajustarSaldoPorCategoria(conta, categoria, -existente.getValor(), contaDao);

            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public Transacao buscarPorId(Integer id) throws SQLException, IOException {
        Connection conn = BancoDados.conectar();
        TransacaoDAO dao = new TransacaoDAO(conn);
        return dao.buscarPorId(id);
    }

    public List<Transacao> listarTransacoes() throws SQLException, IOException {
        Connection conn = BancoDados.conectar();
        TransacaoDAO dao = new TransacaoDAO(conn);
        return dao.buscarTodos();
    }

    public List<Transacao> listarPorConta(int contaId) throws SQLException, IOException {
        Connection conn = BancoDados.conectar();
        TransacaoDAO dao = new TransacaoDAO(conn);
        return dao.buscarPorConta(contaId);
    }

    public List<Transacao> listarPorCategoria(int categoriaId) throws SQLException, IOException {
        Connection conn = BancoDados.conectar();
        TransacaoDAO dao = new TransacaoDAO(conn);
        return dao.buscarPorCategoria(categoriaId);
    }

    public List<Transacao> listarPorPeriodo(LocalDate inicio, LocalDate fim) throws SQLException, IOException {
        Connection conn = BancoDados.conectar();
        TransacaoDAO dao = new TransacaoDAO(conn);
        return dao.buscarPorPeriodo(Date.valueOf(inicio), Date.valueOf(fim));
    }

    private void validarBasico(Transacao t) throws Exception {
        if (t.getContaId() == null) throw new Exception("Conta é obrigatória.");
        if (t.getCategoriaId() == null) throw new Exception("Categoria é obrigatória.");
        if (t.getValor() == null || t.getValor() <= 0) throw new Exception("Valor deve ser maior que zero.");
        if (t.getData() == null) throw new Exception("Data é obrigatória.");
    }

    private void ajustarSaldoPorCategoria(Conta conta, Categoria categoria, Double valor, ContaDAO contaDao) throws SQLException {
        if ("Entrada".equalsIgnoreCase(categoria.getTipo())) {
            conta.setSaldoInicial(conta.getSaldoInicial() + valor);
        } else {
            conta.setSaldoInicial(conta.getSaldoInicial() - valor);
        }
        contaDao.atualizar(conta);
    }

    private void reverterEAplicar(Transacao antiga, Transacao novaTrans, ContaDAO contaDao, CategoriaDAO catDao, TransacaoDAO transDao) throws Exception {
        Categoria catAntiga = catDao.buscarPorId(antiga.getCategoriaId());
        Categoria catNova = catDao.buscarPorId(novaTrans.getCategoriaId());
        Conta contaAntiga = contaDao.buscarPorId(antiga.getContaId());
        Conta contaNova = contaDao.buscarPorId(novaTrans.getContaId());
        if (catAntiga == null || catNova == null || contaAntiga == null || contaNova == null) {
            throw new Exception("Dados para atualização inválidos.");
        }

        // Reverte a transação antiga
        if ("Entrada".equalsIgnoreCase(catAntiga.getTipo())) {
            contaAntiga.setSaldoInicial(contaAntiga.getSaldoInicial() - antiga.getValor());
        } else {
            contaAntiga.setSaldoInicial(contaAntiga.getSaldoInicial() + antiga.getValor());
        }
        contaDao.atualizar(contaAntiga);

        // Aplica a nova transação
        if ("Entrada".equalsIgnoreCase(catNova.getTipo())) {
            contaNova.setSaldoInicial(contaNova.getSaldoInicial() + novaTrans.getValor());
        } else {
            contaNova.setSaldoInicial(contaNova.getSaldoInicial() - novaTrans.getValor());
        }
        contaDao.atualizar(contaNova);

        transDao.atualizar(novaTrans);
    }
}
