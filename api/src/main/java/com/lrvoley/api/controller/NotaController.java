package com.lrvoley.api.controller;

import com.lrvoley.api.model.Nota;
import com.lrvoley.api.repository.NotaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notas")
public class NotaController {

    private final NotaRepository repo;

    public NotaController(NotaRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Nota> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Nota> getById(@PathVariable int id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<Nota> getByUsuario(@PathVariable int idUsuario) {
        return repo.findByIdUsuario(idUsuario);
    }

    @PostMapping
    public Nota create(@RequestBody Nota nota) {
        return repo.save(nota);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Nota> update(@PathVariable int id, @RequestBody Nota datos) {
        return repo.findById(id).map(n -> {
            n.setTitulo(datos.getTitulo());
            n.setContenido(datos.getContenido());
            n.setIdEntreno(datos.getIdEntreno());
            return ResponseEntity.ok(repo.save(n));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
