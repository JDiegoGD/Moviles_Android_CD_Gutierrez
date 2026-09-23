package com.gutierrez.tecsupfit.datos

import androidx.compose.runtime.mutableStateListOf

data class Clase(
    val id: Int,
    val nombre: String,
    val dia: String,
    val horarios: List<String>,
    val sala: String,
    val duracion: Int,
    val cuposDisponibles: Int,
    val cuposTotales: Int,
    val descripcion: String
)

enum class EstadoReserva { CONFIRMADA, COMPLETADA }

data class Reserva(
    val clase: String,
    val dia: String,
    val hora: String,
    val estado: EstadoReserva
)

data class Rutina(
    val nombre: String,
    val detalle: String
)


object DatosGym {

    val filtros = listOf("Hoy", "Esta semana")

    val clases = listOf(
        Clase(1, "Yoga funcional", "Hoy", listOf("7:00 am", "9:00 am", "6:00 pm"),
            "Sala 2", 60, 10, 15,
            "Movilidad, fuerza y respiración para empezar el día con energía."),
        Clase(2, "Cross Training", "Hoy", listOf("6:00 pm", "7:30 pm", "8:30 pm"),
            "Sala 1", 45, 8, 12,
            "Entrenamiento funcional de alta intensidad."),
        Clase(3, "Spinning", "Hoy", listOf("7:30 pm", "8:30 pm"),
            "Sala 3", 50, 5, 20,
            "Ciclismo con música para mejorar tu resistencia."),
        Clase(4, "Pilates", "Miércoles", listOf("8:00 am", "5:00 pm"),
            "Sala 2", 50, 12, 15,
            "Fortalecimiento del core, postura y flexibilidad."),
    )

    val reservas = mutableStateListOf(
        Reserva("Yoga funcional", "Ayer", "7:00 am", EstadoReserva.COMPLETADA)
    )

    val rutinas = listOf(
        Rutina("Full body principiante", "3 días por semana · 40 min"),
        Rutina("Fuerza tren superior", "Pecho, espalda y brazos · 50 min"),
        Rutina("Cardio quema grasa", "Intervalos HIIT · 30 min")
    )

    // Datos del usuario para la pantalla de Perfil
    const val nombreUsuario = "Diego Ramos"
    const val planUsuario = "Plan Premium"
    const val clasesTomadas = 14
    const val rachaSemanas = 3

    fun buscarClase(id: Int): Clase? = clases.find { it.id == id }

    // "Hoy" muestra solo las clases de hoy; "Esta semana" muestra todas
    fun filtrarClases(filtro: String): List<Clase> =
        if (filtro == "Hoy") clases.filter { it.dia == "Hoy" } else clases

    fun reservar(clase: Clase, hora: String) {
        reservas.add(0, Reserva(clase.nombre, clase.dia, hora, EstadoReserva.CONFIRMADA))
    }
}