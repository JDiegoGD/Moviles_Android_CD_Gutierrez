package com.gutierrez.huellitasvet.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Imagen de cabecera con las esquinas de abajo redondeadas (login y registro). */
@Composable
fun CabeceraImagen(
    @DrawableRes imagen: Int,
    modifier: Modifier = Modifier,
    alto: Dp = 200.dp,
    alineacion: Alignment = Alignment.CenterStart
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(alto)
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
    ) {
        Image(
            painter = painterResource(imagen),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alignment = alineacion
        )
    }
}
