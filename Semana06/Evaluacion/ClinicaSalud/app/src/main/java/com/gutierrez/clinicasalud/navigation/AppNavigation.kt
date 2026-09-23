package com.gutierrez.clinicasalud.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gutierrez.clinicasalud.screens.*

@Composable
fun AppNavigation() {
    // Crea y recuerda el controlador de navegación
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(Screen.Home.route) {
            HomeScreen(navController, onMenuClick = {})
        }
        composable(Screen.MisCitas.route) {
            MisCitasScreen(navController, onMenuClick = {})
        }
        composable(Screen.Historial.route) {
            HistorialScreen(navController, onMenuClick = {})
        }
        composable(Screen.Perfil.route) {
            PerfilScreen(navController, onMenuClick = {})
        }

        // ── Rutas con argumento ──
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