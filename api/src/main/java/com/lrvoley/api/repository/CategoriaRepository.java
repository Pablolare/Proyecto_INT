package com.lrvoley.api.repository;

import com.lrvoley.api.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    @Query(value = "SELECT c.* FROM Categoria c " +
            "INNER JOIN Ejercicio_Categoria ec ON c.id_categoria = ec.id_categoria " +
            "WHERE ec.id_ejer = :idEjer",
            nativeQuery = true)
    List<Categoria> findByEjercicioId(@Param("idEjer") int idEjer);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Ejercicio_Categoria WHERE id_ejer = :idEjer", nativeQuery = true)
    void deleteEjercicioCategoria(@Param("idEjer") int idEjer);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Ejercicio_Categoria (id_ejer, id_categoria) VALUES (:idEjer, :idCat)", nativeQuery = true)
    void insertEjercicioCategoria(@Param("idEjer") int idEjer, @Param("idCat") int idCat);
}
