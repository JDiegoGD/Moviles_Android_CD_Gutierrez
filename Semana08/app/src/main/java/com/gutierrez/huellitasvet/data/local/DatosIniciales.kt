package com.gutierrez.huellitasvet.data.local

import com.gutierrez.huellitasvet.data.local.entity.Cita
import com.gutierrez.huellitasvet.data.local.entity.Mascota
import com.gutierrez.huellitasvet.data.local.entity.Servicio
import com.gutierrez.huellitasvet.data.local.entity.Usuario
import com.gutierrez.huellitasvet.model.Especie
import com.gutierrez.huellitasvet.model.EstadoCita
import com.gutierrez.huellitasvet.model.Rol
import com.gutierrez.huellitasvet.model.Sexo
import kotlinx.coroutines.flow.first
import java.time.LocalDate

/** Datos ficticios (Ley 29733) que se cargan solo la primera vez. */
object DatosIniciales {

    fun servicios(): List<Servicio> = listOf(
        Servicio(
            nombre = "Consulta general", descripcion = "Revision general de salud",
            duracionMin = 30, precio = 60.0, activo = true, icono = "heart"
        ),
        Servicio(
            nombre = "Vacunación", descripcion = "Aplicación de vacunas al día",
            duracionMin = 30, precio = 45.0, activo = true, icono = "syringe"
        ),
        Servicio(
            nombre = "Baño y corte", descripcion = "Higiene general y corte de pelo",
            duracionMin = 60, precio = 50.0, activo = true, icono = "scissors"
        ),
        Servicio(
            nombre = "Desparasitación", descripcion = "Tratamiento antiparasitario interno y externo",
            duracionMin = 15, precio = 25.0, activo = false, icono = "heart"
        )
    )

    /** Fecha relativa a hoy: n = 0 hoy, -10 hace 10 dias, 2 pasado mañana... */
    private fun dia(n: Long): String = LocalDate.now().plusDays(n).toString()

    suspend fun sembrar(db: VetDatabase) {
        if (db.usuarioDao().contar() != 0) return

        // Servicios
        // Si el proyecto base ya sembro los servicios, los reutilizamos (con sus ids reales)
        val servicioDao = db.servicioDao()
        if (servicioDao.contar() == 0) servicioDao.insertarTodos(servicios())
        val guardados = servicioDao.observarTodos().first()
        val consulta = guardados.first { it.nombre == "Consulta general" }
        val vacuna = guardados.first { it.nombre == "Vacunación" }
        val bano = guardados.first { it.nombre == "Baño y corte" }

        // Usuarios: 2 dueños y 1 veterinario
        db.usuarioDao().insertarTodos(
            listOf(
                Usuario(nombre = "Ana García", correo = "ana@correo.test", clave = "1234", rol = Rol.CLIENTE.name),
                Usuario(nombre = "Luis Rojas", correo = "luis@correo.test", clave = "1234", rol = Rol.CLIENTE.name),
                Usuario(nombre = "Dra. Rojas", correo = "vet@huellitas.test", clave = "admin", rol = Rol.ADMIN.name)
            )
        )
        val ana = db.usuarioDao().login("ana@correo.test", "1234")!!
        val luis = db.usuarioDao().login("luis@correo.test", "1234")!!

        // Mascotas
        val mascotaDao = db.mascotaDao()
        val luna = mascotaDao.insertar(
            Mascota(duenoId = ana.id, nombre = "Luna", especie = Especie.GATO.name, raza = "Siamesa",
                anioNacimiento = LocalDate.now().year - 3, peso = 4.3, sexo = Sexo.HEMBRA.name)
        ).toInt()
        val max = mascotaDao.insertar(
            Mascota(duenoId = ana.id, nombre = "Max", especie = Especie.PERRO.name, raza = "Labrador",
                anioNacimiento = LocalDate.now().year - 5, peso = 24.5, sexo = Sexo.MACHO.name)
        ).toInt()
        val rocky = mascotaDao.insertar(
            Mascota(duenoId = luis.id, nombre = "Rocky", especie = Especie.PERRO.name, raza = "Bulldog",
                anioNacimiento = LocalDate.now().year - 6, peso = 23.8, sexo = Sexo.MACHO.name)
        ).toInt()
        val michi = mascotaDao.insertar(
            Mascota(duenoId = luis.id, nombre = "Michi", especie = Especie.GATO.name, raza = "Mestizo",
                anioNacimiento = LocalDate.now().year - 2, peso = 3.9, sexo = Sexo.MACHO.name)
        ).toInt()

        // ~10 citas en distintos estados (nunca dos no canceladas en el mismo horario)
        val citaDao = db.citaDao()
        fun cita(
            mascotaId: Int, s: Servicio, fecha: String, hora: String, estado: EstadoCita,
            diag: String? = null, trat: String? = null, peso: Double? = null
        ) = Cita(
            mascotaId = mascotaId, servicioId = s.id, fecha = fecha, hora = hora,
            estado = estado.name, precio = s.precio,
            diagnostico = diag, tratamiento = trat, pesoRegistrado = peso
        )

        val citas = listOf(
            // Historial de Luna (3 atenciones)
            cita(luna, vacuna, dia(-10), "10:00", EstadoCita.ATENDIDA, "Vacuna triple felina", "Refuerzo en 12 meses", 4.3),
            cita(luna, consulta, dia(-100), "09:30", EstadoCita.ATENDIDA, "Control de rutina, sin hallazgos", "Ninguno", 4.2),
            cita(luna, bano, dia(-200), "11:00", EstadoCita.ATENDIDA, "Higiene general", "Baño medicado suave", 4.0),
            // Otra atencion pasada
            cita(rocky, consulta, dia(-30), "14:00", EstadoCita.ATENDIDA, "Leve dermatitis", "Shampoo especial 2 semanas", 23.8),
            // Hoy
            cita(max, bano, dia(0), "09:00", EstadoCita.ATENDIDA, "Higiene general", "Corte de pelo", 24.5),
            cita(rocky, consulta, dia(0), "11:00", EstadoCita.PENDIENTE),
            cita(michi, bano, dia(0), "12:30", EstadoCita.PENDIENTE),
            // Proximos dias
            cita(luna, vacuna, dia(1), "10:30", EstadoCita.CONFIRMADA),
            cita(max, consulta, dia(2), "10:00", EstadoCita.PENDIENTE),
            cita(michi, vacuna, dia(3), "09:30", EstadoCita.CANCELADA)
        )
        citas.forEach { citaDao.insertar(it) }
    }
}
