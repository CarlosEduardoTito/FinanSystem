package br.edu.utfpr.oo2.FinanSystem.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import br.edu.utfpr.oo2.FinanSystem.entities.MetaFinanceira;
import br.edu.utfpr.oo2.FinanSystem.service.MetaFinanceiraService;

public class JanelaPlanejamento extends JDialog {

    private JTextField txtDescricao;
    private JTextField txtValor;
    private JComboBox<String> cbTipo;
    private JTable tabelaMetas;
    private DefaultTableModel tableModel;
    
    private Integer idMetaEmEdicao = null;
    
    private final MetaFinanceiraService service = new MetaFinanceiraService();
    private final Integer idUsuarioLogado;

    public JanelaPlanejamento(JFrame owner, Integer idUsuario) {
        super(owner, "Planejamento Financeiro", true);
        
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
        
        this.idUsuarioLogado = idUsuario;
        
        setSize(650, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        
        iniciarComponentes();
        carregarTabela();
    }
    
    private void iniciarComponentes() {
    	
        JPanel panelForm = new JPanel(new GridLayout(4, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(panelForm, BorderLayout.NORTH);
        
        txtDescricao = new JTextField();
        txtValor = new JTextField();
        cbTipo = new JComboBox<>(new String[] {"Longo Prazo", "Despesa Ocasional"});
        
        panelForm.add(new JLabel("Descrição da Meta:"));
        panelForm.add(txtDescricao);
        
        panelForm.add(new JLabel("Valor Mensal (R$):"));
        panelForm.add(txtValor);
        
        panelForm.add(new JLabel("Tipo de Meta:"));
        panelForm.add(cbTipo);
        
        JButton btnSalvar = new JButton("Salvar Meta");
        btnSalvar.setBackground(new Color(0, 128, 0));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.addActionListener(e -> salvarMeta());
        
        JButton btnLimpar = new JButton("Cancelar Edição / Limpar");
        btnLimpar.addActionListener(e -> limparCampos());
        
        panelForm.add(btnLimpar);
        panelForm.add(btnSalvar);
        
        String[] colunas = {"ID", "Descrição", "Valor (R$)", "Tipo"};
        tableModel = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaMetas = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaMetas);
        
        JPanel panelCentro = new JPanel(new BorderLayout());
        JLabel lblTituloTabela = new JLabel("  Suas Metas Cadastradas");
        lblTituloTabela.setFont(new Font("Arial", Font.BOLD, 14));
        panelCentro.add(lblTituloTabela, BorderLayout.NORTH);
        panelCentro.add(scrollPane, BorderLayout.CENTER);
        
        add(panelCentro, BorderLayout.CENTER);
        
        JPanel panelSul = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnEditar = new JButton("Editar Selecionada");
        btnEditar.setBackground(new Color(255, 165, 0));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.addActionListener(e -> carregarParaEdicao());
        
        JButton btnExcluir = new JButton("Excluir Selecionada");
        btnExcluir.setBackground(Color.RED);
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.addActionListener(e -> excluirMeta());
        
        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        
        panelSul.add(btnEditar);
        panelSul.add(btnExcluir);
        panelSul.add(btnFechar);
        add(panelSul, BorderLayout.SOUTH);
    }
    
    private void carregarTabela() {
    	
        TarefaComCarregamento.executar(
            (JFrame) getOwner(),
            () -> {
                List<MetaFinanceira> lista = service.listarPorUsuario(idUsuarioLogado);
                SwingUtilities.invokeLater(() -> {
                    tableModel.setRowCount(0);
                    DecimalFormat df = new DecimalFormat("#,##0.00");
                    for(MetaFinanceira m : lista) {
                        tableModel.addRow(new Object[] {
                            m.getId(),
                            m.getDescricao(),
                            df.format(m.getValorMensal()),
                            m.getTipoMeta()
                        });
                    }
                });
            },
            null
        );
    }
    
    private void salvarMeta() {
        try {
            String desc = txtDescricao.getText();
            String valorStr = txtValor.getText().replace(",", ".");
            double valor = Double.parseDouble(valorStr);
            String tipo = (String) cbTipo.getSelectedItem();
            
            MetaFinanceira meta = new MetaFinanceira();
            meta.setUsuarioId(idUsuarioLogado);
            meta.setDescricao(desc);
            meta.setValorMensal(valor);
            meta.setTipoMeta(tipo);
            
            if (idMetaEmEdicao != null) {
                meta.setId(idMetaEmEdicao);
                
                TarefaComCarregamento.executar(
                    (JFrame) getOwner(),
                    () -> service.atualizar(meta),
                    () -> {
                        JOptionPane.showMessageDialog(this, "Meta atualizada com sucesso!");
                        limparCampos();
                        carregarTabela();
                    }
                );
            } else {
                TarefaComCarregamento.executar(
                    (JFrame) getOwner(),
                    () -> service.cadastrar(meta),
                    () -> {
                        JOptionPane.showMessageDialog(this, "Meta salva com sucesso!");
                        limparCampos();
                        carregarTabela();
                    }
                );
            }
            
        } catch(NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor inválido! Digite apenas números.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void carregarParaEdicao() {
        int linha = tabelaMetas.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma meta para editar.");
            return;
        }
        
        idMetaEmEdicao = (Integer) tabelaMetas.getValueAt(linha, 0);
        txtDescricao.setText((String) tabelaMetas.getValueAt(linha, 1));
        
        String valorStr = (String) tabelaMetas.getValueAt(linha, 2);
        valorStr = valorStr.replace(".", "").replace(",", ".");
        txtValor.setText(valorStr);
        
        cbTipo.setSelectedItem((String) tabelaMetas.getValueAt(linha, 3));
    }
    
    private void excluirMeta() {
        int linhaSelecionada = tabelaMetas.getSelectedRow();
        if(linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma meta para excluir.");
            return;
        }
        
        Integer id = (Integer) tabelaMetas.getValueAt(linhaSelecionada, 0);
        
        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir?", "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if(confirm == JOptionPane.YES_OPTION) {
            TarefaComCarregamento.executar(
                (JFrame) getOwner(),
                () -> service.excluir(id),
                () -> {
                    JOptionPane.showMessageDialog(this, "Meta excluída.");
                    carregarTabela();
                    limparCampos();
                }
            );
        }
    }
    
    private void limparCampos() {
        txtDescricao.setText("");
        txtValor.setText("");
        cbTipo.setSelectedIndex(0);
        idMetaEmEdicao = null;
    }
    
}