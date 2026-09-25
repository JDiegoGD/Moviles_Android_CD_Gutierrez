package com.gutierrez.navLab.navigation

sealed class Screen(val route: String) {

    // Pantalla de inicio - punto de entrada de la app tras autenticación
    object Home : Screen("home")

    // Pantalla que muestra la lista de elementos (Directorio de Alumnos)
    object List : Screen("list")

    // Pantalla de perfil de usuario (Configuración de Perfil)
    object Profile : Screen("profile")

    // Pantalla de detalle de un elemento (Expediente Académico)
    object Detail : Screen("detail/{itemId}") {
        fun createRoute(itemId: Int) = "detail/$itemId"
    }
}
