package com.lrvoley.api.repository;

import com.lrvoley.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByLogin(String login);
    Optional<Usuario> findByCorreo(String correo);
}
