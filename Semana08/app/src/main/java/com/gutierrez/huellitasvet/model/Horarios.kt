package com.gutierrez.huellitasvet.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Locale

/** Genera los 18 horarios del dia: 09:00, 09:30 ... 17:30. */
object Horarios {
    val todos: List<String> = (0 until 18).map { i ->
        val minutos = 9 * 60 + i * 30
        String.format(Locale.US, "%02d:%02d", minutos / 60, minutos % 60)
    }
}

/** Funciones de formato usadas por varias pantallas. */
object Formato {
    private val dias = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
    private val meses = listOf(
        "ene", "feb", "mar", "abr", "may", "jun",
        "jul", "ago", "sep", "oct", "nov", "dic"
    )

    fun diaSemana(f: LocalDate): String = dias[f.dayOfWeek.value - 1]

    fun fechaCorta(fecha: String): String {
        val d = LocalDate.parse(fecha)
        return "${diaSemana(d)} ${d.dayOfMonth} ${meses[d.monthValue - 1]}"
    }

    fun fechaDiaMes(fecha: String): String {
        val d = LocalDate.parse(fecha)
        return "${d.dayOfMonth} ${meses[d.monthValue - 1]}"
    }

    fun fechaHora(fecha: String, hora: String): String = "${fechaCorta(fecha)} · $hora"

    fun soles(monto: Double): String = "S/ " + String.format(Locale.US, "%.2f", monto)

    fun peso(kg: Double): String = String.format(Locale.US, "%.1f kg", kg)

    fun edad(anioNacimiento: Int): Int = LocalDate.now().year - anioNacimiento

    fun fechaHoraDe(fecha: String, hora: String): LocalDateTime =
        LocalDateTime.of(LocalDate.parse(fecha), LocalTime.parse(hora))
}
