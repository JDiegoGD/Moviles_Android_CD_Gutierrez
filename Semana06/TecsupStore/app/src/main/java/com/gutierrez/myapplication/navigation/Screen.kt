package com.gutierrez.myapplication.navigation

sealed class Screen(val route: String) {

    // Pantalla de inicio - punto de entrada de la app tras autenticación
    object Home : Screen("home")

    // Pantalla que muestra la lista de elementos (Directorio de Alumnos)
    object List : Screen("list")

    object Detail : Screen("Detail")
}