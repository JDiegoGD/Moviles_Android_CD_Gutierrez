package com.gutierrez.myapplication.navigation

sealed class Screen(val route: String) {

    // Pantalla de inicio
    object Home : Screen("home")

    object Pedidos : Screen("pedidos")

    object Favoritos : Screen("favoritos")

    object Perfil : Screen("profile")

    object Close : Screen("Close")
}