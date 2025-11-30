package br.edu.utfpr.oo2.FinanSystem.gui;

import br.edu.utfpr.oo2.FinanSystem.entities.Transacao;
import br.edu.utfpr.oo2.FinanSystem.service.RelatorioService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class JanelaRelatorios extends JDialog {

    private JComboBox<String> cbMes;
    private JTextField txtAno;
    private JTable tabela;
    private DefaultTableModel modelo;
    
    private JRadioButton rbMensal;
    private JRadioButton rbAnual;
    private ButtonGroup grupoTipo;

    private final RelatorioService service = new RelatorioService();
    
    private List<Transacao> transacoesAtuais;
    private Map<String, Double> dadosAnuaisAtuais;

    public JanelaRelatorios(Frame owner) {
        super(owner, "Relatórios e Exportação", true);
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            
            javax.swing.SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        setSize(800, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        iniciarTopo();
        iniciarTabela();
        iniciarRodape();
    }

    private void iniciarTopo() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        rbMensal = new JRadioButton("Mensal (Detalhado)", true);
        rbAnual = new JRadioButton("Anual (Agrupado)");
        grupoTipo = new ButtonGroup();
        grupoTipo.add(rbMensal);
        grupoTipo.add(rbAnual);

        cbMes = new JComboBox<>(new String[]{
                "01 - Janeiro", "02 - Fevereiro", "03 - Março", "04 - Abril",
                "05 - Maio", "06 - Junho", "07 - Julho", "08 - Agosto",
                "09 - Setembro", "10 - Outubro", "11 - Novembro", "12 - Dezembro"
        });
        cbMes.setSelectedIndex(LocalDate.now().getMonthValue() - 1);

        txtAno = new JTextField(String.valueOf(LocalDate.now().getYear()), 5);

        JButton btnFiltrar = new JButton("Gerar Relatório em Tela");
        btnFiltrar.addActionListener(e -> carregarDados());

        rbMensal.addActionListener(e -> cbMes.setEnabled(true));
        rbAnual.addActionListener(e -> cbMes.setEnabled(false));

        panel.add(rbMensal);
        panel.add(rbAnual);
        panel.add(new JLabel("| Mês:"));
        panel.add(cbMes);
        panel.add(new JLabel("Ano:"));
        panel.add(txtAno);
        panel.add(btnFiltrar);

        add(panel, BorderLayout.NORTH);
    }

    private void iniciarTabela() {

        String[] colunas = {"ID", "Data", "Descrição", "Valor (R$)"};
        modelo = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modelo);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    private void iniciarRodape() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnPdf = new JButton("Exportar PDF");
        btnPdf.setBackground(new Color(200, 50, 50));
        btnPdf.setForeground(Color.WHITE);
        btnPdf.addActionListener(e -> acaoExportar("PDF"));

        JButton btnExcel = new JButton("Exportar Excel");
        btnExcel.setBackground(new Color(34, 139, 34));
        btnExcel.setForeground(Color.WHITE);
        btnExcel.addActionListener(e -> acaoExportar("EXCEL"));

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());

        panel.add(btnPdf);
        panel.add(btnExcel);
        panel.add(btnFechar);

        add(panel, BorderLayout.SOUTH);
    }

    private void carregarDados() {
        int ano;
        try {
            ano = Integer.parseInt(txtAno.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ano inválido.");
            return;
        }


        if (rbMensal.isSelected()) {
            int mes = cbMes.getSelectedIndex() + 1;

            TarefaComCarregamento.executarComRetorno(
                    (Frame) getOwner(),
                    () -> service.buscarTransacoesMensais(mes, ano),
                    lista -> {

                        this.dadosAnuaisAtuais = null;
                        this.transacoesAtuais = lista;

                        modelo.setColumnCount(0);
                        modelo.addColumn("ID");
                        modelo.addColumn("Data");
                        modelo.addColumn("Descrição");
                        modelo.addColumn("Valor (R$)");

                        modelo.setRowCount(0);
                        for (Transacao t : lista) {
                            modelo.addRow(new Object[]{
                                    t.getId(),
                                    t.getData(),
                                    t.getDescricao(),
                                    t.getValor()
                            });
                        }
                        if (lista.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Nenhuma transação encontrada neste período.");
                        }
                    }
            );
        } 

        else {
            TarefaComCarregamento.executarComRetorno(
                    (Frame) getOwner(),
                    () -> service.gerarRelatorioAnualAgrupado(ano),
                    mapaResumo -> {

                        this.transacoesAtuais = null;
                        this.dadosAnuaisAtuais = mapaResumo;

                        modelo.setColumnCount(0);
                        modelo.addColumn("Categoria");
                        modelo.addColumn("Total (R$)");

                        modelo.setRowCount(0);
                        if (mapaResumo.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Sem dados para o ano.");
                        } else {
                            for (Map.Entry<String, Double> entry : mapaResumo.entrySet()) {
                                modelo.addRow(new Object[]{
                                        entry.getKey(),
                                        String.format("%.2f", entry.getValue())
                                });
                            }
                        }
                    }
            );
        }
    }

    private void acaoExportar(String formato) {

        boolean temDadosMensais = (rbMensal.isSelected() && transacoesAtuais != null && !transacoesAtuais.isEmpty());
        boolean temDadosAnuais = (rbAnual.isSelected() && dadosAnuaisAtuais != null && !dadosAnuaisAtuais.isEmpty());

        if (!temDadosMensais && !temDadosAnuais) {
            JOptionPane.showMessageDialog(this, "Gere o relatório na tela antes de exportar.");
            return;
        }

        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Salvar Relatório " + formato + " (" + (rbMensal.isSelected() ? "Mensal" : "Anual") + ")");

        String ext = formato.equals("PDF") ? ".pdf" : ".xlsx";
        String nomePadrao = "Relatorio_" + (rbMensal.isSelected() ? "Mensal" : "Anual") + "_" + LocalDate.now() + ext;
        jfc.setSelectedFile(new File(nomePadrao));

        int r = jfc.showSaveDialog(this);
        if (r == JFileChooser.APPROVE_OPTION) {
            File arquivo = jfc.getSelectedFile();
            int ano = Integer.parseInt(txtAno.getText());

            TarefaComCarregamento.executar(
                    (Frame) getOwner(),
                    () -> {

                        if (formato.equals("PDF")) {
                            if (rbMensal.isSelected()) {
                                service.exportarPdf(transacoesAtuais, arquivo.getAbsolutePath());
                            } else {
                                service.exportarPdfAnual(dadosAnuaisAtuais, ano, arquivo.getAbsolutePath());
                            }
                        } 

                        else {
                            if (rbMensal.isSelected()) {
                                service.exportarExcel(transacoesAtuais, arquivo.getAbsolutePath());
                            } else {

                                service.exportarExcelAnual(dadosAnuaisAtuais, ano, arquivo.getAbsolutePath());

                            }
                        }
                    },
                    () -> JOptionPane.showMessageDialog(this, "Arquivo exportado com sucesso!")
            );
        }
    }
}