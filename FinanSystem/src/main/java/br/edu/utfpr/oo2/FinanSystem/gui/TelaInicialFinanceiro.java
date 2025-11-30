package br.edu.utfpr.oo2.FinanSystem.gui;

import br.edu.utfpr.oo2.FinanSystem.entities.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaInicialFinanceiro extends JFrame {


    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private final Usuario usuario;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TelaInicialFinanceiro frame = new TelaInicialFinanceiro(new Usuario());
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public TelaInicialFinanceiro(Usuario usuario) {
        this.usuario = usuario;
        iniciarComponentes();
    }

    public void abrirJanelaConta() {
        JanelaConta janelaConta = new JanelaConta(this, true);
        janelaConta.setVisible(true);
    }

    public void abrirJanelaCategoria() {
        JanelaCategoria janelaCategoria = new JanelaCategoria(this, true);
        janelaCategoria.setVisible(true);
    }

    public void abriJanelaTransacao() {
        JanelaTransacao janelaTransacao = new JanelaTransacao(this, true);
        janelaTransacao.setVisible(true);
    }

    public void iniciarComponentes() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);


        JLabel titulo = new JLabel("FinanSystem");
        titulo.setFont(new Font("Tahoma", Font.BOLD, 26));
        titulo.setBounds(130, 20, 300, 40);
        contentPane.add(titulo);

        JLabel subTitulo = new JLabel("Gerenciamento de Contas, Receitas e Despesas");
        subTitulo.setFont(new Font("Tahoma", Font.PLAIN, 14));
        subTitulo.setBounds(70, 60, 350, 20);
        contentPane.add(subTitulo);

        JButton ContaButton = new JButton("Conta");
        ContaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirJanelaConta();
            }
        });
        ContaButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        ContaButton.setBounds(44, 120, 85, 25);
        contentPane.add(ContaButton);

        JButton TransacaoButton = new JButton("Transacao");
        TransacaoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abriJanelaTransacao();
            }
        });
        TransacaoButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        TransacaoButton.setBounds(160, 120, 110, 25);
        contentPane.add(TransacaoButton);

        JButton CategoriaButton = new JButton("Categoria");
        CategoriaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirJanelaCategoria();
            }
        });
        CategoriaButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        CategoriaButton.setBounds(285, 120, 110, 25);
        contentPane.add(CategoriaButton);
        
        JButton btnRelatorios = new JButton("Relatórios");
        btnRelatorios.addActionListener(e -> {
            JanelaRelatorios janela = new JanelaRelatorios(this);
            janela.setVisible(true);
        });
        btnRelatorios.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnRelatorios.setBounds(44, 160, 110, 25);
        contentPane.add(btnRelatorios);
        
        JButton btnPlanejamento = new JButton("Planejamento");
        btnPlanejamento.addActionListener(e -> {

            JanelaPlanejamento janela = new JanelaPlanejamento(this, this.usuario.getId());
            janela.setVisible(true);
        });
        btnPlanejamento.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnPlanejamento.setBounds(160, 160, 130, 25);
        contentPane.add(btnPlanejamento);
    }
}
