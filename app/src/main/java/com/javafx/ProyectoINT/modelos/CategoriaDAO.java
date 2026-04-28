package com.javafx.ProyectoINT.modelos;

import com.javafx.ProyectoINT.ConexionBD;
import java.sql.*;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CategoriaDAO {

    public boolean insertarCategoria(Categoria categoria) {
        String sql = "INSERT INTO Categoria (nombre, descripcion) VALUES (?, ?)";

        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, categoria.getNombre());
            stmt.setString(2, categoria.getDescripcion());
            stmt.executeUpdate();
            stmt.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al insertar categoria: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarCategoria(Categoria categoria) {
        String sql = "UPDATE Categoria SET nombre=?, descripcion=? WHERE id_categoria=?";

        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, categoria.getNombre());
            stmt.setString(2, categoria.getDescripcion());
            stmt.setInt(3, categoria.getId_categoria());
            stmt.executeUpdate();
            stmt.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar categoria: " + e.getMessage());
            return false;
        }
    }

    public boolean borrarCategoria(int id_categoria) {
        String sql = "DELETE FROM Categoria WHERE id_categoria=?";

        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_categoria);
            stmt.executeUpdate();
            stmt.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al borrar categoria: " + e.getMessage());
            return false;
        }
    }

    public ObservableList<Categoria> cargarCategoriasDesdeBD() {
        ObservableList<Categoria> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Categoria ORDER BY nombre ASC";

        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")));
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Error al obtener categorias: " + e.getMessage());
        }

        return lista;
    }

    public ObservableList<Categoria> obtenerCategoriasPorEjercicio(int id_ejer) {
        ObservableList<Categoria> lista = FXCollections.observableArrayList();
        String sql = "SELECT c.* FROM Categoria c " +
                     "INNER JOIN Ejercicio_Categoria ec ON c.id_categoria = ec.id_categoria " +
                     "WHERE ec.id_ejer = ?";

        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_ejer);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")));
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Error al obtener categorias por ejercicio: " + e.getMessage());
        }

        return lista;
    }

    public boolean asignarCategoriasAEjercicio(int id_ejer, List<Categoria> categorias) {
        String sqlDelete = "DELETE FROM Ejercicio_Categoria WHERE id_ejer=?";
        String sqlInsert = "INSERT INTO Ejercicio_Categoria (id_ejer, id_categoria) VALUES (?, ?)";

        try {
            Connection conn = ConexionBD.getInstancia().getConexion();

            PreparedStatement stmtDelete = conn.prepareStatement(sqlDelete);
            stmtDelete.setInt(1, id_ejer);
            stmtDelete.executeUpdate();
            stmtDelete.close();

            for (Categoria cat : categorias) {
                PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert);
                stmtInsert.setInt(1, id_ejer);
                stmtInsert.setInt(2, cat.getId_categoria());
                stmtInsert.executeUpdate();
                stmtInsert.close();
            }

            return true;

        } catch (SQLException e) {
            System.out.println("Error al asignar categorias: " + e.getMessage());
            return false;
        }
    }
}
