package com.gutierrez.citas.data.repository

import com.gutierrez.citas.data.model.Cita
import com.gutierrez.citas.data.model.Especialidad
import com.gutierrez.citas.data.model.Medico
import com.gutierrez.citas.data.model.Usuario

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
        Medico(1, "Dra. Rosa Delgado", 1, 4.8, 12, 134, "Disponible hoy", retrato("women", 21)),
        Medico(2, "Dr. Hugo Benavides", 1, 4.5, 8, 92, "Disponible mañana", retrato("men", 14)),
        Medico(3, "Dra. Marisol Quiroga", 2, 4.7, 10, 118, "Disponible hoy", retrato("women", 52)),
        Medico(4, "Dr. Raúl Espinoza", 2, 4.4, 6, 61, "Disponible mañana", retrato("men", 29)),
        Medico(5, "Dra. Teresa Villanueva", 3, 4.9, 15, 187, "Disponible hoy", retrato("women", 71)),
        Medico(6, "Dra. Gabriela Montoya", 3, 4.6, 9, 103, "Disponible esta semana", retrato("women", 8)),
        Medico(7, "Dr. Alonso Cárdenas", 4, 4.9, 16, 226, "Disponible hoy", retrato("men", 63)),
        Medico(8, "Dr. Percy Altamirano", 4, 4.5, 11, 94, "Disponible esta semana", retrato("men", 40)),
        Medico(9, "Dra. Karina Bustamante", 5, 4.8, 10, 121, "Disponible mañana", retrato("women", 35)),
        Medico(10, "Dr. Sergio Medina", 5, 4.4, 7, 58, "Disponible hoy", retrato("men", 77)),
        Medico(11, "Dr. Iván Camacho", 6, 4.6, 9, 84, "Disponible hoy", retrato("men", 5)),
        Medico(12, "Dra. Natalia Ponce", 6, 4.7, 8, 97, "Disponible mañana", retrato("women", 59)),
        Medico(13, "Dra. Beatriz Lazo", 7, 4.8, 13, 142, "Disponible esta semana", retrato("women", 83)),
        Medico(14, "Dr. Emilio Arce", 7, 4.3, 5, 47, "Disponible hoy", retrato("men", 91)),
        Medico(15, "Dr. Joaquín Palacios", 8, 4.7, 14, 133, "Disponible mañana", retrato("men", 24)),
        Medico(16, "Dra. Daniela Zegarra", 8, 4.5, 7, 76, "Disponible hoy", retrato("women", 46))
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

    // ------------------------------------------------------------------
    // CITAS Y HORARIOS
    // ------------------------------------------------------------------
    val citas = mutableListOf<Cita>()

    /** Número que recibirá la próxima cita creada; se incrementa con cada una. */
    private var siguienteIdCita = 1

    /** Franjas que atiende cada médico todos los días. */
    val horariosBase = listOf(
        "09:00", "09:30", "10:00",
        "10:30", "11:00", "11:30",
        "12:00", "12:30", "13:00"
    )

    // Horas libres de un médico en una fecha (formato "2026-10-12"): a los horarios base
    // se les quitan las horas de las citas que ese médico ya tiene ese día.
    fun horariosDisponibles(medicoId: Int, fecha: String): List<String> {
        val ocupadas = citas
            .filter { it.medicoId == medicoId && it.fecha == fecha }
            .map { it.hora }
        return horariosBase.filter { it !in ocupadas }
    }

    // Crea la cita para el usuario en sesión. Devuelve false si no hay sesión o si el
    // médico ya tiene una cita en esa fecha y hora (alguien se le adelantó).
    fun agendarCita(medicoId: Int, especialidadId: Int, fecha: String, hora: String): Boolean {
        val paciente = usuarioActual ?: return false
        val horaTomada = citas.any {
            it.medicoId == medicoId && it.fecha == fecha && it.hora == hora
        }
        if (horaTomada) return false
        citas.add(Cita(siguienteIdCita++, paciente.correo, medicoId, especialidadId, fecha, hora))
        return true
    }

    // TODO: Repo-12 -> buscar una cita por id con find (null si no existe).
    fun obtenerCita(id: Int): Cita? {
        return null
    }

    // Citas del usuario en sesión, de la más próxima a la más lejana. Sin sesión: lista vacía.
    fun citasDelUsuario(): List<Cita> {
        val paciente = usuarioActual ?: return emptyList()
        return citas
            .filter { it.correoUsuario.equals(paciente.correo, ignoreCase = true) }
            .sortedWith(compareBy<Cita>({ it.fecha }, { it.hora }))
    }

    // TODO: Repo-14 -> eliminar la cita con removeIf. Devolver true si existía.
    fun cancelarCita(id: Int): Boolean {
        return false
    }
}