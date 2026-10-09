package com.gutierrez.citas.data.repository

import androidx.compose.runtime.mutableStateListOf
import com.gutierrez.citas.data.model.Cita
import com.gutierrez.citas.data.model.Especialidad
import com.gutierrez.citas.data.model.Local
import com.gutierrez.citas.data.model.Medico
import com.gutierrez.citas.data.model.Resultado
import com.gutierrez.citas.data.model.Usuario
import com.gutierrez.citas.util.diasEnTexto
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

object Repositorio {

    // ------------------------------------------------------------------
    // USUARIOS Y SESIÓN
    // ------------------------------------------------------------------
    val usuarios = mutableListOf<Usuario>()

    /** Paciente que inició sesión; null cuando no hay sesión abierta. */
    var usuarioActual: Usuario? = null

    // Registra al paciente si su correo aún no existe. Devuelve false si ya estaba registrado.
    fun registrarUsuario(usuario: Usuario): Boolean {
        val correoLimpio = usuario.correo.trim()
        val yaExiste = usuarios.any { it.correo.equals(correoLimpio, ignoreCase = true) }
        if (yaExiste) return false
        usuarios.add(usuario.copy(correo = correoLimpio))
        return true
    }

    // Abre sesión si hay un usuario con ese correo y esa contraseña.
    // Si no coincide ninguno, usuarioActual queda en null y se devuelve false.
    fun iniciarSesion(correo: String, contrasena: String): Boolean {
        val encontrado = usuarios.find {
            it.correo.equals(correo.trim(), ignoreCase = true) && it.contrasena == contrasena
        }
        usuarioActual = encontrado
        return encontrado != null
    }

    fun cerrarSesion() {
        usuarioActual = null
    }

    // ------------------------------------------------------------------
    // LOCALES
    // ------------------------------------------------------------------
    val locales = listOf(
        Local(1, "Sede Los Olivos", "Av. Los Olivos 123, Los Olivos"),
        Local(2, "Sede Miraflores", "Av. Larco 845, Miraflores"),
        Local(3, "Sede San Isidro", "Av. Javier Prado Este 1260, San Isidro")
    )

    var localElegido: Local? = null

    fun obtenerLocal(id: Int): Local? {
        return locales.find { it.id == id }
    }

    // ------------------------------------------------------------------
    // ESPECIALIDADES
    // ------------------------------------------------------------------
    val especialidades = listOf(
        Especialidad(1, "Medicina General", "Consulta y control integral"),
        Especialidad(2, "Pediatría", "Salud de niños y adolescentes"),
        Especialidad(3, "Ginecología", "Cuidado de la salud femenina"),
        Especialidad(4, "Cardiología", "Corazón y circulación"),
        Especialidad(5, "Dermatología", "Piel, cabello y uñas"),
        Especialidad(6, "Odontología", "Salud bucal y dental"),
        Especialidad(7, "Oftalmología", "Cuidado de la visión"),
        Especialidad(8, "Traumatología", "Huesos, músculos y articulaciones")
    )

    // Especialidades cuyo nombre contiene el texto, sin importar mayúsculas.
    // Con el texto vacío salen todas.
    fun buscarEspecialidades(texto: String): List<Especialidad> {
        val buscado = texto.trim()
        return especialidades.filter { it.nombre.contains(buscado, ignoreCase = true) }
    }

    // Las primeras 5 especialidades, para el carrusel de Inicio.
    fun especialidadesDestacadas(): List<Especialidad> {
        return especialidades.take(5)
    }

    fun obtenerEspecialidad(id: Int): Especialidad? {
        return especialidades.find { it.id == id }
    }

    // ------------------------------------------------------------------
    // MÉDICOS
    // ------------------------------------------------------------------

    // Arma la URL de un retrato de randomuser.me. genero: "men" o "women"; numero: de 0 a 99.
    private fun retrato(genero: String, numero: Int): String =
        "https://randomuser.me/api/portraits/$genero/$numero.jpg"

