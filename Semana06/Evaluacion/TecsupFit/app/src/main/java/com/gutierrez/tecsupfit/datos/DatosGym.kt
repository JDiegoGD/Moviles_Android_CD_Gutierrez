package com.gutierrez.tecsupfit.datos

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf

// ───── Modelos ─────

data class Clase(
    val id: Int,
    val nombre: String,
    val dia: String,              // "Hoy" o el día de la semana
    val horarios: List<String>,   // horarios disponibles (el primero es el principal)
    val sala: String,
    val duracion: Int,            // en minutos
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
            "Entrenamiento funcional de alta intensidad. Cupos limitados."),
        Clase(3, "Spinning", "Hoy", listOf("7:30 pm", "8:30 pm"),
            "Sala 3", 50, 5, 20,
            "Ciclismo indoor con música para mejorar tu resistencia cardiovascular."),
        Clase(4, "Pilates", "Miércoles", listOf("8:00 am", "5:00 pm"),
            "Sala 2", 50, 12, 15,
            "Fortalecimiento del core, postura y flexibilidad."),
        Clase(5, "Boxeo", "Hoy", listOf("5:00 pm", "6:00 pm"),
            "Sala 4", 45, 1, 10,
            "Técnicas de boxeo y acondicionamiento físico."),
        Clase(6, "Zumba", "Jueves", listOf("7:00 pm", "8:00 pm"),
            "Sala 1", 50, 15, 20,
            "Baile y cardio ritmos latinos.")
    )

    // Estado observable con los cupos restantes de cada clase por ID
    val cupos = mutableStateMapOf<Int, Int>().apply {
        clases.forEach { put(it.id, it.cuposDisponibles) }
    }

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

    fun filtrarClases(filtro: String): List<Clase> =
        if (filtro == "Hoy") clases.filter { it.dia == "Hoy" } else clases

    // Devuelve los cupos restantes de una clase por su ID (0 si no existe)
    fun cuposRestantes(claseId: Int): Int = cupos[claseId] ?: 0

    // Devuelve true si la clase no tiene cupos disponibles
    fun estaLlena(claseId: Int): Boolean = cuposRestantes(claseId) <= 0

    // Devuelve true si ya existe una reserva CONFIRMADA con el mismo nombre de clase y la misma hora
    fun yaReservada(clase: Clase, hora: String): Boolean {
        return reservas.any { it.clase == clase.nombre && it.hora == hora && it.estado == EstadoReserva.CONFIRMADA }
    }

    // Realiza la reserva descontando cupo si hay disponibilidad y no ha sido reservada en esa hora; retorna Boolean
    fun reservar(clase: Clase, hora: String): Boolean {
        if (estaLlena(clase.id) || yaReservada(clase, hora)) {
            return false
        }
        val cuposActuales = cuposRestantes(clase.id)
        cupos[clase.id] = cuposActuales - 1
        reservas.add(0, Reserva(clase.nombre, clase.dia, hora, EstadoReserva.CONFIRMADA))
        return true
    }
}