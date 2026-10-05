package com.gutierrez.citas.ui.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.gutierrez.citas.navigation.Rutas
import com.gutierrez.citas.ui.theme.AzulClinica
import com.gutierrez.citas.ui.theme.AzulNiebla
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


@Composable
fun BarraInferior(
    rutaActual: String,
    onNavegar: (String) -> Unit
) {
    NavigationBar(containerColor = Color.White) {
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
                    indicatorColor = AzulNiebla,
                    unselectedIconColor = GrisMedio,
                    unselectedTextColor = GrisMedio
                )
            )
        }
    }
}