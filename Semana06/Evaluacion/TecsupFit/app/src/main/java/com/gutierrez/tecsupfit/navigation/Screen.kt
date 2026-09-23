package com.gutierrez.tecsupfit.navigation

sealed class Screen(val route: String) {

    // Pestañas del bottomBar
    object Inicio : Screen("inicio")
    object Reservas : Screen("reservas")
    object Rutinas : Screen("rutinas")
    object Perfil : Screen("perfil")

    // Detalle de la clase elegida
    object Detalle : Screen("detalle/{claseId}") {
        fun createRoute(claseId: Int): String = "detalle/$claseId"
    }

    // Confirmación: recibe la clase y el horario elegido (su posición en la lista)
    object Confirmacion : Screen("confirmacion/{claseId}/{horario}") {
        fun createRoute(claseId: Int, horario: Int): String =
            "confirmacion/$claseId/$horario"
    }
}