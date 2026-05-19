package com.lrvoley.api.controller;

import com.lrvoley.api.model.Ejercicio;
import com.lrvoley.api.repository.EjercicioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ejercicios")
public class EjercicioController {

    private final EjercicioRepository repo;

    public EjercicioController(EjercicioRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Ejercicio> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ejercicio> getById(@PathVariable int id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tipo/{tipo}")
    public List<Ejercicio> getByTipo(@PathVariable String tipo) {
        return repo.findByTipo(tipo);
    }

    @GetMapping("/porEntreno")
    public List<Ejercicio> getByEntreno(@RequestParam String nombre, @RequestParam int idUsuario) {
        return repo.findByEntrenoNombreAndIdUsuario(nombre, idUsuario);
    }

    @PostMapping
    public Ejercicio create(@RequestBody Ejercicio ejercicio) {
        return repo.save(ejercicio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ejercicio> update(@PathVariable int id, @RequestBody Ejercicio datos) {
        return repo.findById(id).map(e -> {
            e.setNombreEjer(datos.getNombreEjer());
            e.setTipo(datos.getTipo());
            e.setFinalidad(datos.getFinalidad());
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
