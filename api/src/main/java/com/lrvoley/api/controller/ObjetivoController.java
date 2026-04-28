package com.lrvoley.api.controller;

import com.lrvoley.api.model.Objetivo;
import com.lrvoley.api.repository.ObjetivoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/objetivos")
public class ObjetivoController {

    private final ObjetivoRepository repo;

    public ObjetivoController(ObjetivoRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Objetivo> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Objetivo> getById(@PathVariable int id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<Objetivo> getByUsuario(@PathVariable int idUsuario) {
        return repo.findByIdUsuario(idUsuario);
    }

    @PostMapping
    public Objetivo create(@RequestBody Objetivo objetivo) {
        return repo.save(objetivo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Objetivo> update(@PathVariable int id, @RequestBody Objetivo datos) {
        return repo.findById(id).map(o -> {
            o.setDescripcion(datos.getDescripcion());
            o.setMetaAciertos(datos.getMetaAciertos());
            o.setMetaRepeticiones(datos.getMetaRepeticiones());
            o.setCumplido(datos.isCumplido());
            return ResponseEntity.ok(repo.save(o));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
