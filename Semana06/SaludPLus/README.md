# Clínica SaludPlus · App Paciente
###### Gutierrez Duran Juan Diego Gilmer
App Android hecha con Kotlin y Jetpack Compose (Material 3) para agendar citas médicas en la Clínica SaludPlus.  
El paciente se registra, elige especialidad, médico, fecha y hora, y gestiona sus citas, resultados y datos desde un menú inferior.  
No usa base de datos: usuarios, médicos y citas viven en colecciones dentro del objeto `Repositorio` y se pierden al cerrar la app.

## Objetivos

* Completar una app real de agendamiento de citas médicas a partir de un código esqueleto, llenando cada archivo .kt según sus comentarios TODO.
* Implementar NavigationBar como menú principal de navegación en la pantalla de Inicio (Inicio, Citas, Resultados, Perfil).
* Aplicar LazyRow (especialidades destacadas), LazyColumn (especialidades, médicos y citas) y LazyVerticalGrid (horarios) trabajando con colecciones en memoria.
* Aplicar navegación con paso de parámetros (especialidadId, medicoId, fecha, hora) y popUpTo.
* Diseñar las vistas que faltan en el diseño de referencia respetando su estilo visual.
* Aplicar control de versiones con GitHub en dos fases: desarrollo propio y mejora asistida por IA.

## Esqueleto del proyecto

```text
com.gutierrez.citas
├── MainActivity.kt
├── data
│   ├── model
│   │   ├── Usuario.kt
│   │   ├── Especialidad.kt
│   │   ├── Medico.kt
│   │   ├── Cita.kt
│   │   └── Resultado.kt
│   └── repository
│       └── Repositorio.kt
├── navigation
│   ├── Rutas.kt
│   └── AppNavigation.kt
├── util
│   └── FechaTexto.kt
└── ui
    ├── theme
    │   ├── Color.kt
    │   ├── Theme.kt
    │   └── Type.kt
    ├── components
    │   ├── BannerDegradado.kt
    │   ├── BarraInferior.kt
    │   ├── BarraSuperior.kt
    │   ├── BotonPrimario.kt
    │   ├── CampoConIcono.kt
    │   ├── Componentes.kt
    │   ├── EnlaceTexto.kt
    │   ├── FilaDato.kt
    │   ├── FotoMedico.kt
    │   ├── IconoEspecialidad.kt
    │   ├── PantallaEnConstruccion.kt
    │   ├── TarjetaBase.kt
    │   └── TarjetaMedico.kt
    └── screens
        ├── auth
        │   ├── SplashScreen.kt
        │   ├── RegistroScreen.kt
        │   ├── LoginScreen.kt
        │   └── TerminosScreen.kt
        ├── home
        │   └── HomeScreen.kt
        ├── agendamiento
        │   ├── EspecialidadesScreen.kt
        │   ├── MedicosScreen.kt
        │   ├── FechaHoraScreen.kt
        │   ├── ConfirmarCitaScreen.kt
        │   └── CitaExitosaScreen.kt
        ├── citas
        │   ├── MisCitasScreen.kt
        │   └── DetalleCitaScreen.kt
        ├── perfil
        │   └── PerfilScreen.kt
        ├── resultados
        │   └── ResultadosScreen.kt
        └── notificaciones
            └── NotificacionesScreen.kt
```

## Capturas

| Splash                                                | Registro                                                   | Iniciar sesión                                                     |
|-------------------------------------------------------|------------------------------------------------------------|--------------------------------------------------------------------|
| <img src="img/Captura1.png" width="200" alt="Splash"> | <img src="img/CrearCuenta.png" width="200" alt="Registro"> | <img src="img/IniciarSesion.png" width="200" alt="Iniciar sesión"> |

| Inicio                                                      | Especialidades                                                    | Médicos                                              |
|-------------------------------------------------------------|-------------------------------------------------------------------|------------------------------------------------------|
| <img src="img/VistaPrincipal.png" width="200" alt="Inicio"> | <img src="img/Especialidad.png" width="200" alt="Especialidades"> | <img src="img/Medico.png" width="200" alt="Médicos"> |

| Fecha y hora                                                 | Confirmar cita                                                     | Cita agendada                                                     |
|--------------------------------------------------------------|--------------------------------------------------------------------|-------------------------------------------------------------------|
| <img src="img/FechaHora.png" width="200" alt="Fecha y hora"> | <img src="img/ConfirmarCita.png" width="200" alt="Confirmar cita"> | <img src="img/CitaAngendada.png" width="200" alt="Cita agendada"> |

| Mis citas                                                          | Mis citas (sin citas)                                                          | Detalle de cita                                                   |
|--------------------------------------------------------------------|--------------------------------------------------------------------------------|-------------------------------------------------------------------|
| <img src="img/MisCitas(Concitas).png" width="200" alt="Mis citas"> | <img src="img/MisCitas(SinCitas).png" width="200" alt="Mis citas (sin citas)"> | <img src="img/DetalleCita.png" width="200" alt="Detalle de cita"> |

| Cancelar cita (diálogo)                                                    | Perfil / Mis datos                                                     | Resultados                                                   |
|----------------------------------------------------------------------------|------------------------------------------------------------------------|--------------------------------------------------------------|
| <img src="img/CancelarCita.png" width="200" alt="Cancelar cita (diálogo)"> | <img src="img/DetalleCuenta.png" width="200" alt="Perfil / Mis datos"> | <img src="img/DetalleCita.png" width="200" alt="Resultados"> |

| Notificaciones                                                      | Notificaciones (vacío)                                                            | Términos y condiciones                                                           |
|---------------------------------------------------------------------|-----------------------------------------------------------------------------------|----------------------------------------------------------------------------------|
| <img src="img/Notificaciones.png" width="200" alt="Notificaciones"> | <img src="img/Notificaciones(Vacia).png" width="200" alt="Notificaciones (vacío)"> | <img src="img/TerminosCondiciones.png" width="200" alt="Términos y condiciones"> |
