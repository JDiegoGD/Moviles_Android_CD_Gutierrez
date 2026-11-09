package com.gutierrez.biblioteca.data.auth

import com.gutierrez.biblioteca.domain.model.Sesion

/**
 * Contrato de autenticacion. Los ViewModels no conocen Firebase: solo esta interface.
 */
interface AuthRepository {

    /** Sesion guardada de un ingreso anterior, o null si no hay (o no se pudo comprobar). */
    suspend fun restaurarSesion(): Sesion?

    /** Ingresa con correo y contrasena. El error trae un mensaje listo para mostrar. */
    suspend fun ingresar(correo: String, contrasena: String): Result<Sesion>

    fun cerrarSesion()
}

/** Error de autenticacion con un mensaje pensado para el usuario. */
class AuthError(mensaje: String) : Exception(mensaje)
