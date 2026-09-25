package com.gutierrez.clinicasalud.navigation

sealed class Screen(val route: String) {

    // Pantalla de lista de médicos
    object Home : Screen("home")

    // Destinos del menú lateral
    object MisCitas : Screen("mis_citas")
    object Historial : Screen("historial")
    object Perfil : Screen("perfil")

    // Perfil del médico
    object Detail : Screen("detail/{medicoId}") {
        // Ejemplo: createRoute(3) → "detail/3"
        fun createRoute(medicoId: Int): String = "detail/$medicoId"
    }

    // Agendar cita
    object Agendar : Screen("agendar/{medicoId}") {
        fun createRoute(medicoId: Int): String = "agendar/$medicoId"
    }

    // Confirmación
    object Confirmacion : Screen("confirmacion/{medicoId}/{fecha}/{hora}") {
        fun createRoute(medicoId: Int, fecha: Int, hora: Int): String =
            "confirmacion/$medicoId/$fecha/$hora"
    }
}