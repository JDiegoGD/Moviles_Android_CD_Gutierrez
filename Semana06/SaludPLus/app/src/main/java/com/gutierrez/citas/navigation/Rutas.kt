package com.gutierrez.citas.navigation

object Rutas {
    // Autenticación
    const val SPLASH = "splash"
    const val REGISTRO = "registro"
    const val LOGIN = "login"
    const val TERMINOS = "terminos"

    // Pantalla principal y pestañas de la barra inferior
    const val HOME = "home"
    const val MIS_CITAS = "misCitas"
    const val RESULTADOS = "resultados"
    const val PERFIL = "perfil"
    const val NOTIFICACIONES = "notificaciones"

    const val LOCALES = "locales"
    const val DOCTORES = "doctores"

    // Flujo de agendamiento
    const val ESPECIALIDADES = "especialidades"
    const val MEDICOS = "medicos/{especialidadId}"
    const val FECHA_HORA = "fechaHora/{medicoId}"
    const val CONFIRMAR_CITA = "confirmarCita/{medicoId}/{fecha}/{hora}"
    const val CITA_EXITOSA = "citaExitosa"

    // Detalle de una cita de la lista
    const val DETALLE_CITA = "detalleCita/{citaId}"

    // Arman la ruta final con el valor del parámetro ya puesto
    fun medicos(especialidadId: Int) = "medicos/$especialidadId"

    fun fechaHora(medicoId: Int) = "fechaHora/$medicoId"

    fun confirmarCita(medicoId: Int, fecha: String, hora: String) =
        "confirmarCita/$medicoId/$fecha/$hora"

    fun detalleCita(citaId: Int) = "detalleCita/$citaId"
}
