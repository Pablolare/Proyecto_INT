package com.lrvoley.api.controller;

import com.lrvoley.api.model.Entrenamiento;
import com.lrvoley.api.repository.EntrenamientoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entrenamientos")
public class EntrenamientoController {

    private final EntrenamientoRepository repo;

    public EntrenamientoController(EntrenamientoRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Entrenamiento> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entrenamiento> getById(@PathVariable int id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<Entrenamiento> getByUsuario(@PathVariable int idUsuario) {
        return repo.findByIdUsuario(idUsuario);
    }

    @PostMapping
    public Entrenamiento create(@RequestBody Entrenamiento entrenamiento) {
        return repo.save(entrenamiento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entrenamiento> update(@PathVariable int id, @RequestBody Entrenamiento datos) {
        return repo.findById(id).map(e -> {
            e.setNombreEntreno(datos.getNombreEntreno());
            e.setRepeticiones(datos.getRepeticiones());
            e.setFallos(datos.getFallos());
            e.setAciertos(datos.getAciertos());
            e.setCompletado(datos.isCompletado());
            return ResponseEntity.ok(repo.save(e));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
