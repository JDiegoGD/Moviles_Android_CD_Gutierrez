package com.gutierrez.tecsupfit.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

// Pestaña del bottomBar: ruta, texto e ícono
data class Pestana(val ruta: String, val titulo: String, val icono: ImageVector)

val pestanas = listOf(
    Pestana(Screen.Inicio.route, "Inicio", Icons.Filled.Home),
    Pestana(Screen.Reservas.route, "Reservas", Icons.Filled.DateRange),
    Pestana(Screen.Rutinas.route, "Rutinas", Icons.AutoMirrored.Filled.List),
    Pestana(Screen.Perfil.route, "Perfil", Icons.Filled.Person)
)

@Composable
fun BarraInferior(navController: NavController) {

    // Ruta actual: sirve para resaltar la pestaña de la pantalla en la que estamos
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    NavigationBar(containerColor = Color.White) {
        pestanas.forEach { pestana ->
            val seleccionada = rutaActual == pestana.ruta

            NavigationBarItem(
                selected = seleccionada,
                onClick = {
                    if (!seleccionada) {
                        navController.navigate(pestana.ruta) {
                            // Vuelve a la pantalla inicial de la pila para no apilar pestañas
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true   // evita abrir dos veces la misma pestaña
                            restoreState = true      // recupera el estado al volver a una pestaña
                        }
                    }
                },
                icon = { Icon(pestana.icono, contentDescription = pestana.titulo) },
                label = {
                    Text(
                        text = pestana.titulo,
                        fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = Color(0xFF757575),
                    unselectedTextColor = Color(0xFF757575)
                )
            )
        }
    }
}