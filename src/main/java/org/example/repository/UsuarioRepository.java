package org.example.repository;

import org.example.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository
        extends JpaRepository<UsuarioModel, Long> {
}