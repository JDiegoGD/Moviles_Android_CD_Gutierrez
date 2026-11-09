package com.gutierrez.biblioteca.domain.model

/** Tipos de usuario de la app. El rol se guarda en Firestore (usuarios/{uid}, campo "rol"). */
enum class Rol { USUARIO, ADMIN }

/** Usuario que ingreso: quien es y que puede hacer. */
data class Sesion(
    val uid: String,
    val correo: String,
    val rol: Rol
)
