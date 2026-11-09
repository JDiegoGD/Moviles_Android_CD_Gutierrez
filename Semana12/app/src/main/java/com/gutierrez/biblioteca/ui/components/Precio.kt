package com.gutierrez.biblioteca.ui.components

import java.util.Locale

/** Da formato de soles a un precio, por ejemplo 1999.99 -> "S/ 1,999.99". */
fun formatearPrecio(valor: Double): String = "S/ " + String.format(Locale.US, "%,.2f", valor)
