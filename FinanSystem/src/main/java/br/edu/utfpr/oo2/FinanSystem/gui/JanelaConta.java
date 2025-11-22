package br.edu.utfpr.oo2.FinanSystem.gui;

import br.edu.utfpr.oo2.FinanSystem.entities.Conta;
import br.edu.utfpr.oo2.FinanSystem.service.ContaService;

import javax.swing.*;
import java.awt.*;

public class JanelaConta extends JDialog {

    private final ContaService contaService = new ContaService();

    public JanelaConta(Frame owner, boolean modal) {
        super(owner, modal);
        init();
    }

    private void init() {
        setTitle("FinanSystem - Contas");
        setSize(400, 250);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new GridLayout(4, 1, 10, 10));

        JButton btnAdd = new JButton("Adicionar Conta");
        JButton btnEdit = new JButton("Editar Conta");
        JButton btnDelete = new JButton("Excluir Conta");
        JButton btnFechar = new JButton("Fechar");

        painel.add(btnAdd);
        painel.add(btnEdit);
        painel.add(btnDelete);

        add(painel, BorderLayout.CENTER);
        add(btnFechar, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> adicionar());
        btnEdit.addActionListener(e -> editar());
        btnDelete.addActionListener(e -> excluir());
        btnFechar.addActionListener(e -> dispose());
    }

    private Conta coletarDados(Conta existente) {
        String nomeBanco = JOptionPane.showInputDialog(this, "Nome do Banco:",
                existente != null ? existente.getNomeBanco() : "");
        if (nomeBanco == null) return null;

        String agencia = JOptionPane.showInputDialog(this, "Agência:",
                existente != null ? existente.getAgencia() : "");
        if (agencia == null) return null;

        String numeroStr = JOptionPane.showInputDialog(this, "Número da Conta:",
                existente != null ? existente.getNumeroConta() : "");
        if (numeroStr == null) return null;

        String saldoStr = JOptionPane.showInputDialog(this, "Saldo Inicial:",
                existente != null ? existente.getSaldoInicial() : "");
        if (saldoStr == null) return null;

        String[] tipos = {"Corrente", "Poupança", "Salário", "Investimento"};
        String tipo = (String) JOptionPane.showInputDialog(
                this, "Tipo da Conta:", "Tipo",
                JOptionPane.QUESTION_MESSAGE, null, tipos,
                existente != null ? existente.getTipoConta() : tipos[0]
        );
        if (tipo == null) return null;

        Conta c = existente != null ? existente : new Conta();
        c.setNomeBanco(nomeBanco.trim());
        c.setAgencia(agencia.trim());
        c.setNumeroConta(Integer.parseInt(numeroStr.trim()));
        c.setSaldoInicial(Double.parseDouble(saldoStr.trim()));
        c.setTipoConta(tipo);

        return c;
    }

    private void adicionar() {
        try {
            Conta conta = coletarDados(null);
            if (conta == null) return;
            contaService.cadastrarConta(conta);
            JOptionPane.showMessageDialog(this, "Conta cadastrada com sucesso.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void editar() {
        try {
            String idStr = JOptionPane.showInputDialog(this, "ID da Conta:");
            if (idStr == null) return;

            Conta conta = contaService.buscarPorId(Integer.parseInt(idStr));
            if (conta == null) {
                JOptionPane.showMessageDialog(this, "Conta não encontrada.");
                return;
            }

            Conta atualizada = coletarDados(conta);
            if (atualizada == null) return;

            contaService.atualizarConta(atualizada);
            JOptionPane.showMessageDialog(this, "Conta atualizada com sucesso.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void excluir() {
        try {
            String idStr = JOptionPane.showInputDialog(this, "ID da Conta:");
            if (idStr == null) return;

            contaService.excluirConta(Integer.parseInt(idStr));
            JOptionPane.showMessageDialog(this, "Conta excluída com sucesso.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
