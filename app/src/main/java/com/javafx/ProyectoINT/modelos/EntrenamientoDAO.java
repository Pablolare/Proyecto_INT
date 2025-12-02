package com.javafx.ProyectoINT.modelos;

import com.javafx.ProyectoINT.ConexionBD;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EntrenamientoDAO {

    public boolean insertarEntrenamiento(Entrenamiento entrenamiento) {
        String sql = "INSERT INTO entrenamientos (id_usuario, id_ejer, nombre_entreno, repeticiones, fallos, aciertos, completado) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, entrenamiento.getId_usuario());
            stmt.setInt(2, entrenamiento.getId_ejer());
            stmt.setString(3, entrenamiento.getNombre_entreno());
            stmt.setInt(4, entrenamiento.getRepeticiones());
            stmt.setInt(5, entrenamiento.getFallos());
            stmt.setInt(6, entrenamiento.getAciertos());
            stmt.setBoolean(7, entrenamiento.isCompletado());

            stmt.executeUpdate();
            stmt.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al insertar entrenamiento: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarEntrenamiento(Entrenamiento entrenamiento) {
        String sql = "UPDATE entrenamientos SET id_usuario=?, id_ejer=?, nombre_entreno=?, repeticiones=?, fallos=?, aciertos=?, completado=? WHERE id_entreno=?";

        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, entrenamiento.getId_usuario());
            stmt.setInt(2, entrenamiento.getId_ejer());
            stmt.setString(3, entrenamiento.getNombre_entreno());
            stmt.setInt(4, entrenamiento.getRepeticiones());
            stmt.setInt(5, entrenamiento.getFallos());
            stmt.setInt(6, entrenamiento.getAciertos());
            stmt.setBoolean(7, entrenamiento.isCompletado());
            stmt.setInt(8, entrenamiento.getId_entreno());

            stmt.executeUpdate();
            stmt.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar entrenamiento: " + e.getMessage());
            return false;
        }
    }

    public boolean borrarEntrenamiento(int id_entreno) {
        String sql = "DELETE FROM entrenamientos WHERE id_entreno=?";

        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_entreno);

            stmt.executeUpdate();
            stmt.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al borrar entrenamiento: " + e.getMessage());
            return false;
        }
    }

    public ObservableList<Entrenamiento> cargarEntrenamientosDesdeBD() {
        ObservableList<Entrenamiento> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Entrenamientos";

        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Entrenamiento entrenamiento = new Entrenamiento(
                        rs.getInt("id_entreno"),
                        rs.getInt("id_usuario"),
                        rs.getInt("id_ejer"),
                        rs.getString("nombre_entreno"),
                        rs.getInt("repeticiones"),
                        rs.getInt("fallos"),
                        rs.getInt("aciertos"),
                        rs.getBoolean("completado"));
                lista.add(entrenamiento);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Error al obtener entrenamientos: " + e.getMessage());
        }

        return lista;
    }
}
