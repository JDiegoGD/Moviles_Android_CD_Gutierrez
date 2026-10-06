package com.gutierrez.citas.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Tarjeta blanca de esquinas redondeadas con sombra suave.
 * Si recibe [onClick] se puede tocar; si no, es solo un contenedor.
 */
@Composable
fun TarjetaBase(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contenido: @Composable ColumnScope.() -> Unit
) {
    val forma = RoundedCornerShape(16.dp)
    val colores = CardDefaults.cardColors(containerColor = Color.White)
    val elevacion = CardDefaults.cardElevation(defaultElevation = 2.dp)

    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier,
            shape = forma,
            colors = colores,
            elevation = elevacion,
            content = contenido
        )
    } else {
        Card(
            modifier = modifier,
            shape = forma,
            colors = colores,
            elevation = elevacion,
            content = contenido
        )
    }
}
