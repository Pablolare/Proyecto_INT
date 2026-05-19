package com.javafx.ProyectoINT.modelos;

import com.javafx.ProyectoINT.ApiCliente;
import javafx.collections.ObservableList;

public class NotaDAO {

    public boolean insertarNota(Nota nota) {
        return ApiCliente.insertarNota(nota);
    }

    public boolean actualizarNota(Nota nota) {
        return ApiCliente.actualizarNota(nota);
    }

    public boolean borrarNota(int id_nota) {
        return ApiCliente.borrarNota(id_nota);
    }

    public ObservableList<Nota> cargarNotasUsuario(int id_usuario) {
        return ApiCliente.cargarNotasUsuario(id_usuario);
    }

    public Integer obtenerPrimerIdEntrenoPorNombre(String nombreEntreno, int idUsuario) {
        return ApiCliente.obtenerPrimerIdEntrenoPorNombre(nombreEntreno, idUsuario);
    }
}
