package com.gutierrez.tecsupfit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gutierrez.tecsupfit.screens.*

@Composable
fun AppNavigation() {
    // Crea y recuerda el controlador que maneja el back stack
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Inicio.route
    ) {
        composable(Screen.Inicio.route) {
            InicioScreen(navController)
        }
        composable(Screen.Reservas.route) {
            ReservasScreen(navController)
        }
        composable(Screen.Rutinas.route) {
            RutinasScreen(navController)
        }
        composable(Screen.Perfil.route) {
            PerfilScreen(navController)
        }

        composable(
            route = Screen.Detalle.route,
            arguments = listOf(
                navArgument("claseId") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val claseId = backStackEntry.arguments?.getInt("claseId") ?: 0
            DetalleClaseScreen(navController, claseId)
        }

        composable(
            route = Screen.Confirmacion.route,
            arguments = listOf(
                navArgument("claseId") { type = NavType.IntType; defaultValue = 0 },
                navArgument("horario") { type = NavType.IntType; defaultValue = 0 }
            )
        ) { backStackEntry ->
            val claseId = backStackEntry.arguments?.getInt("claseId") ?: 0
            val horario = backStackEntry.arguments?.getInt("horario") ?: 0
            ConfirmacionScreen(navController, claseId, horario)
        }
    }
}