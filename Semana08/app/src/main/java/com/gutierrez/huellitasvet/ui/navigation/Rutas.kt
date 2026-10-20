package com.gutierrez.huellitasvet.ui.navigation

object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTRO = "registro"

    // Dueño
    const val INICIO = "inicio"
    const val MASCOTAS = "mascotas"
    const val FICHA_MASCOTA = "ficha_mascota/{id}"
    const val FORM_MASCOTA = "form_mascota?id={id}"
    const val PEDIR_CITA = "pedir_cita?mascotaId={mascotaId}"
    const val MIS_CITAS = "mis_citas"
    const val PERFIL = "perfil"

    // Veterinario
    const val PANEL = "panel"
    const val AGENDA = "agenda"
    const val ATENDER_CITA = "atender_cita/{id}"
    const val PACIENTES = "pacientes"
    const val SERVICIOS = "servicios"
    const val FORM_SERVICIO = "form_servicio?id={id}"
    const val AJUSTES = "ajustes"

    fun fichaMascota(id: Int) = "ficha_mascota/$id"
    fun formMascota(id: Int? = null) = if (id == null) "form_mascota" else "form_mascota?id=$id"
    fun pedirCita(mascotaId: Int? = null) = if (mascotaId == null) "pedir_cita" else "pedir_cita?mascotaId=$mascotaId"
    fun atenderCita(id: Int) = "atender_cita/$id"
    fun formServicio(id: Int? = null) = if (id == null) "form_servicio" else "form_servicio?id=$id"
}
