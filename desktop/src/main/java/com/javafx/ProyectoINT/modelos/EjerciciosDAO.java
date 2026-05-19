package com.javafx.ProyectoINT.modelos;

import com.javafx.ProyectoINT.ApiCliente;
import javafx.collections.ObservableList;

public class EjerciciosDAO {

    public boolean insertarEjercicio(Ejercicios ejercicio) {
        return ApiCliente.insertarEjercicio(ejercicio);
    }

    public int insertarEjercicioRetornarId(Ejercicios ejercicio) {
        return ApiCliente.insertarEjercicioRetornarId(ejercicio);
    }

    public boolean actualizarEjercicio(Ejercicios ejercicio) {
        return ApiCliente.actualizarEjercicio(ejercicio);
    }

    public boolean borrarEjercicio(int id_ejer) {
        return ApiCliente.borrarEjercicio(id_ejer);
    }

    public ObservableList<Ejercicios> obtenerEjerciciosPorEntreno(String nombreEntreno, int idUsuario) {
        return ApiCliente.obtenerEjerciciosPorEntreno(nombreEntreno, idUsuario);
    }

    public ObservableList<Ejercicios> cargarEjerciciosDesdeBD() {
        return ApiCliente.cargarEjercicios();
    }
}
