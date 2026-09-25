package com.gutierrez.clinicasalud.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gutierrez.clinicasalud.screens.*
import kotlinx.coroutines.launch

// Opción del menú lateral: ruta, texto e ícono
data class OpcionMenu(val ruta: String, val titulo: String, val icono: ImageVector)

val opcionesMenu = listOf(
    OpcionMenu(Screen.Home.route, "Inicio", Icons.Filled.Home),
    OpcionMenu(Screen.MisCitas.route, "Mis citas", Icons.Filled.DateRange),
    OpcionMenu(Screen.Historial.route, "Historial médico", Icons.AutoMirrored.Filled.List),
    OpcionMenu(Screen.Perfil.route, "Perfil", Icons.Filled.Person)
)

@Composable
fun AppNavigation() {
    // Crea y recuerda el controlador de navegación
    val navController = rememberNavController()

    // Estado del drawer (abierto / cerrado) y scope para abrirlo o cerrarlo
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Ruta actual: sirve para resaltar la opción seleccionada en el menú
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    // Función que reciben las pantallas para abrir el menú con el ícono ☰
    val abrirMenu: () -> Unit = { scope.launch { drawerState.open() } }

    // ── Navegación secundaria: menú lateral que envuelve a todo el NavHost ──
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MenuLateral(rutaActual = rutaActual) { ruta ->
                scope.launch { drawerState.close() }
                navegarDesdeMenu(navController, ruta)
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ) {
            // ── Rutas simples (destinos del menú) ──
            composable(Screen.Home.route) {
                HomeScreen(navController, onMenuClick = abrirMenu)
            }
            composable(Screen.MisCitas.route) {
                MisCitasScreen(navController, onMenuClick = abrirMenu)
            }
            composable(Screen.Historial.route) {
                HistorialScreen(navController, onMenuClick = abrirMenu)
            }
            composable(Screen.Perfil.route) {
                PerfilScreen(navController, onMenuClick = abrirMenu)
            }

            // ── Rutas con argumento (flujo secuencial) ──
            composable(
                route = Screen.Detail.route,
                arguments = listOf(
                    navArgument("medicoId") {
                        type = NavType.IntType
                        defaultValue = 0
                    }
                )
            ) { backStackEntry ->
                val medicoId = backStackEntry.arguments?.getInt("medicoId") ?: 0
                DetailScreen(navController, medicoId)
            }

            composable(
                route = Screen.Agendar.route,
                arguments = listOf(
                    navArgument("medicoId") {
                        type = NavType.IntType
                        defaultValue = 0
                    }
                )
            ) { backStackEntry ->
                val medicoId = backStackEntry.arguments?.getInt("medicoId") ?: 0
                AgendarScreen(navController, medicoId)
            }

            composable(
                route = Screen.Confirmacion.route,
                arguments = listOf(
                    navArgument("medicoId") { type = NavType.IntType; defaultValue = 0 },
                    navArgument("fecha") { type = NavType.IntType; defaultValue = 0 },
                    navArgument("hora") { type = NavType.IntType; defaultValue = 0 }
                )
            ) { backStackEntry ->
                val medicoId = backStackEntry.arguments?.getInt("medicoId") ?: 0
                val fecha = backStackEntry.arguments?.getInt("fecha") ?: 0
                val hora = backStackEntry.arguments?.getInt("hora") ?: 0
                ConfirmacionScreen(navController, medicoId, fecha, hora)
            }
        }
    }
}

// Contenido del menú lateral: cabecera del paciente + opciones
@Composable
fun MenuLateral(rutaActual: String?, onOpcionClick: (String) -> Unit) {
    ModalDrawerSheet {
        // Cabecera: avatar con iniciales, nombre y rol
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text("JP", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Juan Pérez", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Paciente", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(12.dp))

        // Opciones del menú (se resalta la pantalla actual)
        opcionesMenu.forEach { opcion ->
            NavigationDrawerItem(
                icon = { Icon(opcion.icono, contentDescription = null) },
                label = { Text(opcion.titulo) },
                selected = rutaActual == opcion.ruta,
                onClick = { onOpcionClick(opcion.ruta) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}

// Navega desde el menú dejando siempre Inicio como base de la pila,
// así "atrás" regresa a Inicio y no se apilan pantallas repetidas
fun navegarDesdeMenu(navController: NavController, ruta: String) {
    if (ruta == Screen.Home.route) {
        navController.popBackStack(Screen.Home.route, inclusive = false)
    } else {
        navController.navigate(ruta) {
            popUpTo(Screen.Home.route)
            launchSingleTop = true
        }
    }
}