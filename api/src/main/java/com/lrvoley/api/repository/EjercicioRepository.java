package com.lrvoley.api.repository;

import com.lrvoley.api.model.Ejercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EjercicioRepository extends JpaRepository<Ejercicio, Integer> {

    List<Ejercicio> findByTipo(String tipo);

    @Query(value = "SELECT e.* FROM Ejercicio e " +
            "INNER JOIN Entrenamientos t ON e.id_ejer = t.id_ejer " +
            "WHERE t.nombre_entreno = :nombre AND t.id_usuario = :idUsuario",
            nativeQuery = true)
    List<Ejercicio> findByEntrenoNombreAndIdUsuario(@Param("nombre") String nombre, @Param("idUsuario") int idUsuario);
}
