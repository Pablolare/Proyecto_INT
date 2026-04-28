package com.javafx.ProyectoINT.modelos;

import com.javafx.ProyectoINT.ConexionBD;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EjerciciosDAO {

    public boolean insertarEjercicio(Ejercicios ejercicio) {
        String sql = "INSERT INTO Ejercicio (nombre_ejer, tipo, finalidad) VALUES (?, ?, ?)";

        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, ejercicio.getNombre_ejer());
            stmt.setString(2, ejercicio.getTipo());
            stmt.setString(3, ejercicio.getFinalidad());

            stmt.executeUpdate();
            stmt.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al insertar ejercicio: " + e.getMessage());
            return false;
        }
    }

    public int insertarEjercicioRetornarId(Ejercicios ejercicio) {
        String sql = "INSERT INTO Ejercicio (nombre_ejer, tipo, finalidad) VALUES (?, ?, ?)";

        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, ejercicio.getNombre_ejer());
            stmt.setString(2, ejercicio.getTipo());
            stmt.setString(3, ejercicio.getFinalidad());

            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            int id = -1;
            if (keys.next()) {
                id = keys.getInt(1);
            }
            keys.close();
            stmt.close();
            return id;

        } catch (SQLException e) {
            System.out.println("Error al insertar ejercicio: " + e.getMessage());
            return -1;
        }
    }

    public boolean actualizarEjercicio(Ejercicios ejercicio) {
        String sql = "UPDATE Ejercicio SET nombre_ejer=?, tipo=?, finalidad=? WHERE id_ejer=?";

        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, ejercicio.getNombre_ejer());
            stmt.setString(2, ejercicio.getTipo());
            stmt.setString(3, ejercicio.getFinalidad());
            stmt.setInt(4, ejercicio.getId_ejer());

            stmt.executeUpdate();
            stmt.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar ejercicio: " + e.getMessage());
            return false;
        }
    }

    public boolean borrarEjercicio(int id_ejer) {
        String sql = "DELETE FROM Ejercicio WHERE id_ejer=?";

        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_ejer);

            stmt.executeUpdate();
            stmt.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al borrar ejercicio: " + e.getMessage());
            return false;
        }
    }

    public ObservableList<Ejercicios> obtenerEjerciciosPorEntreno(String nombreEntreno, int idUsuario) {
        ObservableList<Ejercicios> lista = FXCollections.observableArrayList();
        String sql = "SELECT e.* FROM Ejercicio e " +
                     "INNER JOIN Entrenamientos t ON e.id_ejer = t.id_ejer " +
                     "WHERE t.nombre_entreno = ? AND t.id_usuario = ?";

        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombreEntreno);
            stmt.setInt(2, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Ejercicios ejercicio = new Ejercicios(
                        rs.getInt("id_ejer"),
                        rs.getString("nombre_ejer"),
                        rs.getString("tipo"),
                        rs.getString("finalidad"));
                lista.add(ejercicio);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Error al obtener ejercicios por entreno: " + e.getMessage());
        }

        return lista;
    }

    public ObservableList<Ejercicios> cargarEjerciciosDesdeBD() {
        ObservableList<Ejercicios> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Ejercicio";

        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Ejercicios ejercicio = new Ejercicios(
                        rs.getInt("id_ejer"),
                        rs.getString("nombre_ejer"),
                        rs.getString("tipo"),
                        rs.getString("finalidad"));
                lista.add(ejercicio);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Error al obtener ejercicios: " + e.getMessage());
        }

        return lista;
    }
}
