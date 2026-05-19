package com.lrvoley.api.controller;

import com.lrvoley.api.model.Usuario;
import com.lrvoley.api.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepository repo;

    public UsuarioController(UsuarioRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Usuario> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable int id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Usuario create(@RequestBody Usuario usuario) {
        return repo.save(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(@PathVariable int id, @RequestBody Usuario datos) {
        return repo.findById(id).map(u -> {
            u.setNombre(datos.getNombre());
            u.setApellido(datos.getApellido());
            u.setCorreo(datos.getCorreo());
            u.setRol(datos.getRol());
            return ResponseEntity.ok(repo.save(u));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody Map<String, String> credenciales) {
        String loginOCorreo = credenciales.get("login");
        String contrasena = credenciales.get("contrasena");
        java.util.Optional<Usuario> usuario = repo.findByLogin(loginOCorreo);
        if (usuario.isEmpty()) {
            usuario = repo.findByCorreo(loginOCorreo);
        }
        return usuario.filter(u -> u.getContrasena().equals(contrasena))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).build());
    }
}
