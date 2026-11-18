package com.gutierrez.huellitasvet.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

/** Ilustracion que se muestra cuando una lista no tiene datos (agenda sin citas, busqueda sin resultados). */
@Composable
fun EstadoVacio(
    @DrawableRes imagen: Int,
    descripcion: String,
    modifier: Modifier = Modifier
) {
    Box(modifier.fillMaxWidth().padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(imagen),
            contentDescription = descripcion,
            modifier = Modifier.widthIn(max = 300.dp).fillMaxWidth().clip(RoundedCornerShape(24.dp)),
            contentScale = ContentScale.Fit
        )
    }
}
