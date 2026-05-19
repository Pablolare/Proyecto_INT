package com.javafx.ProyectoINT.modelos;

import com.javafx.ProyectoINT.ApiCliente;
import javafx.collections.ObservableList;

public class ObjetivoDAO {

    public boolean insertarObjetivo(Objetivo objetivo) {
        return ApiCliente.insertarObjetivo(objetivo);
    }

    public boolean actualizarObjetivo(Objetivo objetivo) {
        return ApiCliente.actualizarObjetivo(objetivo);
    }

    public boolean borrarObjetivo(int id_objetivo) {
        return ApiCliente.borrarObjetivo(id_objetivo);
    }

    public ObservableList<Objetivo> cargarObjetivosUsuario(int id_usuario) {
        return ApiCliente.cargarObjetivosUsuario(id_usuario);
    }
}
