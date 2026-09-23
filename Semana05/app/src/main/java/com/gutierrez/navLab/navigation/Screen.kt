package com.gutierrez.navLab.navigation

sealed class Screen(val route: String) {
    // Pantalla de Login
    object Login : Screen("login")

    //Pantalla de inicio - punto de entrada de la app
    object Home : Screen("home")

    //Pantalla que muestra la lista de elementos
    object List : Screen("list")

    //Pantalla de perfil de usuario
    object Profile : Screen("profile")

    //Pantalla de detalle de un elemento - con argumento {itemId}
    object Detail : Screen("detail/{itemId}"){
        //Construye la ruta final con el argumento.
        fun createRoute(itemId: Int) = "detail/$itemId"
    }
}