    val medicos = listOf(
        Medico(1, "Dra. Rosa Delgado", 1, 4.8, 12, 134, setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY), "09:00", "13:00", retrato("women", 21)),
        Medico(2, "Dr. Hugo Benavides", 1, 4.5, 8, 92, setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY), "14:00", "18:00", retrato("men", 14)),
        Medico(3, "Dra. Marisol Quiroga", 2, 4.7, 10, 118, setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY), "08:00", "12:00", retrato("women", 52)),
        Medico(4, "Dr. Raúl Espinoza", 2, 4.4, 6, 61, setOf(DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), "15:00", "19:00", retrato("men", 29)),
        Medico(5, "Dra. Teresa Villanueva", 3, 4.9, 15, 187, setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY), "14:00", "18:00", retrato("women", 71)),
        Medico(6, "Dra. Gabriela Montoya", 3, 4.6, 9, 103, setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY), "09:00", "13:00", retrato("women", 8)),
        Medico(7, "Dr. Alonso Cárdenas", 4, 4.9, 16, 226, setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.THURSDAY), "09:00", "13:00", retrato("men", 63)),
        Medico(8, "Dr. Percy Altamirano", 4, 4.5, 11, 94, setOf(DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY), "14:00", "19:00", retrato("men", 40)),
        Medico(9, "Dra. Karina Bustamante", 5, 4.8, 10, 121, setOf(DayOfWeek.MONDAY, DayOfWeek.THURSDAY), "08:00", "12:00", retrato("women", 35)),
        Medico(10, "Dr. Sergio Medina", 5, 4.4, 7, 58, setOf(DayOfWeek.TUESDAY, DayOfWeek.FRIDAY), "15:00", "19:00", retrato("men", 77)),
        Medico(11, "Dr. Iván Camacho", 6, 4.6, 9, 84, setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY), "08:00", "12:00", retrato("men", 5)),
        Medico(12, "Dra. Natalia Ponce", 6, 4.7, 8, 97, setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY), "14:00", "18:00", retrato("women", 59)),
        Medico(13, "Dra. Beatriz Lazo", 7, 4.8, 13, 142, setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY), "09:00", "13:00", retrato("women", 83)),
        Medico(14, "Dr. Emilio Arce", 7, 4.3, 5, 47, setOf(DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY), "09:00", "13:00", retrato("men", 91)),
        Medico(15, "Dr. Joaquín Palacios", 8, 4.7, 14, 133, setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), "14:00", "18:00", retrato("men", 24)),
        Medico(16, "Dra. Daniela Zegarra", 8, 4.5, 7, 76, setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), "15:00", "19:00", retrato("women", 46))
    )

    fun obtenerMedico(id: Int): Medico? {
        return medicos.find { it.id == id }
    }

    // Médicos de una especialidad, del mejor calificado al peor.
    fun medicosPorEspecialidad(especialidadId: Int): List<Medico> {
        return medicos
            .filter { it.especialidadId == especialidadId }
            .sortedByDescending { it.calificacion }
    }

    // Igual que la anterior, pero filtrando además por el nombre del médico.
    fun buscarMedicos(especialidadId: Int, texto: String): List<Medico> {
        val buscado = texto.trim()
        return medicosPorEspecialidad(especialidadId)
            .filter { it.nombre.contains(buscado, ignoreCase = true) }
    }

    // Búsqueda general de médicos por nombre de médico o por nombre de su especialidad.
    // Con el texto vacío devuelve todos los médicos ordenados por nombre.
    fun buscarMedicos(texto: String): List<Medico> {
        val buscado = texto.trim()
        if (buscado.isEmpty()) {
            return medicos.sortedBy { it.nombre }
        }
        return medicos.filter { medico ->
            val especialidadNombre = obtenerEspecialidad(medico.especialidadId)?.nombre.orEmpty()
            medico.nombre.contains(buscado, ignoreCase = true) ||
                    especialidadNombre.contains(buscado, ignoreCase = true)
        }
    }

    // Genera las franjas horarias de un médico cada 30 minutos desde horaInicio hasta antes de horaFin.
    fun generarHorariosMedico(medico: Medico): List<String> {
        val resultado = mutableListOf<String>()
        try {
            val inicio = LocalTime.parse(medico.horaInicio)
            val fin = LocalTime.parse(medico.horaFin)
            var actual = inicio
            while (actual.isBefore(fin)) {
                resultado.add(actual.toString())
                actual = actual.plusMinutes(30)
            }
        } catch (_: Exception) {}
        return resultado
    }

    // Calcula la etiqueta de disponibilidad basada en la fecha actual y los días de atención.
    fun calcularDisponibilidad(medico: Medico, hoy: LocalDate = LocalDate.now()): String {
        val diaHoy = hoy.dayOfWeek
        if (diaHoy in medico.diasAtencion) {
            return "Disponible hoy"
        }
        val manana = hoy.plusDays(1)
        if (manana.dayOfWeek in medico.diasAtencion) {
            return "Disponible mañana"
        }
        return "Atiende ${diasEnTexto(medico.diasAtencion)}"
    }

    // ------------------------------------------------------------------
    // CITAS Y HORARIOS
    // ------------------------------------------------------------------
    // Lista observable: las pantallas que la leen se redibujan solas cuando se agrega o se cancela una cita
    val citas = mutableStateListOf<Cita>()

    /** Número que recibirá la próxima cita creada; se incrementa con cada una. */
    private var siguienteIdCita = 1

    // Horas libres de un médico en una fecha (formato "2026-10-12"):
    // si el médico atiende ese día, a sus horas base se les quitan las citas reservadas.
    fun horariosDisponibles(medicoId: Int, fecha: String): List<String> {
        val medico = obtenerMedico(medicoId) ?: return emptyList()
        val localDate = try {
            LocalDate.parse(fecha)
        } catch (_: Exception) {
            return emptyList()
        }
        if (localDate.dayOfWeek !in medico.diasAtencion) {
            return emptyList()
        }
        val baseMedico = generarHorariosMedico(medico)
        val ocupadas = citas
            .filter { it.medicoId == medicoId && it.fecha == fecha }
            .map { it.hora }
        return baseMedico.filter { it !in ocupadas }
    }

    // Crea la cita para el usuario en sesión. Devuelve false si no hay sesión, si la fecha/hora
    // no pertenecen al horario del médico, o si la hora ya fue tomada.
    fun agendarCita(medicoId: Int, especialidadId: Int, fecha: String, hora: String): Boolean {
        val paciente = usuarioActual ?: return false
        val medico = obtenerMedico(medicoId) ?: return false
        val localDate = try {
            LocalDate.parse(fecha)
        } catch (_: Exception) {
            return false
        }
        if (localDate.dayOfWeek !in medico.diasAtencion) return false

        val baseMedico = generarHorariosMedico(medico)
        if (hora !in baseMedico) return false

        val horaTomada = citas.any {
            it.medicoId == medicoId && it.fecha == fecha && it.hora == hora
        }
        if (horaTomada) return false

        val localId = localElegido?.id ?: 1
        citas.add(Cita(siguienteIdCita++, paciente.correo, medicoId, especialidadId, localId, fecha, hora))
        localElegido = null
        return true
    }

    fun obtenerCita(id: Int): Cita? {
        return citas.find { it.id == id }
    }

    // Citas del usuario en sesión, de la más próxima a la más lejana. Sin sesión: lista vacía.
    fun citasDelUsuario(): List<Cita> {
        val paciente = usuarioActual ?: return emptyList()
        return citas
            .filter { it.correoUsuario.equals(paciente.correo, ignoreCase = true) }
            .sortedWith(compareBy<Cita>({ it.fecha }, { it.hora }))
    }

    // Elimina la cita y, con ello, vuelve a liberar su horario. Devuelve true si existía.
    fun cancelarCita(id: Int): Boolean {
        return citas.removeAll { it.id == id }
    }

    // ------------------------------------------------------------------
    // RESULTADOS
    // ------------------------------------------------------------------

    /** Lista fija de ejemplo: es la misma para todos los usuarios. */
    val resultados = listOf(
        Resultado(1, "Hemograma completo", "2026-09-18", 1, "Disponible"),
        Resultado(2, "Perfil lipídico", "2026-09-25", 4, "Disponible"),
        Resultado(3, "Electrocardiograma", "2026-10-01", 4, "Disponible"),
        Resultado(4, "Radiografía de rodilla", "2026-10-02", 8, "En proceso"),
        Resultado(5, "Examen de la vista", "2026-09-10", 7, "Disponible"),
        Resultado(6, "Glucosa en ayunas", "2026-10-05", 1, "En proceso")
    )
}
