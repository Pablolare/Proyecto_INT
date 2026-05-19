package com.lrvoley.api.repository;

import com.lrvoley.api.model.Entrenamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EntrenamientoRepository extends JpaRepository<Entrenamiento, Integer> {

    List<Entrenamiento> findByIdUsuario(int idUsuario);
    List<Entrenamiento> findByIdUsuarioAndCompletado(int idUsuario, boolean completado);
    List<Entrenamiento> findByNombreEntrenoAndIdUsuario(String nombreEntreno, int idUsuario);

    @Query(value = "SELECT nombre_entreno, COUNT(*) as num_ejercicios, " +
            "MAX(descripcion) as descripcion, " +
            "SUM(fallos) as fallos, SUM(aciertos) as aciertos, MIN(completado) as completado " +
            "FROM Entrenamientos WHERE id_usuario = :idUsuario GROUP BY nombre_entreno ORDER BY nombre_entreno ASC",
            nativeQuery = true)
    List<Object[]> findAgrupadosByIdUsuario(@Param("idUsuario") int idUsuario);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Entrenamientos WHERE nombre_entreno = :nombre AND id_usuario = :idUsuario",
            nativeQuery = true)
    void deleteByNombreAndIdUsuario(@Param("nombre") String nombre, @Param("idUsuario") int idUsuario);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Entrenamientos WHERE nombre_entreno = :nombre AND id_ejer = :idEjer AND id_usuario = :idUsuario",
            nativeQuery = true)
    void deleteByNombreAndIdEjerAndIdUsuario(@Param("nombre") String nombre, @Param("idEjer") int idEjer, @Param("idUsuario") int idUsuario);

    @Query(value = "SELECT id_entreno, aciertos, fallos, nombre_entreno FROM Entrenamientos " +
            "WHERE id_usuario = :idUsuario ORDER BY id_entreno ASC",
            nativeQuery = true)
    List<Object[]> findProgresionByIdUsuario(@Param("idUsuario") int idUsuario);

    @Query(value = "SELECT COUNT(*) as total_entrenos, SUM(aciertos) as total_aciertos, SUM(fallos) as total_fallos, " +
            "AVG(aciertos) as promedio_aciertos, AVG(fallos) as promedio_fallos " +
            "FROM Entrenamientos WHERE id_usuario = :idUsuario",
            nativeQuery = true)
    List<Object[]> findEstadisticasByIdUsuario(@Param("idUsuario") int idUsuario);

    @Query(value = "SELECT MIN(id_entreno) FROM Entrenamientos WHERE nombre_entreno = :nombre AND id_usuario = :idUsuario",
            nativeQuery = true)
    Integer findPrimeroByNombreAndIdUsuario(@Param("nombre") String nombre, @Param("idUsuario") int idUsuario);
}
