package br.edu.utfpr.oo2.FinanSystem.gui;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class TarefaComCarregamento {

    public static void executar(Frame owner, RunnableComExcecao tarefa, Runnable aoSucesso) {
        executar(owner, tarefa, aoSucesso, null);
    }

    public static void executar(Frame owner, RunnableComExcecao tarefa, Runnable aoSucesso, Consumer<Exception> aoErro) {
        JanelaCarregamento dialog = new JanelaCarregamento(owner);

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private Exception erro = null;

            @Override
            protected Void doInBackground() {
                try {
                    tarefa.executar();
                } catch (Exception e) {
                    erro = e;
                }
                return null;
            }

            @Override
            protected void done() {
                dialog.fechar();

                if (erro != null) {
                    if (aoErro != null) {
                        aoErro.accept(erro);
                    } else {
                        JOptionPane.showMessageDialog(owner,
                                "Erro: " + erro.getMessage(),
                                "Erro",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    if (aoSucesso != null) {
                        aoSucesso.run();
                    }
                }
            }
        };

        worker.execute();
        dialog.setVisible(true);
    }

    public static <T> void executarComRetorno(Frame owner, Callable<T> tarefa, Consumer<T> aoSucesso) {
        executarComRetorno(owner, tarefa, aoSucesso, null);
    }

    public static <T> void executarComRetorno(Frame owner, Callable<T> tarefa, Consumer<T> aoSucesso, Consumer<Exception> aoErro) {
        JanelaCarregamento dialog = new JanelaCarregamento(owner);

        SwingWorker<T, Void> worker = new SwingWorker<T, Void>() {
            private Exception erro = null;

            @Override
            protected T doInBackground() {
                try {
                    return tarefa.call();
                } catch (Exception e) {
                    erro = e;
                    return null;
                }
            }

            @Override
            protected void done() {
                dialog.fechar();

                if (erro != null) {
                    if (aoErro != null) {
                        aoErro.accept(erro);
                    } else {
                        JOptionPane.showMessageDialog(owner,
                                "Erro: " + erro.getMessage(),
                                "Erro",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    try {
                        T resultado = get();
                        if (aoSucesso != null && resultado != null) {
                            aoSucesso.accept(resultado);
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(owner,
                                "Erro ao processar resultado: " + e.getMessage(),
                                "Erro",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };

        worker.execute();
        dialog.setVisible(true);
    }

    @FunctionalInterface
    public interface RunnableComExcecao {
        void executar() throws Exception;
    }
}
