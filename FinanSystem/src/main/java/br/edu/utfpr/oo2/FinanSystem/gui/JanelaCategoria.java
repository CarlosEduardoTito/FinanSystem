package br.edu.utfpr.oo2.FinanSystem.gui;

import br.edu.utfpr.oo2.FinanSystem.entities.Categoria;
import br.edu.utfpr.oo2.FinanSystem.service.CategoriaService;

import javax.swing.*;
import java.awt.*;

public class JanelaCategoria extends JDialog {

    private final CategoriaService categoriaService = new CategoriaService();

    public JanelaCategoria(Frame owner, boolean modal) {
        super(owner, modal);
        init();
    }

    private void init() {
        setTitle("FinanSystem - Categorias");
        setSize(400, 250);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new GridLayout(4, 1, 10, 10));

        JButton btnAdd = new JButton("Adicionar Categoria");
        JButton btnEdit = new JButton("Editar Categoria");
        JButton btnDelete = new JButton("Excluir Categoria");
        JButton btnFechar = new JButton("Fechar");

        painel.add(btnAdd);
        painel.add(btnEdit);
        painel.add(btnDelete);

        add(painel, BorderLayout.CENTER);
        add(btnFechar, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> adicionar());
        btnEdit.addActionListener(e -> editar());
        btnDelete.addActionListener(e -> excluir());
        btnFechar.addActionListener(e -> dispose());
    }

    private String escolherTipo(String atual) {
        String[] tipos = {"Entrada", "Saída", "Investimento"};
        return (String) JOptionPane.showInputDialog(
                this,
                "Selecione o tipo da categoria:",
                "Tipo",
                JOptionPane.QUESTION_MESSAGE,
                null,
                tipos,
                atual != null ? atual : tipos[0]
        );
    }

    private void adicionar() {
        try {
            String nome = JOptionPane.showInputDialog(this, "Nome da Categoria:");
            if (nome == null || nome.trim().isEmpty()) return;

            String tipo = escolherTipo(null);
            if (tipo == null) return;

            Categoria c = new Categoria();
            c.setNome(nome.trim());
            c.setTipo(tipo);

            categoriaService.cadastrarCategoria(c);

            JOptionPane.showMessageDialog(this, "Categoria cadastrada com sucesso.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void editar() {
        try {
            String idStr = JOptionPane.showInputDialog(this, "ID da categoria:");
            if (idStr == null || idStr.trim().isEmpty()) return;

            int id = Integer.parseInt(idStr);
            Categoria c = categoriaService.buscarPorId(id);

            if (c == null) {
                JOptionPane.showMessageDialog(this, "Categoria não encontrada.");
                return;
            }

            String nome = JOptionPane.showInputDialog(this, "Nome da Categoria:", c.getNome());
            if (nome == null || nome.trim().isEmpty()) return;

            String tipo = escolherTipo(c.getTipo());
            if (tipo == null) return;

            c.setNome(nome.trim());
            c.setTipo(tipo);

            categoriaService.atualizarCategoria(c);

            JOptionPane.showMessageDialog(this, "Categoria atualizada com sucesso.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void excluir() {
        try {
            String idStr = JOptionPane.showInputDialog(this, "ID da categoria:");
            if (idStr == null || idStr.trim().isEmpty()) return;

            int id = Integer.parseInt(idStr);

            categoriaService.excluirCategoria(id);

            JOptionPane.showMessageDialog(this, "Categoria excluída com sucesso.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}
