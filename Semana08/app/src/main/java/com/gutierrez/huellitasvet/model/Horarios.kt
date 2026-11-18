package com.gutierrez.huellitasvet.model

import com.gutierrez.huellitasvet.data.local.entity.HorarioAtencion
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Locale

/** Reglas del horario de atencion: genera las horas reservables de cada dia. */
object Horarios {
    val nombresDias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")

    fun aMinutos(hora: String): Int = hora.substring(0, 2).toInt() * 60 + hora.substring(3, 5).toInt()

    fun aTexto(minutos: Int): String =
        String.format(Locale.US, "%02d:%02d", minutos / 60, minutos % 60)

    /** Busca el horario que le toca a una fecha (lunes = 1 ... domingo = 7). */
    fun delDia(lista: List<HorarioAtencion>, fecha: LocalDate): HorarioAtencion? =
        lista.firstOrNull { it.diaSemana == fecha.dayOfWeek.value }

    /** true si la clinica atiende ese dia. Mientras carga (lista vacia) no bloquea nada. */
    fun estaAbierto(lista: List<HorarioAtencion>, fecha: LocalDate): Boolean =
        lista.isEmpty() || delDia(lista, fecha)?.abierto == true

    /** Horas reservables: cada 30 min entre apertura y cierre, sin la hora de descanso. */
    fun horasDe(h: HorarioAtencion?): List<String> {
        if (h == null || !h.abierto) return emptyList()
        val horas = mutableListOf<String>()
        var m = aMinutos(h.apertura)
        val fin = aMinutos(h.cierre) - 30 // la ultima cita empieza 30 min antes de cerrar
        val ini = aMinutos(h.descansoInicio)
        val finDescanso = aMinutos(h.descansoFin)
        while (m <= fin) {
            val enDescanso = m >= ini && m < finDescanso
            if (!enDescanso) horas.add(aTexto(m))
            m += 30
        }
        return horas
    }

    /** Texto corto para mostrar el horario de un dia. */
    fun resumen(h: HorarioAtencion?): String {
        if (h == null || !h.abierto) return "La clínica no atiende este día"
        val base = "Atención: ${h.apertura} – ${h.cierre}"
        return if (h.descansoInicio != h.descansoFin) base + " · Descanso ${h.descansoInicio} – ${h.descansoFin}" else base
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
