package br.edu.utfpr.oo2.FinanSystem.gui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class JanelaCarregamento extends JDialog {
    private int x = 0;
    private int direction = 1;
    private Timer timer;

    public JanelaCarregamento(Frame owner) {
        super(owner, true);
        setTitle("Carregando...");
        setSize(350, 150);
        setLocationRelativeTo(owner);
        setLayout(null);
        getContentPane().setBackground(new Color(240, 240, 240));
        setResizable(false);
        setUndecorated(true);

        getRootPane().setBorder(new LineBorder(new Color(50, 50, 50), 3));

        JLabel texto = new JLabel("Carregando...", SwingConstants.CENTER);
        texto.setFont(new Font("Tahoma", Font.PLAIN, 16));
        texto.setBounds(0, 10, 350, 30);
        add(texto);

        JPanel painelAnimacao = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int y = (getHeight() - 20) / 2;
                g2.setColor(Color.GRAY);
                g2.setStroke(new BasicStroke(3f));
                g2.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);

                g2.setColor(Color.DARK_GRAY);
                g2.fillOval(x, y, 20, 20);
            }
        };
        painelAnimacao.setBounds(20, 50, 300, 40);
        painelAnimacao.setOpaque(false);
        add(painelAnimacao);

        timer = new Timer(10, e -> {
            x += direction;
            if (x <= 0) direction = 1;
            if (x >= painelAnimacao.getWidth() - 20) direction = -1;
            painelAnimacao.repaint();
        });
        timer.start();
    }

    public void fechar() {
        if (timer != null) timer.stop();
        dispose();
    }
}