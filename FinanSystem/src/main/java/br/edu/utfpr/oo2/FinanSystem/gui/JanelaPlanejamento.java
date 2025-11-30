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
import javax.swing.table.DefaultTableModel;

import br.edu.utfpr.oo2.FinanSystem.entities.MetaFinanceira;
import br.edu.utfpr.oo2.FinanSystem.service.MetaFinanceiraService;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class JanelaPlanejamento extends JDialog {

	private JTextField txtDescricao;
    private JTextField txtValor;
    private JComboBox<String> cbTipo;
    private JTable tabelaMetas;
    private DefaultTableModel tableModel;
    
    private MetaFinanceiraService service = new MetaFinanceiraService();
    private Integer idUsuarioLogado;
	
	
	

	public JanelaPlanejamento(JFrame owner, Integer idUsuario) {
		
		super(owner, "Planejamento Financeiro", true);
		this.idUsuarioLogado = idUsuario;
		
		setSize(600, 450);
		setLocationRelativeTo(owner);
		setLayout(new BorderLayout(10, 10));
		
		iniciarComponentes();
		carregarTabela();
		
	}
	
	private void iniciarComponentes() {
		
		JPanel panelForm = new JPanel(new GridLayout(4, 2, 5, 5));
		panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		getContentPane().add(panelForm, BorderLayout.NORTH);
		
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
		btnSalvar.addActionListener(e -> salvarMeta());

		btnSalvar.setBackground(new Color(0, 128, 0));
		btnSalvar.setForeground(Color.WHITE);
		
		panelForm.add(new JLabel(""));
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
		JLabel lblTituloTabela = new JLabel("Suas metas Cadastradas");
		lblTituloTabela.setFont(new Font("Arial", Font.BOLD, 14));
		panelCentro.add(lblTituloTabela,BorderLayout.NORTH);
		panelCentro.add(scrollPane, BorderLayout.CENTER);
		
		add(panelCentro, BorderLayout.CENTER);
		
		JPanel panelSul = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton btnExcluir = new JButton("Excluir Selecionada");
		btnExcluir.setBackground(Color.RED);
		btnExcluir.addActionListener(e -> excluirMeta());
		
		JButton btnFechar = new JButton("Fechar");
		btnFechar.addActionListener(e -> dispose());
		
		panelSul.add(btnExcluir);
		panelSul.add(btnFechar);
		add(panelSul, BorderLayout.SOUTH);
		
	}
	
	private void carregarTabela() {
		
		tableModel.setRowCount(0);
		
		DecimalFormat df = new DecimalFormat("#,##0.00");
		
		try {
			List<MetaFinanceira> lista = service.listarPorUsuario(idUsuarioLogado);
			for(MetaFinanceira m : lista) {
				
				tableModel.addRow(new Object[] {
						m.getId(),
						m.getDescricao(),
						df.format(m.getValorMensal()),
						m.getTipoMeta()
				});
				
			}
		} catch(Exception e) {
			
			JOptionPane.showMessageDialog(this, "Erro ao carregar metas: " + e.getMessage());
			
		}
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
			
			service.cadastrar(meta);
			
			JOptionPane.showMessageDialog(this, "Meta salva com sucesso.");
			
			limparCampos();
			carregarTabela();
			
		} catch(NumberFormatException ex) {
			
			JOptionPane.showMessageDialog(this, "Valor inválido! Digite apenas números.", "Erro", JOptionPane.ERROR_MESSAGE);
			
		} catch (Exception ex) {
			
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
			
		}
		
	}
	
	private void excluirMeta() {
		
		int linhaSelecionada = tabelaMetas.getSelectedRow();
		if(linhaSelecionada == -1) {
			
			JOptionPane.showMessageDialog(this, "Selecione uma meta na tabela para excluir.");
			return;
			
		}
		
		Integer id = (Integer) tabelaMetas.getValueAt(linhaSelecionada, 0);
		
		int confirm = JOptionPane.showConfirmDialog(this, "tem certeza que deseja excluir esta meta?", "Confirmar exclusão.", JOptionPane.YES_NO_OPTION);
		
		if(confirm == JOptionPane.YES_OPTION) {
			
			try {
				
				service.excluir(id);
				carregarTabela();
				
			} catch(Exception ex) {
				
				JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage());
				
			}
			
		}
		
	}
	
	private void limparCampos() {
		
		txtDescricao.setText("");
		txtValor.setText("");
		
	}
	
    


}
