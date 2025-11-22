package br.edu.utfpr.oo2.FinanSystem.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Arrays;

import br.edu.utfpr.oo2.FinanSystem.dao.BancoDados;
import br.edu.utfpr.oo2.FinanSystem.dao.CategoriaDAO;
import br.edu.utfpr.oo2.FinanSystem.entities.Categoria;

public class CategoriaService {
    private static final List<String> TIPOS_VALIDOS =
            Arrays.asList("Entrada", "Saída", "Investimento");

    public void cadastrarCategoria(Categoria categoria) throws SQLException, IOException, Exception {
        validar(categoria);

        try (Connection conn = BancoDados.conectar()) {
            CategoriaDAO dao = new CategoriaDAO(conn);

            Categoria existente = dao.buscarPorNome(categoria.getNome());
            if (existente != null) {
                throw new Exception("Já existe uma categoria com esse nome.");
            }

            dao.cadastrar(categoria);
        }
    }

    public void atualizarCategoria(Categoria categoria) throws SQLException, IOException, Exception {
        validar(categoria);

        try (Connection conn = BancoDados.conectar()) {
            CategoriaDAO dao = new CategoriaDAO(conn);

            Categoria existente = dao.buscarPorNome(categoria.getNome());
            if (existente != null && !existente.getId().equals(categoria.getId())) {
                throw new Exception("Já existe uma categoria com esse nome.");
            }

            dao.atualizar(categoria);
        }
    }

    public void excluirCategoria(Integer id) throws SQLException, IOException {
        try (Connection conn = BancoDados.conectar()) {
            CategoriaDAO dao = new CategoriaDAO(conn);
            dao.excluir(id);
        }
    }

    public Categoria buscarPorId(Integer id) throws SQLException, IOException {
        try (Connection conn = BancoDados.conectar()) {
            CategoriaDAO dao = new CategoriaDAO(conn);
            return dao.buscarPorId(id);
        }
    }

    public List<Categoria> listarCategorias() throws SQLException, IOException {
        try (Connection conn = BancoDados.conectar()) {
            CategoriaDAO dao = new CategoriaDAO(conn);
            return dao.buscarTodos();
        }
    }

    private void validar(Categoria categoria) throws Exception {
        if (categoria.getNome() == null || categoria.getNome().trim().isEmpty()) {
            throw new Exception("O nome da categoria é obrigatório.");
        }

        if (categoria.getTipo() == null || !TIPOS_VALIDOS.contains(categoria.getTipo())) {
            throw new Exception("Tipo de categoria inválido.");
        }
    }
}
