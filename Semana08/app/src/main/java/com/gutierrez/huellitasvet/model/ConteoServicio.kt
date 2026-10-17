package com.gutierrez.huellitasvet.model

/** Resultado de la consulta GROUP BY: cuantas citas tiene cada servicio. */
data class ConteoServicio(
    val nombre: String,
    val total: Int
)
