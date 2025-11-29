package br.edu.utfpr.oo2.FinanSystem.gui;

import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import br.edu.utfpr.oo2.FinanSystem.entities.Usuario;
import br.edu.utfpr.oo2.FinanSystem.service.UsuarioService;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;

public class JanelaCadastroUsuario extends JDialog {

	private JTextField txtNomeCompleto;
    private JFormattedTextField txtDataNascimento;
    private JComboBox<String> cbSexo;
    private JTextField txtNomeUsuario;
    private JPasswordField txtSenha;
    private JButton btnConfirmar;
    private JButton btnCancelar;

    private UsuarioService usuarioService = new UsuarioService();
    private JLabel lblFinanSystem;
    private JLabel lblSlogan;
    private JLabel lblNewLabel;

    public JanelaCadastroUsuario(Frame owner, boolean modal) throws ParseException {
        super(owner, modal);
        setTitle("FinanSystem - Novo Usuário");
        setSize(350, 410);
        setLocationRelativeTo(owner);

        iniciarComponentes();
        adicionarEventos();
    }

    private void iniciarComponentes() throws ParseException {
        txtNomeCompleto = new JTextField(25);
        txtNomeCompleto.setBounds(136, 111, 188, 20);
        cbSexo = new JComboBox<>(new String[]{"M", "F", "Outro"});
        cbSexo.setBounds(222, 170, 70, 18);
        txtNomeUsuario = new JTextField(15);
        txtNomeUsuario.setBounds(136, 208, 188, 20);
        txtSenha = new JPasswordField(15);
        txtSenha.setBounds(136, 236, 188, 20);
        lblFinanSystem = new JLabel("FinanSystem");
        lblFinanSystem.setHorizontalAlignment(SwingConstants.CENTER);
        lblFinanSystem.setForeground(Color.BLACK);
        lblFinanSystem.setFont(new Font("Georgia", Font.PLAIN, 30));
        lblFinanSystem.setBounds(79, 11, 174, 38);
        getContentPane().add(lblFinanSystem);
        
        lblSlogan = new JLabel("O melhor pro seu planejamento");
        lblSlogan.setHorizontalAlignment(SwingConstants.CENTER);
        lblSlogan.setBounds(58, 44, 209, 14);
        getContentPane().add(lblSlogan);
        
        lblNewLabel = new JLabel("Cadastro de novo usuário");
        lblNewLabel.setForeground(new Color(0, 0, 0));
        lblNewLabel.setFont(new Font("Georgia", Font.BOLD, 14));
        lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
        lblNewLabel.setBounds(10, 70, 188, 35);
        getContentPane().add(lblNewLabel);
        
        btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setBounds(188, 291, 136, 45);
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(10, 291, 135, 45);

        MaskFormatter mask = new MaskFormatter("##/##/####");
        mask.setPlaceholderCharacter('_');
        mask.setValueContainsLiteralCharacters(false);
        mask.setAllowsInvalid(false);
        mask.setOverwriteMode(true);

        txtDataNascimento = new JFormattedTextField(mask);
        txtDataNascimento.setHorizontalAlignment(SwingConstants.CENTER);
        txtDataNascimento.setBounds(222, 139, 70, 20);
        getContentPane().setLayout(null);

        JLabel label = new JLabel("Nome completo:");
        label.setForeground(new Color(0, 0, 0));
        label.setFont(new Font("Arial", Font.BOLD, 11));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setVerticalAlignment(SwingConstants.TOP);
        label.setBounds(10, 115, 126, 17);
        getContentPane().add(label);
        getContentPane().add(txtNomeCompleto);

        JLabel label_1 = new JLabel("Data nascimento (dd/MM/yyyy):");
        label_1.setForeground(new Color(0, 0, 0));
        label_1.setFont(new Font("Arial", Font.BOLD, 11));
        label_1.setHorizontalAlignment(SwingConstants.LEFT);
        label_1.setVerticalAlignment(SwingConstants.TOP);
        label_1.setBounds(10, 143, 188, 17);
        getContentPane().add(label_1);
        getContentPane().add(txtDataNascimento);

        JLabel lblInformeOSeu = new JLabel("Informe o seu sexo:");
        lblInformeOSeu.setForeground(new Color(0, 0, 0));
        lblInformeOSeu.setHorizontalAlignment(SwingConstants.LEFT);
        lblInformeOSeu.setFont(new Font("Arial", Font.BOLD, 11));
        lblInformeOSeu.setVerticalAlignment(SwingConstants.TOP);
        lblInformeOSeu.setBounds(10, 173, 188, 17);
        getContentPane().add(lblInformeOSeu);
        getContentPane().add(cbSexo);

        JLabel label_3 = new JLabel("Nome de usuário:");
        label_3.setFont(new Font("Arial", Font.BOLD, 11));
        label_3.setBackground(new Color(0, 0, 0));
        label_3.setHorizontalAlignment(SwingConstants.LEFT);
        label_3.setForeground(new Color(0, 0, 0));
        label_3.setVerticalAlignment(SwingConstants.TOP);
        label_3.setBounds(10, 212, 126, 17);
        getContentPane().add(label_3);
        getContentPane().add(txtNomeUsuario);

        JLabel lblInformeUmaSenha = new JLabel("Informe uma senha:");
        lblInformeUmaSenha.setForeground(new Color(0, 0, 0));
        lblInformeUmaSenha.setFont(new Font("Arial", Font.BOLD, 11));
        lblInformeUmaSenha.setVerticalAlignment(SwingConstants.TOP);
        lblInformeUmaSenha.setBounds(10, 240, 126, 17);
        getContentPane().add(lblInformeUmaSenha);
        getContentPane().add(txtSenha);
        
        txtSenha.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    confirmarUsuario();
                }
            }
        });
        
        getContentPane().add(btnConfirmar);
        getContentPane().add(btnCancelar);
        
    }
    
    private void adicionarEventos() {
        btnConfirmar.addActionListener(e -> confirmarUsuario());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void confirmarUsuario() {
        try {
            // Validação de campos vazios
            String nomeCompleto = txtNomeCompleto.getText().trim();
            if (nomeCompleto.isEmpty()) {
                throw new Exception("O campo 'Nome completo' é obrigatório!");
            }

            String dataNascStr = txtDataNascimento.getText().trim();
            if (dataNascStr.isEmpty() || dataNascStr.contains("_")) {
                throw new Exception("O campo 'Data de nascimento' é obrigatório!");
            }

            LocalDate dataNasc;
            try {
                dataNasc = LocalDate.parse(
                        dataNascStr,
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                );
            } catch (DateTimeParseException e) {
                throw new Exception("O campo 'Data de nascimento' é obrigatório!");
            }

            String sexo = (String) cbSexo.getSelectedItem();
            String nomeUsuario = txtNomeUsuario.getText().trim();
            if (nomeUsuario.isEmpty()) {
                throw new Exception("O campo 'Nome de usuário' é obrigatório!");
            }

            String senha = new String(txtSenha.getPassword());
            if (senha.isEmpty()) {
                throw new Exception("O campo 'Senha' é obrigatório!");
            }

            Usuario usuario = new Usuario(
                    nomeCompleto,
                    dataNasc,
                    sexo,
                    nomeUsuario,
                    senha
            );

            // Cadastro com tela de carregamento usando thread
            TarefaComCarregamento.executar(
                    (Frame) getOwner(),
                    () -> usuarioService.cadastrar(usuario),
                    () -> {
                        JOptionPane.showMessageDialog(this,
                                "Usuário cadastrado com sucesso!");
                        dispose();
                    },
                    ex -> {
                        JOptionPane.showMessageDialog(this,
                                ex.getMessage(),
                                "Erro ao cadastrar usuário",
                                JOptionPane.ERROR_MESSAGE);
                    }
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Erro ao cadastrar usuário",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
