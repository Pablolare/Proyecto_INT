package com.lrvoley.api.repository;

import com.lrvoley.api.model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotaRepository extends JpaRepository<Nota, Integer> {

    List<Nota> findByIdUsuario(int idUsuario);

    @Query(value = "SELECT n.id_nota, n.id_usuario, n.id_entreno, n.titulo, n.contenido, n.fecha, e.nombre_entreno " +
            "FROM Nota n LEFT JOIN Entrenamientos e ON n.id_entreno = e.id_entreno " +
            "WHERE n.id_usuario = :idUsuario ORDER BY n.fecha DESC",
            nativeQuery = true)
    List<Object[]> findCompletasByIdUsuario(@Param("idUsuario") int idUsuario);
}
