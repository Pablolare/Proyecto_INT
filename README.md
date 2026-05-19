# LrVolley — Trabajo Fin de Grado

Aplicación de gestión de entrenamientos de voleibol compuesta por tres capas: API REST (Spring Boot), aplicación de escritorio (JavaFX) y aplicación móvil (React Native + Expo).

---

## Índice

1. [Descripción general](#descripción-general)
2. [Arquitectura](#arquitectura)
3. [Stack tecnológico](#stack-tecnológico)
4. [Estructura del repositorio](#estructura-del-repositorio)
5. [Modelos de datos](#modelos-de-datos)
6. [API REST — Endpoints principales](#api-rest--endpoints-principales)
7. [Aplicación de escritorio (JavaFX)](#aplicación-de-escritorio-javafx)
8. [Aplicación móvil (React Native)](#aplicación-móvil-react-native)
9. [Configuración y puesta en marcha](#configuración-y-puesta-en-marcha)
10. [Usuarios de prueba](#usuarios-de-prueba)

---

## Descripción general

LrVolley permite a entrenadores y jugadores de voleibol gestionar sus sesiones de entrenamiento. Las funcionalidades principales son:

- Crear y editar entrenamientos con ejercicios, descripción y estadísticas de aciertos/fallos.
- Ejecutar entrenamientos en tiempo real desde el móvil con temporizador.
- Consultar el historial de entrenamientos completados con gráfico de progresión.
- Gestión de usuarios con roles **jugador** y **administrador**.
- Generación de informes PDF/HTML con JasperReports.
- Gestión de ejercicios por categorías y finalidad.
- Sistema de notas y objetivos.

---

## Arquitectura

```
┌─────────────────────────────────────────┐
│          TiDB Cloud (MySQL)             │
│  gateway01.eu-central-1.prod.aws...     │
│  Base de datos: LrVoley                 │
└────────────────────┬────────────────────┘
                     │ JDBC
┌────────────────────▼────────────────────┐
│      API REST — Spring Boot             │
│      Puerto: 8080  /api/*               │
│      ddl-auto: none                     │
└───────┬──────────────────────┬──────────┘
        │ HTTP/JSON            │ HTTP/JSON
┌───────▼────────┐    ┌────────▼────────┐
│  Desktop       │    │  Mobile         │
│  JavaFX + Gradle│   │  React Native   │
│  Java 24       │    │  Expo Router    │
└────────────────┘    └─────────────────┘
```

- El desktop y el móvil **nunca acceden directamente a TiDB**; toda la comunicación pasa por la API REST.
- La API usa Spring Data JPA con `ddl-auto=none` — el schema de la base de datos se gestiona manualmente con SQL.
- Los informes JasperReports (desktop) tienen una conexión JDBC directa a TiDB para la ejecución del SQL del informe.

---

## Stack tecnológico

| Capa | Tecnología |
|---|---|
| Base de datos | TiDB Cloud (compatible MySQL 8) |
| API | Spring Boot 3, Spring Data JPA, Jakarta Persistence |
| Desktop | JavaFX 25, Gradle, Java 24, JasperReports 7, Gson |
| Móvil | React Native, Expo Router, NativeWind (Tailwind), Zustand, Axios, TypeScript |

---

## Estructura del repositorio

```
TFG/
├── api/                          # Spring Boot REST API
│   └── src/main/java/com/lrvoley/api/
│       ├── controller/           # @RestController por entidad
│       ├── model/                # Entidades JPA
│       ├── repository/           # JpaRepository + queries nativas
│       └── dto/                  # DTOs para respuestas agregadas
│
├── desktop/                      # Aplicación JavaFX
│   └── src/main/
│       ├── java/com/javafx/ProyectoINT/
│       │   ├── modelos/          # POJO + DAO (llaman a ApiCliente)
│       │   ├── dto/              # DTOs Gson para deserializar JSON
│       │   ├── ApiCliente.java   # Capa HTTP (java.net.http)
│       │   ├── ConexionBD.java   # Conexión JDBC directa (solo informes)
│       │   └── <Modulo>/         # Controller + FXML por pantalla
│       └── resources/
│           ├── FXML_Proyecto/    # Archivos FXML de cada pantalla
│           ├── Informes/         # Archivos .jasper y .jrxml
│           ├── styles.css        # Tema oscuro compartido
│           └── database.properties
│
└── mobile/LrVoleyMovile/         # Aplicación React Native
    ├── app/
    │   ├── _layout.tsx           # Stack root + PaperProvider
    │   ├── login.tsx
    │   ├── error.tsx
    │   ├── (tabs)/               # Bottom tabs
    │   │   ├── index.tsx         # Dashboard
    │   │   ├── entrenamientos.tsx
    │   │   ├── historial.tsx
    │   │   └── perfil.tsx
    │   └── ejecutar/[nombre].tsx # Ejecución de entrenamiento
    ├── components/
    │   ├── EjercicioCard.tsx
    │   └── GrupoEntrenoCard.tsx
    ├── store/                    # Zustand stores
    ├── helpers/api.ts            # Axios + funciones de API
    ├── model/Types.ts            # Tipos TypeScript
    └── nav/Navegacion.ts         # Helper de navegación
```

---

## Modelos de datos

### Tablas en TiDB

#### Usuario
| Campo | Tipo | Descripción |
|---|---|---|
| id_usuario | INT PK AUTO | Identificador |
| nombre | VARCHAR | Nombre |
| apellido | VARCHAR | Apellidos |
| login | VARCHAR | Login único |
| contrasena | VARCHAR | Contraseña |
| rol | VARCHAR | `jugador` o `administrador` |
| correo | VARCHAR | Correo electrónico |

#### Ejercicios
| Campo | Tipo | Descripción |
|---|---|---|
| id_ejer | INT PK AUTO | Identificador |
| nombre_ejer | VARCHAR | Nombre del ejercicio |
| tipo | VARCHAR | Tipo (técnico, físico, etc.) |
| finalidad | TEXT | Descripción de la finalidad |

#### Categorias
| Campo | Tipo |
|---|---|
| idCategoria | INT PK AUTO |
| nombre | VARCHAR |
| descripcion | TEXT |

#### EjercicioCategoria (tabla intermedia)
| Campo | Tipo |
|---|---|
| id_ejer | INT FK |
| idCategoria | INT FK |

#### Entrenamientos
| Campo | Tipo | Descripción |
|---|---|---|
| id_entreno | INT PK AUTO | Identificador de la fila |
| id_usuario | INT FK | Usuario propietario |
| id_ejer | INT FK | Ejercicio de esta fila |
| nombre_entreno | VARCHAR | Nombre del grupo de entrenamiento |
| descripcion | TEXT | Descripción del entrenamiento |
| fallos | INT | Fallos registrados |
| aciertos | INT | Aciertos registrados |
| completado | BOOLEAN | Si el ejercicio fue completado |

> Un "entrenamiento" en la UI es un grupo de filas con el mismo `nombre_entreno` e `id_usuario`. Cada fila corresponde a un ejercicio diferente dentro de ese grupo.

---

## API REST — Endpoints principales

Base URL: `http://localhost:8080/api`

### Usuarios
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/usuarios/login` | Login con `{login, contrasena}` |
| GET | `/usuarios/{id}` | Obtener usuario por ID |
| POST | `/usuarios` | Crear usuario |
| PUT | `/usuarios/{id}` | Actualizar usuario |
| DELETE | `/usuarios/{id}` | Eliminar usuario |

### Entrenamientos
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/entrenamientos/usuario/{id}` | Todos los entrenamientos del usuario |
| GET | `/entrenamientos/porNombre?nombre=&idUsuario=` | Filas individuales por nombre |
| GET | `/entrenamientos/agrupados/{id}` | Agrupados por `nombre_entreno` |
| GET | `/entrenamientos/progresion/{id}` | Datos para gráfico de progresión |
| GET | `/entrenamientos/estadisticas/{id}` | Totales y porcentajes |
| POST | `/entrenamientos` | Crear fila |
| PUT | `/entrenamientos/{id}` | Actualizar fila (no sobreescribe `descripcion` si viene null) |
| DELETE | `/entrenamientos/porNombre?nombre=&idUsuario=` | Borrar grupo completo |

### Ejercicios
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/ejercicios` | Todos los ejercicios |
| POST | `/ejercicios` | Crear ejercicio |
| PUT | `/ejercicios/{id}` | Actualizar ejercicio |
| DELETE | `/ejercicios/{id}` | Eliminar ejercicio |

### Categorías
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/categorias` | Todas las categorías |
| GET | `/categorias/ejercicio/{idEjer}` | Categorías de un ejercicio |
| POST | `/categorias` | Crear categoría |
| PUT | `/categorias/{id}` | Actualizar categoría |

---

## Aplicación de escritorio (JavaFX)

### Pantallas

| Pantalla | Descripción |
|---|---|
| InicioSesion | Login con usuario/contraseña |
| Registrar | Registro con selector de rol (jugador / administrador) |
| PaginaPrincipal | Vista principal con sidebar de navegación |
| Entrenos | Lista de entrenamientos agrupados; crear, editar, borrar |
| PagAñadirEntreno | Formulario para añadir ejercicios a un entrenamiento |
| EditarEntreno | Editar nombre y descripción de un entrenamiento |
| Historial | Historial de completados + estadísticas + gráfico de progresión |
| Perfil | Datos del usuario + tabla de todos los usuarios (admin) |
| Informes | Generación de informes PDF/HTML con JasperReports |
| ListarEjercicios | Ver ejercicios de un entrenamiento |
| EditarEjercicios | Gestión del catálogo de ejercicios |
| CrearEjercicio | Crear nuevo ejercicio con categorías |
| Notas | Gestión de notas |
| Objetivos | Gestión de objetivos |

### Patrón de comunicación con la API

`ControladorXxx` → `XxxDAO` → `ApiCliente` → API REST → TiDB

- `ApiCliente.java` centraliza toda la lógica HTTP usando `java.net.http.HttpClient`.
- Los DTOs Gson se usan solo para la deserialización; el modelo del desktop usa sus propios POJOs.
- La conexión directa JDBC (`ConexionBD.java`) solo se usa para JasperReports.

### Informes JasperReports

| Informe | Fichero | Lenguaje |
|---|---|---|
| Informe Usuarios | LrVolley-Usuario.jasper | Java |
| Informe Gráficas | LrVolley-Grafica.jrxml | Java (compilado en runtime) |
| Informe Entrenamientos | LrVolley-Entrenamientos.jasper | Java |

> El informe de gráficas se compila desde el `.jrxml` en tiempo de ejecución porque la versión precompilada usaba `language="javascript"`, incompatible con JasperReports 7 sin el módulo Rhino.

---

## Aplicación móvil (React Native)

### Pantallas / rutas

| Ruta | Descripción |
|---|---|
| `/login` | Login |
| `/(tabs)/index` | Dashboard con estadísticas |
| `/(tabs)/entrenamientos` | Lista de grupos de entrenamiento |
| `/(tabs)/historial` | Historial de completados |
| `/(tabs)/perfil` | Perfil y logout |
| `/ejecutar/[nombre]` | Ejecución con temporizador, aciertos/fallos por ejercicio |
| `/error` | Pantalla de error con mensaje descriptivo |

### Stores Zustand

| Store | Estado |
|---|---|
| `usuarioStore` | Usuario logueado (sesión) |
| `ListaEntrenamientosStore` | Lista completa cargada de la API |
| `EntrenamientoSeleccionadoStore` | Grupo seleccionado para ejecutar |

### Tipos principales (`model/Types.ts`)

```ts
Entrenamiento       // Fila individual de la API
EntrenamientoEnriquecido  // + nombreEjer, tipoEjer, finalidadEjer, categoriasEjer
EntrenoLocal        // + aciertosLocal, fallosLocal, completadoLocal (durante ejecución)
GrupoEntreno        // Agrupación por nombreEntreno para la UI
```

### Dirección de la API según plataforma

```ts
const IP = Platform.OS === 'android' ? '10.0.2.2' : 'localhost'
const BASE_URL = `http://${IP}:8080/api`
```

---

## Configuración y puesta en marcha

### 1. Base de datos (TiDB Cloud)

La base de datos ya está desplegada en TiDB Cloud. Las credenciales están en:
- API: `api/src/main/resources/application.properties`
- Desktop (solo informes): `desktop/src/main/resources/database.properties`

### 2. API REST

```bash
cd api
./mvnw spring-boot:run
# O desde el IDE: ejecutar InicioSesionMain
```

La API arranca en `http://localhost:8080`.

### 3. Aplicación de escritorio

```bash
cd desktop
./gradlew run
```

Requiere que la API esté en marcha en el puerto 8080.

### 4. Aplicación móvil

```bash
cd mobile/LrVoleyMovile
npx expo start
```

- Android emulator: la API debe estar accesible en `10.0.2.2:8080`
- iOS / web: accesible en `localhost:8080`

---

## Usuarios de prueba

| Login | Contraseña | Rol |
|---|---|---|
| jperez | password123 | jugador |
| mgarcia | admin123 | administrador |
| clopez | user456 | jugador |

---

## Módulos del ciclo cubiertos

| Módulo | Cómo lo cubre LrVolley |
|---|---|
| **Acceso a Datos** | Spring Data JPA + repositorios JPA en la API; capa DAO en el desktop usando `java.net.http.HttpClient`; conexión JDBC directa a TiDB solo para JasperReports |
| **Sistemas de Gestión Empresarial** | Gestión de entrenamientos, sesiones y jugadores; roles `jugador` / `administrador` con distintos niveles de acceso; generación de informes PDF/HTML para toma de decisiones |
| **Desarrollo de Interfaces** | JavaFX 25 + FXML (13 pantallas, CSS tema oscuro); React Native + NativeWind (Tailwind); informes visuales con JasperReports 7 |
| **Programación de Servicios y Procesos** | API REST Spring Boot que expone 25+ endpoints HTTP/JSON; `java.net.http.HttpClient` en el desktop para consumir la API; Axios en la app móvil |
| **Programación Multimedia y Móviles** | Aplicación React Native para Android e iOS con un único código; Expo Router para navegación; temporizador en tiempo real; gráfico de progresión en el historial |

---

## Manual de instalación

### Requisitos previos

| Componente | Versión mínima |
|---|---|
| Java | 24 |
| Node.js | 18+ |
| Gradle | 8.x (incluido en el wrapper) |
| Maven | 3.9 (incluido en el wrapper) |
| Android Studio / Emulador | Cualquier versión reciente |

### Pasos

1. **Clonar el repositorio**
```bash
git clone <url-del-repo>
cd TFG
```

2. **Arrancar la API**
```bash
cd api
./mvnw spring-boot:run
```
La API arranca en `http://localhost:8080`. Las credenciales de TiDB Cloud ya están en `api/src/main/resources/application.properties`.

3. **Arrancar la app de escritorio** (requiere la API en marcha)
```bash
cd desktop
./gradlew run
```

4. **Arrancar la app móvil** (requiere la API en marcha)
```bash
cd mobile/LrVoleyMovile
npx expo start
```
- Emulador Android: la API responde en `10.0.2.2:8080`
- iOS / web: responde en `localhost:8080`

---

## Manual de usuario resumido

### App de escritorio (entrenador / administrador)

| Pantalla | Pasos principales |
|---|---|
| **Login** | Introduce login y contraseña → pulsa Entrar |
| **Registrar** | Rellena nombre, apellido, login, correo, contraseña y elige rol en el desplegable |
| **Entrenos** | Lista de entrenamientos agrupados; pulsa `+` para crear uno nuevo, lápiz para editar, papelera para borrar |
| **Añadir ejercicios** | Selecciona ejercicios del catálogo, asigna repeticiones y pulsa Añadir |
| **Editar entrenamiento** | Modifica el nombre o la descripción del entrenamiento y pulsa Guardar |
| **Historial** | Filtra por nombre de ejercicio o entrenamiento; la gráfica de progresión se actualiza automáticamente |
| **Informes** | Elige el informe (Usuarios / Gráficas / Entrenamientos), marca "Todos" o escribe un filtro, y pulsa el botón del informe |
| **Perfil** | Edita nombre, apellidos y correo → Guardar Cambios; los administradores ven la tabla de todos los usuarios |

### App móvil (jugador)

| Pantalla | Pasos principales |
|---|---|
| **Login** | Introduce login y contraseña → Iniciar sesión |
| **Inicio** | Muestra estadísticas totales (aciertos, fallos, porcentaje) |
| **Entrenamientos** | Lista de grupos; pulsa **Ejecutar** para iniciar uno |
| **Ejecutar** | El temporizador arranca automáticamente; usa `+/-` para registrar aciertos y fallos por ejercicio; marca completado; pulsa **Finalizar** para guardar |
| **Historial** | Muestra solo los entrenamientos completados con estadísticas globales |
| **Perfil** | Muestra los datos del usuario; pulsa **Cerrar sesión** para salir |

---

## Cambios relevantes del desarrollo

### Eliminación del campo `limite`
El campo `limite` fue eliminado de la entidad `Entrenamiento` en todas las capas (API, desktop, móvil). La columna sigue existiendo en TiDB pero es ignorada por la aplicación. Se puede eliminar con `ALTER TABLE Entrenamientos DROP COLUMN limite`.

### Rol en el registro
El campo de rol en el formulario de registro cambió de `TextField` libre a `ComboBox` con las opciones `jugador` y `administrador`. El valor por defecto es `jugador`.

### Información de ejercicios en la app móvil
Las tarjetas de ejercicio (`EjercicioCard`) muestran el tipo, la finalidad y las categorías del ejercicio, obtenidas mediante llamadas paralelas a `/api/categorias/ejercicio/{id}` al cargar la pantalla.

### Fix: descripción no se sobreescribe con null
El endpoint `PUT /api/entrenamientos/{id}` dejaba la descripción a `null` cuando el cliente (app móvil al finalizar un entrenamiento) no incluía el campo en el body. Corregido: solo se actualiza `descripcion` si el valor recibido no es `null`.

### Historial: columna muestra "Ejercicio (Entrenamiento)"
La columna de nombre en la tabla del historial ahora muestra el nombre del ejercicio y, entre paréntesis, el nombre del grupo de entrenamiento. Se hace un join en memoria con la lista de ejercicios cargada desde la API.

### Informes: gráfica compilada en runtime
El informe `LrVolley-Grafica` fue recompilado desde su `.jrxml` con `language="java"` en lugar de cargarse el `.jasper` precompilado (que usaba `language="javascript"`, incompatible con JasperReports 7). `ControlInformes` detecta la extensión en runtime: si es `.jrxml` llama a `JasperCompileManager.compileReport()`, si es `.jasper` usa `JRLoader`.

### Perfil: foto eliminada y tabla de usuarios ampliada
Se eliminó el bloque de foto de perfil (`ImageView` + botón "Cambiar Foto") del FXML ya que la funcionalidad no estaba implementada. La `TableView` de usuarios del sistema ahora tiene `minHeight="400"` para que sea visible sin necesidad de hacer scroll.
