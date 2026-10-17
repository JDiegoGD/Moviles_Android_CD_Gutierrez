package com.gutierrez.huellitasvet.data.local

import com.gutierrez.huellitasvet.data.local.entity.Servicio

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

    /** Version del proyecto base: solo siembra los servicios. */
    suspend fun sembrarServicios(db: VetDatabase) {
        if (db.servicioDao().contar() == 0) {
            db.servicioDao().insertarTodos(servicios())
        }
    }

    // TODO (Paso 3): suspend fun sembrar(db) -> servicios, usuarios, mascotas y ~10 citas
}
