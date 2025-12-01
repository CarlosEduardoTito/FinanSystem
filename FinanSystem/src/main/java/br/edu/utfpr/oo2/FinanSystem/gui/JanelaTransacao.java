package br.edu.utfpr.oo2.FinanSystem.gui;

import br.edu.utfpr.oo2.FinanSystem.entities.Transacao;
import br.edu.utfpr.oo2.FinanSystem.service.TransacaoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class JanelaTransacao extends JDialog {

    private final TransacaoService service = new TransacaoService();
    private JTable tabela;
    private DefaultTableModel modelo;

    public JanelaTransacao(Frame owner, boolean modal) {
        super(owner, modal);
        init();
        carregarTabela();
    }

    private void init() {
        setTitle("FinanSystem - Transações");
        setSize(750, 450);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        String[] colunas = { "ID", "Conta", "Categoria", "Descrição", "Valor", "Data" };
        modelo = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tabela = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabela);

        JPanel painelBotoes = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton btnAdd = new JButton("Adicionar");
        JButton btnEdit = new JButton("Editar");
        JButton btnDelete = new JButton("Excluir");
        JButton btnFechar = new JButton("Fechar");

        painelBotoes.add(btnAdd);
        painelBotoes.add(btnEdit);
        painelBotoes.add(btnDelete);
        painelBotoes.add(btnFechar);

        add(scroll, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> adicionar());
        btnEdit.addActionListener(e -> editar());
        btnDelete.addActionListener(e -> excluir());
        btnFechar.addActionListener(e -> dispose());
    }

    private void carregarTabela() {

        TarefaComCarregamento.executar(
                (Frame) getOwner(),
                () -> {
                    List<Transacao> lista = service.listarTransacoes();
                    SwingUtilities.invokeLater(() -> {
                        modelo.setRowCount(0);
                        for (Transacao t : lista) {
                            modelo.addRow(new Object[]{
                                    t.getId(),
                                    t.getContaId(),
                                    t.getCategoriaId(),
                                    t.getDescricao(),
                                    t.getValor(),
                                    t.getData()
                            });
                        }
                    });
                },
                null
        );
    }

    private void adicionar() {

        try {
            int contaId = Integer.parseInt(JOptionPane.showInputDialog(this, "ID da Conta:"));
            int categoriaId = Integer.parseInt(JOptionPane.showInputDialog(this, "ID da Categoria:"));
            String descricao = JOptionPane.showInputDialog(this, "Descrição:");
            if (descricao == null) descricao = "";
            double valor = Double.parseDouble(JOptionPane.showInputDialog(this, "Valor:"));
            LocalDate data = LocalDate.parse(JOptionPane.showInputDialog(this, "Data (AAAA-MM-DD):"));

            Transacao nova = new Transacao(contaId, categoriaId, descricao, valor, data);

            TarefaComCarregamento.executar(
                    (Frame) getOwner(),
                    () -> service.cadastrarTransacao(nova),
                    () -> {
                        JOptionPane.showMessageDialog(this, "Transação cadastrada com sucesso.");
                        carregarTabela();
                    }
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void editar() {

        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog(this, "ID da Transação:"));

            TarefaComCarregamento.executarComRetorno(
                    (Frame) getOwner(),
                    () -> service.buscarPorId(id),
                    existente -> {

                        if (existente == null) {
                            JOptionPane.showMessageDialog(this, "Transação não encontrada.");
                            return;
                        }

                        try {
                            int contaId = Integer.parseInt(JOptionPane.showInputDialog(this, "ID da Conta:", existente.getContaId()));
                            int categoriaId = Integer.parseInt(JOptionPane.showInputDialog(this, "ID da Categoria:", existente.getCategoriaId()));
                            String descricao = JOptionPane.showInputDialog(this, "Descrição:", existente.getDescricao());
                            if (descricao == null) descricao = "";
                            double valor = Double.parseDouble(JOptionPane.showInputDialog(this, "Valor:", existente.getValor()));
                            LocalDate data = LocalDate.parse(JOptionPane.showInputDialog(this, "Data (AAAA-MM-DD):", existente.getData().toString()));

                            Transacao nova = new Transacao(id, contaId, categoriaId, descricao, valor, data);

                            TarefaComCarregamento.executar(
                                    (Frame) getOwner(),
                                    () -> service.atualizarTransacao(nova),
                                    () -> {
                                        JOptionPane.showMessageDialog(this, "Transação atualizada com sucesso.");
                                        carregarTabela();
                                    }
                            );

                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(this, e.getMessage());
                        }

                    }
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void excluir() {

        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog(this, "ID da Transação:"));

            int r = JOptionPane.showConfirmDialog(this,
                    "Excluir esta transação?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (r != JOptionPane.YES_OPTION) return;

            TarefaComCarregamento.executar(
                    (Frame) getOwner(),
                    () -> service.excluirTransacao(id),
                    () -> {
                        JOptionPane.showMessageDialog(this, "Transação excluída com sucesso.");
                        carregarTabela();
                    }
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
