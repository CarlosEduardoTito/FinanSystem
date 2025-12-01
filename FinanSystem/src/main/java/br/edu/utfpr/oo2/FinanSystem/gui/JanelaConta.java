package br.edu.utfpr.oo2.FinanSystem.gui;

import br.edu.utfpr.oo2.FinanSystem.entities.Conta;
import br.edu.utfpr.oo2.FinanSystem.service.ContaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class JanelaConta extends JDialog {

    private final ContaService contaService = new ContaService();
    private JTable tabela;
    private DefaultTableModel modelo;

    public JanelaConta(Frame owner, boolean modal) {
        super(owner, modal);
        init();
        carregarTabela();
    }

    private void init() {
        setTitle("FinanSystem - Contas");
        setSize(750, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(
                new Object[]{"ID", "Banco", "Agência", "Número", "Saldo", "Tipo"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tabela = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabela);

        JPanel botoes = new JPanel(new GridLayout(1, 5, 10, 0));

        JButton add = new JButton("Adicionar");
        JButton edit = new JButton("Editar");
        JButton del = new JButton("Excluir");
        JButton transf = new JButton("Transferir");
        JButton fechar = new JButton("Fechar");

        botoes.add(add);
        botoes.add(edit);
        botoes.add(del);
        botoes.add(transf);
        botoes.add(fechar);

        add(scroll, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);

        add.addActionListener(e -> adicionar());
        edit.addActionListener(e -> editar());
        del.addActionListener(e -> excluir());
        transf.addActionListener(e -> transferir());
        fechar.addActionListener(e -> dispose());
    }

    private void carregarTabela() {

        TarefaComCarregamento.executar(
                (Frame) getOwner(),
                () -> {
                    List<Conta> contas = contaService.listarContas();
                    SwingUtilities.invokeLater(() -> {
                        modelo.setRowCount(0);
                        for (Conta c : contas) {
                            modelo.addRow(new Object[]{
                                    c.getId(),
                                    c.getNomeBanco(),
                                    c.getAgencia(),
                                    c.getNumeroConta(),
                                    c.getSaldoInicial(),
                                    c.getTipoConta()
                            });
                        }
                    });
                },
                null
        );
    }

    private Conta coletarDados(Conta existente) {
        JTextField nomeBanco = new JTextField(existente != null ? existente.getNomeBanco() : "");
        JTextField agencia = new JTextField(existente != null ? existente.getAgencia() : "");
        JTextField numero = new JTextField(existente != null ? String.valueOf(existente.getNumeroConta()) : "");
        JTextField saldo = new JTextField(existente != null ? String.valueOf(existente.getSaldoInicial()) : "");

        String[] tipos = {"Corrente", "Poupança", "Salário", "Investimento"};
        JComboBox<String> tipo = new JComboBox<>(tipos);
        if (existente != null) tipo.setSelectedItem(existente.getTipoConta());

        JPanel painel = new JPanel(new GridLayout(5, 2, 5, 5));
        painel.add(new JLabel("Banco:"));
        painel.add(nomeBanco);
        painel.add(new JLabel("Agência:"));
        painel.add(agencia);
        painel.add(new JLabel("Número da Conta:"));
        painel.add(numero);
        painel.add(new JLabel("Saldo Inicial:"));
        painel.add(saldo);
        painel.add(new JLabel("Tipo:"));
        painel.add(tipo);

        int r = JOptionPane.showConfirmDialog(this, painel, "Dados da Conta", JOptionPane.OK_CANCEL_OPTION);
        if (r != JOptionPane.OK_OPTION) return null;

        Conta c = existente != null ? existente : new Conta();
        c.setNomeBanco(nomeBanco.getText().trim());
        c.setAgencia(agencia.getText().trim());
        c.setNumeroConta(Integer.parseInt(numero.getText().trim()));
        c.setSaldoInicial(Double.parseDouble(saldo.getText().trim()));
        c.setTipoConta(tipo.getSelectedItem().toString());

        c.setUserId(1);

        return c;
    }

    private Integer getIdSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) return null;
        return (Integer) modelo.getValueAt(linha, 0);
    }

    private void adicionar() {
        Conta nova = coletarDados(null);
        if (nova == null) return;

        TarefaComCarregamento.executar(
                (Frame) getOwner(),
                () -> contaService.cadastrarConta(nova),
                () -> {
                    JOptionPane.showMessageDialog(this, "Conta cadastrada com sucesso!");
                    carregarTabela();
                }
        );
    }

    private void editar() {
        Integer id = getIdSelecionado();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma conta para editar.");
            return;
        }

        TarefaComCarregamento.executarComRetorno(
                (Frame) getOwner(),
                () -> contaService.buscarPorId(id),
                conta -> {
                    Conta atualizada = coletarDados(conta);
                    if (atualizada == null) return;

                    TarefaComCarregamento.executar(
                            (Frame) getOwner(),
                            () -> contaService.atualizarConta(atualizada),
                            () -> {
                                JOptionPane.showMessageDialog(this, "Conta atualizada com sucesso!");
                                carregarTabela();
                            }
                    );
                }
        );
    }

    private void excluir() {
        Integer id = getIdSelecionado();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma conta para excluir.");
            return;
        }

        int r = JOptionPane.showConfirmDialog(this,
                "Excluir esta conta?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) return;

        TarefaComCarregamento.executar(
                (Frame) getOwner(),
                () -> contaService.excluirConta(id),
                () -> {
                    JOptionPane.showMessageDialog(this, "Conta excluída com sucesso!");
                    carregarTabela();
                }
        );
    }


    private void transferir() {

        JTextField origem = new JTextField();
        JTextField destino = new JTextField();
        JTextField valor = new JTextField();

        JPanel painel = new JPanel(new GridLayout(3, 2, 5, 5));
        painel.add(new JLabel("ID Conta Origem:"));
        painel.add(origem);
        painel.add(new JLabel("ID Conta Destino:"));
        painel.add(destino);
        painel.add(new JLabel("Valor:"));
        painel.add(valor);

        int r = JOptionPane.showConfirmDialog(this, painel, "Transferência", JOptionPane.OK_CANCEL_OPTION);
        if (r != JOptionPane.OK_OPTION) return;

        int idOrigem = Integer.parseInt(origem.getText().trim());
        int idDestino = Integer.parseInt(destino.getText().trim());
        double v = Double.parseDouble(valor.getText().trim());

        TarefaComCarregamento.executar(
                (Frame) getOwner(),
                () -> contaService.transferir(idOrigem, idDestino, v),
                () -> {
                    JOptionPane.showMessageDialog(this, "Transferência realizada com sucesso!");
                    carregarTabela();
                }
        );
    }
}
