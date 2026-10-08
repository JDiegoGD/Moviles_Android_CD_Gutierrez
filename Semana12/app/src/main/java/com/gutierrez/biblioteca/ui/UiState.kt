package com.gutierrez.biblioteca.ui

import java.io.IOException

/**
 * Estado de una pantalla. El ViewModel lo expone como StateFlow<UiState<T>> y la
 * pantalla solo dibuja segun el estado actual.
 */
sealed interface UiState<out T> {
    data object Cargando : UiState<Nothing>
    data class Exito<T>(val datos: T) : UiState<T>
    data class Error(val mensaje: String) : UiState<Nothing>
}

/** Texto de error para el usuario segun el tipo de fallo; lo comparten los ViewModels. */
fun mensajeDeError(error: Throwable): String =
    if (error is IOException) {
        "No se pudo conectar. Revisa tu conexión a internet."
    } else {
        "Ocurrió un error al cargar los datos."
    }
