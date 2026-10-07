package com.gutierrez.biblioteca.ui

/**
 * Estado de una pantalla. El ViewModel lo expone como StateFlow<UiState<T>> y la
 * pantalla solo dibuja segun el estado actual.
 */
sealed interface UiState<out T> {
    data object Cargando : UiState<Nothing>
    data class Exito<T>(val datos: T) : UiState<T>
    data class Error(val mensaje: String) : UiState<Nothing>
}
