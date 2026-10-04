package com.gutierrez.citas.ui.screens.home

import androidx.compose.runtime.Composable
import com.gutierrez.citas.navigation.Rutas
import com.gutierrez.citas.ui.components.PantallaEnConstruccion

// Pantalla 3 · Inicio
// TODO: P3-1  Scaffold con un NavigationBar en bottomBar con 4 destinos:
//             Inicio, Citas, Resultados y Perfil. Cada uno llama a onNavegar(ruta).
// TODO: P3-2  Encabezado con el saludo "Hola, <nombre>" (Repositorio.usuarioActual)
//             y la campana de notificaciones -> onNotificaciones()
// TODO: P3-3  Tarjetas de acceso rápido: Agendar cita -> onAgendar(), Mis citas -> onMisCitas(),
//             Mis datos -> onMisDatos(), Resultados -> onResultados()
// TODO: P3-4  LazyRow con Repositorio.especialidadesDestacadas(); al tocar una -> onEspecialidad(id)
// TODO: P3-5  Enlace "Ver todas" -> onVerEspecialidades()
// TODO: P3-6  Borrar la llamada a PantallaEnConstruccion.
@Composable
fun HomeScreen(
    onNotificaciones: () -> Unit,
    onAgendar: () -> Unit,
    onMisCitas: () -> Unit,
    onMisDatos: () -> Unit,
    onResultados: () -> Unit,
    onEspecialidad: (Int) -> Unit,
    onVerEspecialidades: () -> Unit,
    onNavegar: (String) -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Inicio",
        acciones = listOf(
            "Notificaciones" to onNotificaciones,
            "Agendar cita" to onAgendar,
            "Mis citas" to onMisCitas,
            "Mis datos" to onMisDatos,
            "Resultados" to onResultados,
            "Ver todas las especialidades" to onVerEspecialidades,
            "Abrir la especialidad 1" to { onEspecialidad(1) },
            "Barra inferior: Perfil" to { onNavegar(Rutas.PERFIL) }
        )
    )
}
