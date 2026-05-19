package com.lrvoley.api.controller;

import com.lrvoley.api.dto.DatosProgresionDTO;
import com.lrvoley.api.dto.EntrenamientoAgrupadoDTO;
import com.lrvoley.api.dto.EstadisticasDTO;
import com.lrvoley.api.model.Entrenamiento;
import com.lrvoley.api.repository.EntrenamientoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

    @GetMapping("/agrupados/{idUsuario}")
    public List<EntrenamientoAgrupadoDTO> getAgrupados(@PathVariable int idUsuario) {
        return repo.findAgrupadosByIdUsuario(idUsuario).stream().map(r ->
            new EntrenamientoAgrupadoDTO(
                (String) r[0],
                ((Number) r[1]).intValue(),
                r[2] != null ? (String) r[2] : "",
                ((Number) r[3]).intValue(),
                ((Number) r[4]).intValue(),
                r[5] instanceof Boolean ? (Boolean) r[5] : ((Number) r[5]).intValue() > 0
            )).collect(Collectors.toList());
    }

    @GetMapping("/porNombre")
    public List<Entrenamiento> getByNombre(@RequestParam String nombre, @RequestParam int idUsuario) {
        return repo.findByNombreEntrenoAndIdUsuario(nombre, idUsuario);
    }

    @GetMapping("/progresion/{idUsuario}")
    public List<DatosProgresionDTO> getProgresion(@PathVariable int idUsuario) {
        List<Object[]> rows = repo.findProgresionByIdUsuario(idUsuario);
        AtomicInteger contador = new AtomicInteger(1);
        return rows.stream().map(r -> new DatosProgresionDTO(
            contador.getAndIncrement(),
            ((Number) r[1]).intValue(),
            ((Number) r[2]).intValue(),
            (String) r[3]
        )).collect(Collectors.toList());
    }

    @GetMapping("/estadisticas/{idUsuario}")
    public ResponseEntity<EstadisticasDTO> getEstadisticas(@PathVariable int idUsuario) {
        List<Object[]> rows = repo.findEstadisticasByIdUsuario(idUsuario);
        if (rows.isEmpty() || rows.get(0)[0] == null) {
            return ResponseEntity.ok(new EstadisticasDTO(0, 0, 0, 0, 0));
        }
        Object[] r = rows.get(0);
        return ResponseEntity.ok(new EstadisticasDTO(
            ((Number) r[0]).intValue(),
            r[1] != null ? ((Number) r[1]).intValue() : 0,
            r[2] != null ? ((Number) r[2]).intValue() : 0,
            r[3] != null ? ((Number) r[3]).doubleValue() : 0.0,
            r[4] != null ? ((Number) r[4]).doubleValue() : 0.0
        ));
    }

    @GetMapping("/primero")
    public ResponseEntity<Integer> getPrimero(@RequestParam String nombre, @RequestParam int idUsuario) {
        return ResponseEntity.ok(repo.findPrimeroByNombreAndIdUsuario(nombre, idUsuario));
    }

    @PostMapping
    public Entrenamiento create(@RequestBody Entrenamiento entrenamiento) {
        return repo.save(entrenamiento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entrenamiento> update(@PathVariable int id, @RequestBody Entrenamiento datos) {
        return repo.findById(id).map(e -> {
            e.setNombreEntreno(datos.getNombreEntreno());
            if (datos.getDescripcion() != null) e.setDescripcion(datos.getDescripcion());
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

    @DeleteMapping("/porNombre")
    public ResponseEntity<Void> deleteByNombre(@RequestParam String nombre, @RequestParam int idUsuario) {
        repo.deleteByNombreAndIdUsuario(nombre, idUsuario);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/porEjercicio")
    public ResponseEntity<Void> deleteByEjercicio(@RequestParam String nombre,
                                                   @RequestParam int idEjer,
                                                   @RequestParam int idUsuario) {
        repo.deleteByNombreAndIdEjerAndIdUsuario(nombre, idEjer, idUsuario);
        return ResponseEntity.noContent().build();
    }
}
