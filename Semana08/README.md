# HuellitasVet 

App móvil de una veterinaria con dos perfiles en el mismo dispositivo: el Dueño registra a sus mascotas, pide citas eligiendo servicio, fecha y hora, y consulta el historial clínico; el Veterinario ve la agenda, confirma citas, registra la atención (diagnóstico, tratamiento y peso), consulta a los pacientes y administra los servicios. Ambos perfiles comparten la misma base de datos Room: cuando el veterinario registra una atención, el dueño la ve al instante en el historial de su mascota.

## Base de datos (4 tablas)

| Tabla | Relación |
| --- | --- |
| `usuarios` | — |
| `servicios` | — |
| `mascotas` (FK → usuarios) | N mascotas → 1 dueño |
| `citas` (FK → mascotas, FK → servicios) | N citas → 1 mascota; N citas → 1 servicio |

## Estructura de paquetes

```
com.tecsup.huellitasvet
├── MainActivity.kt
├── VetApp.kt                     crea la base, el repository y las preferencias
├── data
│   ├── local
│   │   ├── entity   Servicio · Usuario · Mascota · Cita
│   │   ├── dao      ServicioDao · UsuarioDao · MascotaDao · CitaDao
│   │   ├── relation MascotaConDueno · CitaConDetalle
│   │   ├── VetDatabase.kt
│   │   └── DatosIniciales.kt
│   ├── repository   VetRepository (interface) · VetRepositoryRoom
│   └── prefs        PreferenciasStore (DataStore)
├── model            Enums · ConteoServicio · Horarios (+ Formato)
├── viewmodel        Sesion · Inicio · Mascota · PedirCita · MisCitas
│                    Panel · Agenda · AtenderCita · Pacientes · Servicios
└── ui
    ├── theme        Color · Type · Theme (VetTheme por rol)
    ├── components   AppTopBar · VetButton · VetTextField · EstadoCitaChip
    │                MascotaAvatar · CitaCard · FechaChip · HoraChip
    │                HistorialItem · OpcionChip · FilaSwitch · graficos/
    ├── navigation   Rutas · NavVet
    ├── dueno        Splash · Login · Inicio · Mascotas · FormularioMascota
    │                FichaMascota · PedirCita · MisCitas · Perfil
    └── veterinario  Panel · Agenda · AtenderCita · Pacientes
                     Servicios · FormularioServicio · Ajustes
```

## Capturas

### Acceso

| Inicio de sesión | Registro |
| --- | --- |
| ![Inicio de sesión](img/VeterinarioLogin.png) | ![Registro](img/VeterinarioRegister.png) |

### Dueño

| Inicio | Ficha de la mascota | Pedir cita |
| --- | --- | --- |
| ![Inicio dueño](img/ClienteInicio.png) | ![Ficha de la mascota](img/ClienteMascota.png) | ![Pedir cita](img/ClientePedirCita.png) |

| Mis citas | Perfil |
| --- | --- |
| ![Mis citas](img/ClienteCitas.png) | ![Perfil](img/ClientePerfil.png) |

### Veterinario

| Panel | Agenda | Atender cita |
| --- | --- | --- |
| ![Panel](img/VeterinarioPantallaInicio.png) | ![Agenda](img/VeterinarioAgenda.png) | ![Atender cita](img/VeterinarioAtenderCita.png) |

| Ajustes |
| --- |
| ![Ajustes](img/VeterinarioAjustes.png) |

### Modo oscuro

| Pantalla en modo oscuro |
| --- |
| ![Modo oscuro](img/PantallaOscuraEjemplo.png) |
