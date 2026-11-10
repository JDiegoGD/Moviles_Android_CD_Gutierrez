package com.gutierrez.biblioteca.domain.model

/** Opciones de orden y filtrado para la lista de productos. */
data class OpcionesLista(
    val orden: Orden = Orden.RELEVANCIA,
    val precioMinimo: Float? = null,
    val precioMaximo: Float? = null,
    val soloFavoritos: Boolean = false
) {
    val hayFiltrosActivos: Boolean
        get() = orden != Orden.RELEVANCIA || precioMinimo != null || precioMaximo != null || soloFavoritos

    val cantidadFiltrosActivos: Int
        get() {
            var count = 0
            if (orden != Orden.RELEVANCIA) count++
            if (precioMinimo != null || precioMaximo != null) count++
            if (soloFavoritos) count++
            return count
        }
}
