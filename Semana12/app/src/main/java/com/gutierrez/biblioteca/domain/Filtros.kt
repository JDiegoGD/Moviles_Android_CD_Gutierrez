package com.gutierrez.biblioteca.domain

import com.gutierrez.biblioteca.domain.model.Item
import com.gutierrez.biblioteca.domain.model.OpcionesLista
import com.gutierrez.biblioteca.domain.model.Orden

/** Función pura para aplicar búsqueda, filtros y ordenamiento a una lista de items. */
fun aplicarOpciones(
    items: List<Item>,
    opciones: OpcionesLista,
    consulta: String = ""
): List<Item> {
    // 1. Filtrar por texto de búsqueda
    val porConsulta = if (consulta.isBlank()) {
        items
    } else {
        items.filter {
            it.titulo.contains(consulta, ignoreCase = true) ||
                it.subtitulo.contains(consulta, ignoreCase = true)
        }
    }

    // 2. Filtrar por solo favoritos
    val porFavoritos = if (opciones.soloFavoritos) {
        porConsulta.filter { it.esFavorito }
    } else {
        porConsulta
    }

    // 3. Filtrar por rango de precios
    val porPrecio = porFavoritos.filter { item ->
        val precio = item.precio ?: 0.0
        val min = opciones.precioMinimo?.toDouble() ?: 0.0
        val max = opciones.precioMaximo?.toDouble() ?: Double.MAX_VALUE
        precio in min..max
    }

    // 4. Ordenar
    return when (opciones.orden) {
        Orden.RELEVANCIA -> porPrecio
        Orden.PRECIO_MENOR -> porPrecio.sortedBy { it.precio ?: 0.0 }
        Orden.PRECIO_MAYOR -> porPrecio.sortedByDescending { it.precio ?: 0.0 }
        Orden.MEJOR_VALORADOS -> porPrecio.sortedByDescending { it.valoracion ?: 0.0 }
    }
}
