package com.gutierrez.citas.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeParseException

private val mesesMayuscula = listOf(
    "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
    "Julio", "Agosto", "Setiembre", "Octubre", "Noviembre", "Diciembre"
)

private val mesesMinuscula = listOf(
    "enero", "febrero", "marzo", "abril", "mayo", "junio",
    "julio", "agosto", "setiembre", "octubre", "noviembre", "diciembre"
)

private val diasAbreviados = mapOf(
    DayOfWeek.MONDAY to "Lun",
    DayOfWeek.TUESDAY to "Mar",
    DayOfWeek.WEDNESDAY to "Mié",
    DayOfWeek.THURSDAY to "Jue",
    DayOfWeek.FRIDAY to "Vie",
    DayOfWeek.SATURDAY to "Sáb",
    DayOfWeek.SUNDAY to "Dom"
)

private val diasLargo = mapOf(
    DayOfWeek.MONDAY to "Lunes",
    DayOfWeek.TUESDAY to "Martes",
    DayOfWeek.WEDNESDAY to "Miércoles",
    DayOfWeek.THURSDAY to "Jueves",
    DayOfWeek.FRIDAY to "Viernes",
    DayOfWeek.SATURDAY to "Sábado",
    DayOfWeek.SUNDAY to "Domingo"
)

/**
 * Devuelve los siguientes [cantidad] días hábiles a partir de [desde] (incluido si es hábil).
 * Sábados y domingos nunca se incluyen.
 */
fun proximosDiasHabiles(desde: LocalDate, cantidad: Int = 5): List<LocalDate> {
    val resultado = mutableListOf<LocalDate>()
    var actual = desde
    while (resultado.size < cantidad) {
        if (actual.dayOfWeek != DayOfWeek.SATURDAY && actual.dayOfWeek != DayOfWeek.SUNDAY) {
            resultado.add(actual)
        }
        actual = actual.plusDays(1)
    }
    return resultado
}

/**
 * Devuelve el nombre del mes con mayúscula inicial y el año, por ejemplo "Octubre 2026".
 */
fun nombreMesAnio(fecha: LocalDate): String {
    val mes = mesesMayuscula[fecha.monthValue - 1]
    return "$mes ${fecha.year}"
}

/**
 * Devuelve la abreviatura del día de la semana en español, por ejemplo "Lun".
 */
fun abreviaturaDia(fecha: LocalDate): String {
    return diasAbreviados[fecha.dayOfWeek] ?: ""
}

/**
 * Convierte un conjunto de días de la semana a texto legible en español, por ejemplo:
 * "Lun, Mié y Vie" o "Mar y Jue".
 */
fun diasEnTexto(dias: Set<DayOfWeek>): String {
    if (dias.isEmpty()) return ""
    val ordenados = dias.sortedBy { it.value }.mapNotNull { diasAbreviados[it] }
    if (ordenados.isEmpty()) return ""
    if (ordenados.size == 1) return ordenados[0]
    if (ordenados.size == 2) return "${ordenados[0]} y ${ordenados[1]}"
    val anteriores = ordenados.dropLast(1).joinToString(", ")
    val ultimo = ordenados.last()
    return "$anteriores y $ultimo"
}

/**
 * Convierte una fecha ISO ("yyyy-MM-dd") a fecha larga en español, por ejemplo:
 * "Martes 16 de setiembre 2026". Si no se puede convertir, devuelve el texto original.
 */
fun fechaLargaEs(fechaIso: String): String {
    val fecha = try {
        LocalDate.parse(fechaIso)
    } catch (_: DateTimeParseException) {
        return fechaIso
    } catch (_: Exception) {
        return fechaIso
    }
    val diaSemana = diasLargo[fecha.dayOfWeek] ?: ""
    val mes = mesesMinuscula[fecha.monthValue - 1]
    return "$diaSemana ${fecha.dayOfMonth} de $mes ${fecha.year}"
}
