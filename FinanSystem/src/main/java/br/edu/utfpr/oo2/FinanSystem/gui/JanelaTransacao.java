package br.edu.utfpr.oo2.FinanSystem.gui;

import br.edu.utfpr.oo2.FinanSystem.entities.Transacao;
import br.edu.utfpr.oo2.FinanSystem.service.TransacaoService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class JanelaTransacao extends JDialog {

    private final TransacaoService service = new TransacaoService();

    public JanelaTransacao(Frame owner, boolean modal) {
        super(owner, modal);
        init();
    }

    private void init() {
        setTitle("FinanSystem - Transações");
        setSize(480, 300);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new GridLayout(4, 1, 10, 10));

        JButton btnAdd = new JButton("Adicionar Transação");
        JButton btnEdit = new JButton("Editar Transação");
        JButton btnDelete = new JButton("Excluir Transação");
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

    private void adicionar() {
        try {
            int contaId = Integer.parseInt(JOptionPane.showInputDialog(this, "ID da Conta:"));
            int categoriaId = Integer.parseInt(JOptionPane.showInputDialog(this, "ID da Categoria:"));

            String descricao = JOptionPane.showInputDialog(this, "Descrição:");
            if (descricao == null) descricao = "";

            double valor = Double.parseDouble(
                    JOptionPane.showInputDialog(this, "Valor:")
            );

            LocalDate data = LocalDate.parse(
                    JOptionPane.showInputDialog(this, "Data (AAAA-MM-DD):")
            );

            Transacao t = new Transacao(contaId, categoriaId, descricao, valor, data);
            service.cadastrarTransacao(t);

            JOptionPane.showMessageDialog(this, "Transação cadastrada com sucesso.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void editar() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog(this, "ID da Transação:"));
            Transacao existente = service.buscarPorId(id);

            if (existente == null) {
                JOptionPane.showMessageDialog(this, "Transação não encontrada.");
                return;
            }

            int contaId = Integer.parseInt(JOptionPane.showInputDialog(this, "ID da Conta:", existente.getContaId()));
            int categoriaId = Integer.parseInt(JOptionPane.showInputDialog(this, "ID da Categoria:", existente.getCategoriaId()));

            String descricao = JOptionPane.showInputDialog(this, "Descrição:", existente.getDescricao());
            if (descricao == null) descricao = "";

            double valor = Double.parseDouble(
                    JOptionPane.showInputDialog(this, "Valor:", existente.getValor())
            );

            LocalDate data = LocalDate.parse(
                    JOptionPane.showInputDialog(this, "Data (AAAA-MM-DD):", existente.getData().toString())
            );

            Transacao nova = new Transacao(id, contaId, categoriaId, descricao, valor, data);

            service.atualizarTransacao(nova);

            JOptionPane.showMessageDialog(this, "Transação atualizada com sucesso.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void excluir() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog(this, "ID da Transação:"));
            service.excluirTransacao(id);
            JOptionPane.showMessageDialog(this, "Transação excluída com sucesso.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
