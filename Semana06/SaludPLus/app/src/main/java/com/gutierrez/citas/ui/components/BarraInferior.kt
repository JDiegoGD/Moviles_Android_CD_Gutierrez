package com.gutierrez.citas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.gutierrez.citas.navigation.Rutas
import com.gutierrez.citas.ui.theme.AzulClinica
import com.gutierrez.citas.ui.theme.GrisMedio

private data class DestinoBarra(
    val ruta: String,
    val etiqueta: String,
    val icono: ImageVector
)

private val destinosBarra = listOf(
    DestinoBarra(Rutas.HOME, "Inicio", Icons.Filled.Home),
    DestinoBarra(Rutas.MIS_CITAS, "Citas", Icons.Filled.CalendarMonth),
    DestinoBarra(Rutas.RESULTADOS, "Resultados", Icons.Filled.Description),
    DestinoBarra(Rutas.PERFIL, "Perfil", Icons.Filled.Person)
)

/**
 * Menú principal de la app. [rutaActual] marca la pestaña seleccionada y
 * [onNavegar] recibe la ruta de la pestaña tocada. Lleva una línea fina arriba
 * para separarla del contenido y sin la "píldora" de fondo en la pestaña activa.
 */
@Composable
fun BarraInferior(
    rutaActual: String,
    onNavegar: (String) -> Unit
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFFE6EBF3))
        )
        NavigationBar(
            containerColor = Color(0xFFFAFBFE),
            tonalElevation = 0.dp
        ) {
            destinosBarra.forEach { destino ->
                NavigationBarItem(
                    selected = destino.ruta == rutaActual,
                    onClick = {
                        if (destino.ruta != rutaActual) onNavegar(destino.ruta)
                    },
                    icon = { Icon(imageVector = destino.icono, contentDescription = destino.etiqueta) },
                    label = { Text(destino.etiqueta) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AzulClinica,
                        selectedTextColor = AzulClinica,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = GrisMedio,
                        unselectedTextColor = GrisMedio
                    )
                )
            }
        }
    }
}
