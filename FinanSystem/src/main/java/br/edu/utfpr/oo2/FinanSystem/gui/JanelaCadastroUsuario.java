package br.edu.utfpr.oo2.FinanSystem.gui;

import java.awt.Frame;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import br.edu.utfpr.oo2.FinanSystem.entities.Usuario;
import br.edu.utfpr.oo2.FinanSystem.service.UsuarioService;

public class JanelaCadastroUsuario extends JDialog {

	private JTextField txtNomeCompleto;
    private JFormattedTextField txtDataNascimento;
    private JComboBox<String> cbSexo;
    private JTextField txtNomeUsuario;
    private JPasswordField txtSenha;

    private JButton btnSalvar;
    private JButton btnCancelar;

    private UsuarioService usuarioService = new UsuarioService();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public JanelaCadastroUsuario(Frame owner, boolean modal) {
        super(owner, modal);
        setTitle("Cadastro de Usuário");
        setSize(400, 250);
        setLocationRelativeTo(owner);

        iniciarComponentes();
        adicionarEventos();
    }

    private void iniciarComponentes() {
        txtNomeCompleto = new JTextField(25);
        txtDataNascimento = new JFormattedTextField("##/##/####");
        cbSexo = new JComboBox<>(new String[]{"M", "F", "Outro"});
        txtNomeUsuario = new JTextField(15);
        txtSenha = new JPasswordField(15);

        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");

        setLayout(new GridLayout(6, 2, 5, 5));

        add(new JLabel("Nome completo:"));
        add(txtNomeCompleto);

        add(new JLabel("Data nascimento (dd/MM/yyyy):"));
        add(txtDataNascimento);

        add(new JLabel("Sexo:"));
        add(cbSexo);

        add(new JLabel("Nome de usuário:"));
        add(txtNomeUsuario);

        add(new JLabel("Senha:"));
        add(txtSenha);

        add(btnSalvar);
        add(btnCancelar);
    }

    private void adicionarEventos() {
        btnSalvar.addActionListener(e -> salvarUsuario());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void salvarUsuario() {
        try {
            String nomeCompleto = txtNomeCompleto.getText().trim();
            LocalDate dataNasc = LocalDate.parse(
                    txtDataNascimento.getText().trim(),
                    formatter
            );
            String sexo = (String) cbSexo.getSelectedItem();
            String nomeUsuario = txtNomeUsuario.getText().trim();
            String senha = new String(txtSenha.getPassword());

            Usuario usuario = new Usuario(
                    nomeCompleto,
                    dataNasc,
                    sexo,
                    nomeUsuario,
                    senha
            );

            usuarioService.cadastrar(usuario);

            JOptionPane.showMessageDialog(this,
                    "Usuário cadastrado com sucesso!");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Erro ao cadastrar usuário",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
