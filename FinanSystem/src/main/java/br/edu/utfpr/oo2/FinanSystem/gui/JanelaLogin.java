package br.edu.utfpr.oo2.FinanSystem.gui;

import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import br.edu.utfpr.oo2.FinanSystem.entities.Usuario;
import br.edu.utfpr.oo2.FinanSystem.service.UsuarioService;

public class JanelaLogin extends JFrame {

	private JTextField txtNomeUsuario;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private JButton btnCriarConta;

    private UsuarioService usuarioService = new UsuarioService();

    public JanelaLogin() {
        setTitle("FinanSystem - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 200);
        setLocationRelativeTo(null);

        iniciarComponentes();
        adicionarEventos();
    }

    private void iniciarComponentes() {
        JLabel lblNomeUsuario = new JLabel("Nome de usuário:");
        JLabel lblSenha = new JLabel("Senha:");

        txtNomeUsuario = new JTextField(15);
        txtSenha = new JPasswordField(15);

        btnEntrar = new JButton("Entrar");
        btnCriarConta = new JButton("Criar nova conta");

        setLayout(new GridLayout(3, 2, 5, 5));
        add(lblNomeUsuario);
        add(txtNomeUsuario);
        add(lblSenha);
        add(txtSenha);
        
        txtSenha.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    fazerLogin();
                }
            }
        });
        
        add(btnEntrar);
        add(btnCriarConta);
    }

    private void adicionarEventos() {
        btnEntrar.addActionListener(e -> fazerLogin());
        btnCriarConta.addActionListener(e -> {
			try {
				abrirCadastro();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
    }

    private void fazerLogin() {
        String nomeUsuario = txtNomeUsuario.getText().trim();
        String senha = new String(txtSenha.getPassword());

        try {
            Usuario usuario = usuarioService.login(nomeUsuario, senha);
            JOptionPane.showMessageDialog(this,
                    "Bem-vindo, " + usuario.getNomeCompleto() + "!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Erro de login",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirCadastro() throws ParseException {
        JanelaCadastroUsuario cadastro = new JanelaCadastroUsuario(this, true);
        cadastro.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JanelaLogin().setVisible(true));
    }
}
