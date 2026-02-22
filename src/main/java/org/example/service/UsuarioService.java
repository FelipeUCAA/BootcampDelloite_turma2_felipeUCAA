package org.example.service;

import org.example.model.UsuarioModel;
import org.example.repository.UsuarioRepository;
import org.example.validation.UsuarioValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public void criar(String nome, String email) {
        UsuarioValidator.validarNome(nome);
        UsuarioValidator.validarEmail(email);

        repository.save(new UsuarioModel(nome, email));
    }

    public List<UsuarioModel> listar() {
        return repository.findAll();
    }

    public UsuarioModel buscar(Long id) {
        return repository.findById(id).orElse(null);
    }

    public boolean atualizar(Long id, String nome, String email) {

        UsuarioModel usuario = repository.findById(id).orElse(null);

        if (usuario == null) {
            return false;
        }

        UsuarioValidator.validarNome(nome);
        UsuarioValidator.validarEmail(email);

        usuario.setNome(nome);
        usuario.setEmail(email);

        repository.save(usuario); // save também atualiza

        return true;
    }

    public boolean deletar(Long id) {

        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}