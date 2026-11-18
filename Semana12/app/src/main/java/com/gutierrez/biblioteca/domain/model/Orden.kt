package com.gutierrez.biblioteca.domain.model

/** Opciones de ordenamiento para la lista de productos. */
enum class Orden(val nombre: String) {
    RELEVANCIA("Relevancia"),
    PRECIO_MENOR("Menor precio"),
    PRECIO_MAYOR("Mayor precio"),
    MEJOR_VALORADOS("Mejor valorados")
}
