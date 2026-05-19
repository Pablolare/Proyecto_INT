package com.javafx.ProyectoINT;

import com.google.gson.Gson;
import com.javafx.ProyectoINT.dto.*;
import com.javafx.ProyectoINT.modelos.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApiCliente {

    private static final String BASE = "http://localhost:8080/api";
    private static final HttpClient HTTP = HttpClient.newHttpClient();
    private static final Gson GSON = new Gson();

    // ==================== Helpers HTTP ====================

    private static String get(String url) throws Exception {
        HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
        return HTTP.send(req, HttpResponse.BodyHandlers.ofString()).body();
    }

    private static HttpResponse<String> postRaw(String url, String json) throws Exception {
        HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();
        return HTTP.send(req, HttpResponse.BodyHandlers.ofString());
    }

    private static String post(String url, String json) throws Exception {
        return postRaw(url, json).body();
    }

    private static String put(String url, String json) throws Exception {
        HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();
        return HTTP.send(req, HttpResponse.BodyHandlers.ofString()).body();
    }

    private static int delete(String url) throws Exception {
        HttpRequest req = HttpRequest.newBuilder(URI.create(url)).DELETE().build();
        return HTTP.send(req, HttpResponse.BodyHandlers.ofString()).statusCode();
    }

    private static String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    // ==================== USUARIO ====================

    public static Usuario login(String loginOCorreo, String contrasena) {
        try {
            String body = GSON.toJson(Map.of("login", loginOCorreo, "contrasena", contrasena));
            HttpResponse<String> resp = postRaw(BASE + "/usuarios/login", body);
            if (resp.statusCode() != 200) return null;
            UsuarioDto dto = GSON.fromJson(resp.body(), UsuarioDto.class);
            if (dto == null || dto.idUsuario == 0) return null;
            return new Usuario(dto.idUsuario, dto.nombre, dto.apellido, dto.login, dto.contrasena, dto.rol, dto.correo);
        } catch (Exception e) {
            System.out.println("Error login: " + e.getMessage());
            return null;
        }
    }

    public static boolean insertarUsuario(Usuario u) {
        try {
            String body = GSON.toJson(Map.of(
                "nombre", u.getNombre(), "apellido", u.getApellido(),
                "login", u.getLogin(), "contrasena", u.getContraseña(),
                "rol", u.getRol(), "correo", u.getCorreo()
            ));
            post(BASE + "/usuarios", body);
            return true;
        } catch (Exception e) { System.out.println("Error insertarUsuario: " + e.getMessage()); return false; }
    }

    public static boolean actualizarUsuario(Usuario u) {
        try {
            String body = GSON.toJson(Map.of(
                "nombre", u.getNombre(), "apellido", u.getApellido(),
                "login", u.getLogin(), "contrasena", u.getContraseña(),
                "rol", u.getRol(), "correo", u.getCorreo()
            ));
            put(BASE + "/usuarios/" + u.getId_usuario(), body);
            return true;
        } catch (Exception e) { System.out.println("Error actualizarUsuario: " + e.getMessage()); return false; }
    }

    public static boolean borrarUsuario(int id) {
        try { delete(BASE + "/usuarios/" + id); return true; }
        catch (Exception e) { System.out.println("Error borrarUsuario: " + e.getMessage()); return false; }
    }

    public static Usuario obtenerUsuarioPorId(int id) {
        try {
            UsuarioDto dto = GSON.fromJson(get(BASE + "/usuarios/" + id), UsuarioDto.class);
            if (dto == null || dto.idUsuario == 0) return null;
            return new Usuario(dto.idUsuario, dto.nombre, dto.apellido, dto.login, dto.contrasena, dto.rol, dto.correo);
        } catch (Exception e) { System.out.println("Error obtenerUsuarioPorId: " + e.getMessage()); return null; }
    }

    public static ObservableList<Usuario> cargarUsuarios() {
        ObservableList<Usuario> lista = FXCollections.observableArrayList();
        try {
            UsuarioDto[] dtos = GSON.fromJson(get(BASE + "/usuarios"), UsuarioDto[].class);
            if (dtos != null) for (UsuarioDto d : dtos)
                lista.add(new Usuario(d.idUsuario, d.nombre, d.apellido, d.login, d.contrasena, d.rol, d.correo));
        } catch (Exception e) { System.out.println("Error cargarUsuarios: " + e.getMessage()); }
        return lista;
    }

    // ==================== ENTRENAMIENTO ====================

    public static boolean insertarEntrenamiento(Entrenamiento e) {
        try {
            String desc = e.getDescripcion() != null ? e.getDescripcion() : "";
            String body = "{\"idUsuario\":" + e.getId_usuario() + ",\"idEjer\":" + e.getId_ejer()
                + ",\"nombreEntreno\":" + GSON.toJson(e.getNombre_entreno())
                + ",\"descripcion\":" + GSON.toJson(desc)
                + ",\"fallos\":" + e.getFallos()
                + ",\"aciertos\":" + e.getAciertos()
                + ",\"completado\":" + e.isCompletado() + "}";
            HttpResponse<String> resp = postRaw(BASE + "/entrenamientos", body);
            return resp.statusCode() >= 200 && resp.statusCode() < 300;
        } catch (Exception ex) { System.out.println("Error insertarEntrenamiento: " + ex.getMessage()); return false; }
    }

    public static boolean actualizarEntrenamiento(Entrenamiento e) {
        try {
            String desc = e.getDescripcion() != null ? e.getDescripcion() : "";
            String body = "{\"idUsuario\":" + e.getId_usuario() + ",\"idEjer\":" + e.getId_ejer()
                + ",\"nombreEntreno\":" + GSON.toJson(e.getNombre_entreno())
                + ",\"descripcion\":" + GSON.toJson(desc)
                + ",\"fallos\":" + e.getFallos()
                + ",\"aciertos\":" + e.getAciertos()
                + ",\"completado\":" + e.isCompletado() + "}";
            put(BASE + "/entrenamientos/" + e.getId_entreno(), body);
            return true;
        } catch (Exception ex) { System.out.println("Error actualizarEntrenamiento: " + ex.getMessage()); return false; }
    }

    public static boolean borrarEntrenamiento(int id) {
        try { delete(BASE + "/entrenamientos/" + id); return true; }
        catch (Exception e) { System.out.println("Error borrarEntrenamiento: " + e.getMessage()); return false; }
    }

    public static boolean borrarEntrenamientoPorNombre(String nombre, int idUsuario) {
        try {
            delete(BASE + "/entrenamientos/porNombre?nombre=" + enc(nombre) + "&idUsuario=" + idUsuario);
            return true;
        } catch (Exception e) { System.out.println("Error borrarEntrenamientoPorNombre: " + e.getMessage()); return false; }
    }

    public static boolean borrarEntrenamientoPorEjercicio(String nombre, int idEjer, int idUsuario) {
        try {
            delete(BASE + "/entrenamientos/porEjercicio?nombre=" + enc(nombre) + "&idEjer=" + idEjer + "&idUsuario=" + idUsuario);
            return true;
        } catch (Exception e) { System.out.println("Error borrarEntrenamientoPorEjercicio: " + e.getMessage()); return false; }
    }

    public static ObservableList<Entrenamiento> cargarEntrenamientos() {
        return cargarEntrenamientosList(BASE + "/entrenamientos");
    }

    public static ObservableList<Entrenamiento> obtenerEntrenamientosUsuario(int idUsuario) {
        return cargarEntrenamientosList(BASE + "/entrenamientos/usuario/" + idUsuario);
    }

    public static ObservableList<Entrenamiento> obtenerEntrenamientosPorNombre(String nombre, int idUsuario) {
        return cargarEntrenamientosList(BASE + "/entrenamientos/porNombre?nombre=" + enc(nombre) + "&idUsuario=" + idUsuario);
    }

    private static ObservableList<Entrenamiento> cargarEntrenamientosList(String url) {
        ObservableList<Entrenamiento> lista = FXCollections.observableArrayList();
        try {
            EntrenamientoDto[] dtos = GSON.fromJson(get(url), EntrenamientoDto[].class);
            if (dtos != null) for (EntrenamientoDto d : dtos)
                lista.add(new Entrenamiento(d.idEntreno, d.idUsuario, d.idEjer, d.nombreEntreno,
                        d.descripcion, d.fallos, d.aciertos, d.completado));
        } catch (Exception e) { System.out.println("Error cargarEntrenamientos: " + e.getMessage()); }
        return lista;
    }

    public static ObservableList<Entrenamiento> obtenerEntrenosAgrupadosUsuario(int idUsuario) {
        ObservableList<Entrenamiento> lista = FXCollections.observableArrayList();
        try {
            EntrenamientoAgrupadoDto[] dtos = GSON.fromJson(
                    get(BASE + "/entrenamientos/agrupados/" + idUsuario), EntrenamientoAgrupadoDto[].class);
            if (dtos != null) for (EntrenamientoAgrupadoDto d : dtos) {
                Entrenamiento ent = new Entrenamiento(0, idUsuario, 0, d.nombreEntreno,
                        d.descripcion, d.fallos, d.aciertos, d.completado);
                ent.setNumEjercicios(d.numEjercicios);
                lista.add(ent);
            }
        } catch (Exception e) { System.out.println("Error obtenerEntrenosAgrupados: " + e.getMessage()); }
        return lista;
    }

    public static List<EntrenamientoDAO.DatosProgresion> obtenerProgresionUsuario(int idUsuario) {
        List<EntrenamientoDAO.DatosProgresion> lista = new ArrayList<>();
        try {
            DatosProgresionDto[] dtos = GSON.fromJson(
                    get(BASE + "/entrenamientos/progresion/" + idUsuario), DatosProgresionDto[].class);
            if (dtos != null) for (DatosProgresionDto d : dtos)
                lista.add(new EntrenamientoDAO.DatosProgresion(d.numeroEntreno, d.aciertos, d.fallos, d.nombreEntreno));
        } catch (Exception e) { System.out.println("Error obtenerProgresion: " + e.getMessage()); }
        return lista;
    }

    public static EntrenamientoDAO.EstadisticasUsuario obtenerEstadisticas(int idUsuario) {
        try {
            EstadisticasDto d = GSON.fromJson(get(BASE + "/entrenamientos/estadisticas/" + idUsuario), EstadisticasDto.class);
            if (d == null) return null;
            return new EntrenamientoDAO.EstadisticasUsuario(d.totalEntrenos, d.totalAciertos, d.totalFallos, d.promedioAciertos, d.promedioFallos);
        } catch (Exception e) { System.out.println("Error obtenerEstadisticas: " + e.getMessage()); return null; }
    }

    public static Integer obtenerPrimerIdEntrenoPorNombre(String nombre, int idUsuario) {
        try {
            String resp = get(BASE + "/entrenamientos/primero?nombre=" + enc(nombre) + "&idUsuario=" + idUsuario);
            if (resp == null || resp.isBlank() || resp.equals("null")) return null;
            return Integer.parseInt(resp.trim());
        } catch (Exception e) { System.out.println("Error obtenerPrimero: " + e.getMessage()); return null; }
    }

    // ==================== EJERCICIO ====================

    public static boolean insertarEjercicio(Ejercicios ej) {
        try {
            String body = GSON.toJson(Map.of("nombreEjer", ej.getNombre_ejer(), "tipo", ej.getTipo(), "finalidad", ej.getFinalidad()));
            post(BASE + "/ejercicios", body);
            return true;
        } catch (Exception e) { System.out.println("Error insertarEjercicio: " + e.getMessage()); return false; }
    }

    public static int insertarEjercicioRetornarId(Ejercicios ej) {
        try {
            String body = GSON.toJson(Map.of("nombreEjer", ej.getNombre_ejer(), "tipo", ej.getTipo(), "finalidad", ej.getFinalidad()));
            EjercicioDto dto = GSON.fromJson(post(BASE + "/ejercicios", body), EjercicioDto.class);
            return dto != null ? dto.idEjer : -1;
        } catch (Exception e) { System.out.println("Error insertarEjercicioRetornarId: " + e.getMessage()); return -1; }
    }

    public static boolean actualizarEjercicio(Ejercicios ej) {
        try {
            String body = GSON.toJson(Map.of("nombreEjer", ej.getNombre_ejer(), "tipo", ej.getTipo(), "finalidad", ej.getFinalidad()));
            put(BASE + "/ejercicios/" + ej.getId_ejer(), body);
            return true;
        } catch (Exception e) { System.out.println("Error actualizarEjercicio: " + e.getMessage()); return false; }
    }

    public static boolean borrarEjercicio(int id) {
        try { delete(BASE + "/ejercicios/" + id); return true; }
        catch (Exception e) { System.out.println("Error borrarEjercicio: " + e.getMessage()); return false; }
    }

    public static ObservableList<Ejercicios> cargarEjercicios() {
        return cargarEjerciciosList(BASE + "/ejercicios");
    }

    public static ObservableList<Ejercicios> obtenerEjerciciosPorEntreno(String nombre, int idUsuario) {
        return cargarEjerciciosList(BASE + "/ejercicios/porEntreno?nombre=" + enc(nombre) + "&idUsuario=" + idUsuario);
    }

    private static ObservableList<Ejercicios> cargarEjerciciosList(String url) {
        ObservableList<Ejercicios> lista = FXCollections.observableArrayList();
        try {
            EjercicioDto[] dtos = GSON.fromJson(get(url), EjercicioDto[].class);
            if (dtos != null) for (EjercicioDto d : dtos)
                lista.add(new Ejercicios(d.idEjer, d.nombreEjer, d.tipo, d.finalidad));
        } catch (Exception e) { System.out.println("Error cargarEjercicios: " + e.getMessage()); }
        return lista;
    }

    // ==================== NOTA ====================

    public static boolean insertarNota(Nota n) {
        try {
            String idEntrenoJson = n.getId_entreno() == null ? "null" : String.valueOf(n.getId_entreno());
            String body = "{\"idUsuario\":" + n.getId_usuario() + ",\"idEntreno\":" + idEntrenoJson +
                    ",\"titulo\":" + GSON.toJson(n.getTitulo()) + ",\"contenido\":" + GSON.toJson(n.getContenido()) + "}";
            post(BASE + "/notas", body);
            return true;
        } catch (Exception e) { System.out.println("Error insertarNota: " + e.getMessage()); return false; }
    }

    public static boolean actualizarNota(Nota n) {
        try {
            String idEntrenoJson = n.getId_entreno() == null ? "null" : String.valueOf(n.getId_entreno());
            String body = "{\"idEntreno\":" + idEntrenoJson +
                    ",\"titulo\":" + GSON.toJson(n.getTitulo()) + ",\"contenido\":" + GSON.toJson(n.getContenido()) + "}";
            put(BASE + "/notas/" + n.getId_nota(), body);
            return true;
        } catch (Exception e) { System.out.println("Error actualizarNota: " + e.getMessage()); return false; }
    }

    public static boolean borrarNota(int id) {
        try { delete(BASE + "/notas/" + id); return true; }
        catch (Exception e) { System.out.println("Error borrarNota: " + e.getMessage()); return false; }
    }

    public static ObservableList<Nota> cargarNotasUsuario(int idUsuario) {
        ObservableList<Nota> lista = FXCollections.observableArrayList();
        try {
            NotaCompletaDto[] dtos = GSON.fromJson(
                    get(BASE + "/notas/usuario/" + idUsuario + "/completas"), NotaCompletaDto[].class);
            if (dtos != null) for (NotaCompletaDto d : dtos) {
                Nota nota = new Nota(d.idNota, d.idUsuario, d.idEntreno, d.titulo, d.contenido, d.fecha);
                nota.setNombreEntreno(d.nombreEntreno);
                lista.add(nota);
            }
        } catch (Exception e) { System.out.println("Error cargarNotas: " + e.getMessage()); }
        return lista;
    }

    // ==================== OBJETIVO ====================

    public static boolean insertarObjetivo(Objetivo o) {
        try {
            String metaAc = o.getMeta_aciertos() == null ? "null" : String.valueOf(o.getMeta_aciertos());
            String metaRep = o.getMeta_repeticiones() == null ? "null" : String.valueOf(o.getMeta_repeticiones());
            String body = "{\"idUsuario\":" + o.getId_usuario() +
                    ",\"descripcion\":" + GSON.toJson(o.getDescripcion()) +
                    ",\"metaAciertos\":" + metaAc + ",\"metaRepeticiones\":" + metaRep +
                    ",\"cumplido\":" + o.isCumplido() + "}";
            post(BASE + "/objetivos", body);
            return true;
        } catch (Exception e) { System.out.println("Error insertarObjetivo: " + e.getMessage()); return false; }
    }

    public static boolean actualizarObjetivo(Objetivo o) {
        try {
            String metaAc = o.getMeta_aciertos() == null ? "null" : String.valueOf(o.getMeta_aciertos());
            String metaRep = o.getMeta_repeticiones() == null ? "null" : String.valueOf(o.getMeta_repeticiones());
            String body = "{\"descripcion\":" + GSON.toJson(o.getDescripcion()) +
                    ",\"metaAciertos\":" + metaAc + ",\"metaRepeticiones\":" + metaRep +
                    ",\"cumplido\":" + o.isCumplido() + "}";
            put(BASE + "/objetivos/" + o.getId_objetivo(), body);
            return true;
        } catch (Exception e) { System.out.println("Error actualizarObjetivo: " + e.getMessage()); return false; }
    }

    public static boolean borrarObjetivo(int id) {
        try { delete(BASE + "/objetivos/" + id); return true; }
        catch (Exception e) { System.out.println("Error borrarObjetivo: " + e.getMessage()); return false; }
    }

    public static ObservableList<Objetivo> cargarObjetivosUsuario(int idUsuario) {
        ObservableList<Objetivo> lista = FXCollections.observableArrayList();
        try {
            ObjetivoDto[] dtos = GSON.fromJson(get(BASE + "/objetivos/usuario/" + idUsuario), ObjetivoDto[].class);
            if (dtos != null) for (ObjetivoDto d : dtos)
                lista.add(new Objetivo(d.idObjetivo, d.idUsuario, d.descripcion,
                        d.metaAciertos, d.metaRepeticiones, d.cumplido, d.fechaCreacion));
        } catch (Exception e) { System.out.println("Error cargarObjetivos: " + e.getMessage()); }
        return lista;
    }

    // ==================== CATEGORIA ====================

    public static boolean insertarCategoria(Categoria c) {
        try {
            String body = GSON.toJson(Map.of("nombre", c.getNombre(), "descripcion", c.getDescripcion()));
            post(BASE + "/categorias", body);
            return true;
        } catch (Exception e) { System.out.println("Error insertarCategoria: " + e.getMessage()); return false; }
    }

    public static boolean actualizarCategoria(Categoria c) {
        try {
            String body = GSON.toJson(Map.of("nombre", c.getNombre(), "descripcion", c.getDescripcion()));
            put(BASE + "/categorias/" + c.getId_categoria(), body);
            return true;
        } catch (Exception e) { System.out.println("Error actualizarCategoria: " + e.getMessage()); return false; }
    }

    public static boolean borrarCategoria(int id) {
        try { delete(BASE + "/categorias/" + id); return true; }
        catch (Exception e) { System.out.println("Error borrarCategoria: " + e.getMessage()); return false; }
    }

    public static ObservableList<Categoria> cargarCategorias() {
        return cargarCategoriasList(BASE + "/categorias");
    }

    public static ObservableList<Categoria> obtenerCategoriasPorEjercicio(int idEjer) {
        return cargarCategoriasList(BASE + "/categorias/ejercicio/" + idEjer);
    }

    private static ObservableList<Categoria> cargarCategoriasList(String url) {
        ObservableList<Categoria> lista = FXCollections.observableArrayList();
        try {
            CategoriaDto[] dtos = GSON.fromJson(get(url), CategoriaDto[].class);
            if (dtos != null) for (CategoriaDto d : dtos)
                lista.add(new Categoria(d.idCategoria, d.nombre, d.descripcion));
        } catch (Exception e) { System.out.println("Error cargarCategorias: " + e.getMessage()); }
        return lista;
    }

    public static boolean asignarCategoriasAEjercicio(int idEjer, List<Categoria> categorias) {
        try {
            List<Integer> ids = new ArrayList<>();
            for (Categoria c : categorias) ids.add(c.getId_categoria());
            put(BASE + "/categorias/ejercicio/" + idEjer, GSON.toJson(ids));
            return true;
        } catch (Exception e) { System.out.println("Error asignarCategorias: " + e.getMessage()); return false; }
    }
}
