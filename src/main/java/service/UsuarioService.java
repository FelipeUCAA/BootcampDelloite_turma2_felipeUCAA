package service;

import model.UsuarioModel;
import repository.UsuarioRepository;
import validation.UsuarioValidator;

import java.util.List;

public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public void criar(String nome, String email) {
        UsuarioValidator.validarNome(nome);
        UsuarioValidator.validarEmail(email);

        repository.salvar(new UsuarioModel(nome, email));
    }

    public List<UsuarioModel> listar() {
        return repository.listar();
    }

    public UsuarioModel buscar(Long id) {
        return repository.buscarPorId(id);
    }

    public boolean atualizar(Long id, String nome, String email) {
        UsuarioModel usuario = repository.buscarPorId(id);

        if (usuario == null) {
            return false;
        }

        UsuarioValidator.validarNome(nome);
        UsuarioValidator.validarEmail(email);

        usuario.setNome(nome);
        usuario.setEmail(email);

        return repository.atualizar(usuario);
    }

    public boolean deletar(Long id) {
        return repository.remover(id);
    }
}