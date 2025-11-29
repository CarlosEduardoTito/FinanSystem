package br.edu.utfpr.oo2.FinanSystem.gui;

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
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;

public class JanelaLogin extends JFrame {

	private JTextField txtNomeUsuario;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private JButton btnCriarConta;

    private UsuarioService usuarioService = new UsuarioService();
    private JLabel lblSlogan;

    public JanelaLogin() {
        setTitle("FinanSystem - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 410);
        setLocationRelativeTo(null);

        iniciarComponentes();
        adicionarEventos();
    }

    private void iniciarComponentes() {
        JLabel lblNomeUsuario = new JLabel("Nome de usuário:");
        lblNomeUsuario.setBounds(10, 88, 130, 20);
        lblNomeUsuario.setVerticalAlignment(SwingConstants.TOP);
        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setVerticalAlignment(SwingConstants.TOP);
        lblSenha.setBounds(10, 175, 91, 20);
        JLabel lblFinanSystem = new JLabel("FinanSystem");
        lblFinanSystem.setHorizontalAlignment(SwingConstants.CENTER);
        lblFinanSystem.setFont(new Font("Georgia", Font.PLAIN, 30));
        lblFinanSystem.setForeground(Color.BLACK);
        lblFinanSystem.setBounds(80, 11, 174, 38);
        getContentPane().add(lblFinanSystem);
        
        lblSlogan = new JLabel("O melhor pro seu planejamento");
        lblSlogan.setHorizontalAlignment(SwingConstants.CENTER);
        lblSlogan.setBounds(64, 47, 209, 14);
        getContentPane().add(lblSlogan);

        txtNomeUsuario = new JTextField(15);
        txtNomeUsuario.setBounds(10, 107, 314, 45);
        txtSenha = new JPasswordField(15);
        txtSenha.setBounds(10, 193, 314, 45);

        btnEntrar = new JButton("Entrar");
        btnEntrar.setBounds(188, 291, 136, 45);
        btnCriarConta = new JButton("Criar nova conta");
        btnCriarConta.setBounds(10, 291, 136, 45);
        getContentPane().setLayout(null);
        getContentPane().add(lblNomeUsuario);
        getContentPane().add(txtNomeUsuario);
        getContentPane().add(lblSenha);
        getContentPane().add(txtSenha);
        
        txtSenha.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                	txtSenha.setFocusable(false);
                	txtSenha.setFocusable(true);
                    fazerLogin();
                }
            }
        });
        
        getContentPane().add(btnEntrar);
        getContentPane().add(btnCriarConta);

    }

    private void adicionarEventos() {
        btnEntrar.addActionListener(e -> fazerLogin());
        btnCriarConta.addActionListener(e -> {
			try {
				abrirCadastro();
			} catch (ParseException e1) {
				JOptionPane.showMessageDialog(this,
						"Erro ao abrir janela de cadastro: " + e1.getMessage(),
						"Erro",
						JOptionPane.ERROR_MESSAGE);
			}
		});
    }

    private void fazerLogin() {
        String nomeUsuario = txtNomeUsuario.getText().trim();
        String senha = new String(txtSenha.getPassword());

        // Validação de campos vazios
        if (nomeUsuario.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "O campo 'Nome de usuário' é obrigatório!",
                    "Erro de login",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (senha.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "O campo 'Senha' é obrigatório!",
                    "Erro de login",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Login com tela de carregamento usando thread
        TarefaComCarregamento.executarComRetorno(
                this,
                () -> usuarioService.login(nomeUsuario, senha),
                usuario -> {
                    JOptionPane.showMessageDialog(this,
                            "Bem-vindo, " + usuario.getNomeCompleto() + "!");
                    // Aqui você pode abrir a próxima janela, por exemplo:
                    // abrirTelaPrincipal(usuario);
                },
                ex -> {
                    JOptionPane.showMessageDialog(this,
                            ex.getMessage(),
                            "Erro de login",
                            JOptionPane.ERROR_MESSAGE);
                }
        );
    }

    private void abrirCadastro() throws ParseException {
        JanelaCadastroUsuario cadastro = new JanelaCadastroUsuario(this, true);
        cadastro.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JanelaLogin().setVisible(true));
    }
}
