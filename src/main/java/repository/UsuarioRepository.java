package repository;

import model.UsuarioModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {

    private static final String URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public UsuarioRepository() {
        criarTabela();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private void criarTabela() {
        String sql = """
            CREATE TABLE IF NOT EXISTS usuarios (
                id IDENTITY PRIMARY KEY,
                nome VARCHAR(100),
                email VARCHAR(100)
            )
        """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela", e);
        }
    }
    //map
    private UsuarioModel map(ResultSet rs) throws SQLException {
        return new UsuarioModel(
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getString("email")
        );
    }

    // CREATE
    public void salvar(UsuarioModel usuario) {
        String sql = "INSERT INTO usuarios (nome, email) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.executeUpdate();
            //pega id gerado automatico
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar usuário", e);
        }
    }

    // READ - todos
    public List<UsuarioModel> listar() {
        List<UsuarioModel> usuarios = new ArrayList<>();
        String sql = "SELECT id, nome, email FROM usuarios";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuarios.add(map(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários", e);
        }

        return usuarios;
    }

    // READ - por ID
    public UsuarioModel buscarPorId(Long id) {
        String sql = "SELECT id, nome, email FROM usuarios WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário", e);
        }

        return null;
    }

    // UPDATE
    public boolean atualizar(UsuarioModel usuario) {
        String sql = """
            UPDATE usuarios
            SET nome = ?, email = ?
            WHERE id = ?
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setLong(3, usuario.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário", e);
        }
    }

    // DELETE
    public boolean remover(Long id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover usuário", e);
        }
    }
}