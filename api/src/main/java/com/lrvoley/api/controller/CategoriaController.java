package com.lrvoley.api.controller;

import com.lrvoley.api.model.Categoria;
import com.lrvoley.api.repository.CategoriaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaRepository repo;

    public CategoriaController(CategoriaRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Categoria> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getById(@PathVariable int id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ejercicio/{idEjer}")
    public List<Categoria> getByEjercicio(@PathVariable int idEjer) {
        return repo.findByEjercicioId(idEjer);
    }

    @PostMapping
    public Categoria create(@RequestBody Categoria categoria) {
        return repo.save(categoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> update(@PathVariable int id, @RequestBody Categoria datos) {
        return repo.findById(id).map(c -> {
            c.setNombre(datos.getNombre());
            c.setDescripcion(datos.getDescripcion());
            return ResponseEntity.ok(repo.save(c));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/ejercicio/{idEjer}")
    public ResponseEntity<Void> asignarCategorias(@PathVariable int idEjer,
                                                   @RequestBody List<Integer> idCategorias) {
        repo.deleteEjercicioCategoria(idEjer);
        for (int idCat : idCategorias) {
            repo.insertEjercicioCategoria(idEjer, idCat);
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
