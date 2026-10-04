package com.gutierrez.citas.ui.screens.perfil

import androidx.compose.runtime.Composable
import com.gutierrez.citas.navigation.Rutas
import com.gutierrez.citas.ui.components.PantallaEnConstruccion

// Pantalla 11 · Perfil / Mis datos (no viene en el diseño: hay que dibujarla con el mismo estilo)
// TODO: P11-1  Mostrar los datos de Repositorio.usuarioActual: nombre, teléfono y correo.
// TODO: P11-2  Mostrar cuántas citas tiene (Repositorio.citasDelUsuario().size).
// TODO: P11-3  Botón "Cerrar sesión": Repositorio.cerrarSesion() y luego onCerrarSesion().
// TODO: P11-4  Barra inferior (NavigationBar) con la pestaña "Perfil" marcada; usa onNavegar(ruta).
// TODO: P11-5  Borrar la llamada a PantallaEnConstruccion.
@Composable
fun PerfilScreen(
    onNavegar: (String) -> Unit,
    onCerrarSesion: () -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Perfil",
        acciones = listOf(
            "Cerrar sesión" to onCerrarSesion,
            "Barra inferior: Inicio" to { onNavegar(Rutas.HOME) }
        )
    )
}
