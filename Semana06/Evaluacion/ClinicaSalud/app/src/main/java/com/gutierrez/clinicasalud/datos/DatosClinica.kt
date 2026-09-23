package com.gutierrez.clinicasalud.datos

import androidx.compose.runtime.mutableStateListOf
import java.text.Normalizer

//Modelo de Medico
data class Medico(
    val id: Int,
    val nombre: String,
    val especialidad: String,
    val calificacion: Double,
    val resenas: Int,
    val experiencia: Int,
    val descripcion: String
)

enum class EstadoCita {
    CONFIRMADA,
    COMPLETADA
}

data class Cita(
    val medico: String,
    val fecha: String,
    val hora: String,
    val estado: EstadoCita
)

data class RegistroHistorial(
    val fecha: String,
    val medico: String,
    val motivo: String
)

//Datos de Ejemplo
object DatosClinica {

    val especialidades = listOf("Todos", "Cardiología", "Pediatría", "Dermatología")

    val medicos = listOf(
        Medico(1, "Dr. Juan", "Cardiología", 4.9, 128, 12,
            "Formación en la Clínica Mayo."),
        Medico(2, "Dr. Diego", "Pediatría", 4.7, 96, 9,
            "Atención al niño y adolescente."),
        Medico(3, "Dr. Gilmer", "Dermatología", 4.8, 110, 15,
            "Tratamiento de acné y prevención de cáncer de piel."),
        Medico(4, "Dr. Sebastian", "Cardiología", 4.6, 74, 8,
            "Prevención y rehabilitación cardiaca."),
    )

    val fechas = listOf("Jue 26", "Vie 27", "Sáb 28", "Lun 30")

    val horas = listOf("9:00 am", "10:30 am", "3:00 pm", "4:30 pm")

    val citas = mutableStateListOf(
        Cita("Dr. Juan", "Mié 15", "3:00 pm", EstadoCita.COMPLETADA)
    )

    val historial = listOf(
        RegistroHistorial("15/09/2026", "Dr. Juan", "Chequeo general"),
        RegistroHistorial("02/07/2026", "Dr. Diego", "Atencion medica a adolencente"),
        RegistroHistorial("18/03/2026", "Dr. Gilmer", "Chequeo de acne")
    )

    fun buscarMedico(id: Int): Medico? = medicos.find { it.id == id }

    fun agendarCita(medico: Medico, fecha: String, hora: String) {
        citas.add(0, Cita(medico.nombre, fecha, hora, EstadoCita.CONFIRMADA))
    }

    // Normaliza el texto quitando tildes, convirtiendo a minúsculas y eliminando espacios en los extremos
    private fun normalizar(texto: String): String {
        val textoSinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD)
            .replace("\\p{Mn}+".toRegex(), "")
        return textoSinTildes.lowercase().trim()
    }

    // Busca médicos filtrando por especialidad y por texto contenido en el nombre o en la especialidad
    fun buscarMedicos(texto: String, especialidad: String): List<Medico> {
        val textoNorm = normalizar(texto)
        return medicos.filter { medico ->
            val cumpleEspecialidad = especialidad == "Todos" || medico.especialidad == especialidad
            val cumpleTexto = textoNorm.isEmpty() ||
                    normalizar(medico.nombre).contains(textoNorm) ||
                    normalizar(medico.especialidad).contains(textoNorm)
            cumpleEspecialidad && cumpleTexto
        }
    }

}