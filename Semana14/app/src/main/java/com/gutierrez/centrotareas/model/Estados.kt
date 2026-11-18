package com.gutierrez.centrotareas.model

data class PrimosProgreso(
    val progreso: Float,
    val cantidad: Int
)

data class MedicionCarga(
    val id: Int,
    val modo: String,
    val milisegundos: Long
)

sealed interface PrimosUi {
    data object Inicial : PrimosUi
    data class Calculando(val progreso: Float, val cantidad: Int) : PrimosUi
    data class Listo(val cantidad: Int, val milisegundos: Long) : PrimosUi
    data class Cancelado(val progreso: Float) : PrimosUi
}

sealed interface CargaUi {
    data object Inicial : CargaUi
    data object Cargando : CargaUi
    data class Exito(
        val modo: String,
        val perfil: String,
        val pedidos: Int,
        val milisegundos: Long
    ) : CargaUi
    data class Error(val mensaje: String) : CargaUi
}
