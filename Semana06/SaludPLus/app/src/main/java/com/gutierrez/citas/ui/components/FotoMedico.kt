package com.gutierrez.citas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.gutierrez.citas.ui.theme.AzulClinica
import com.gutierrez.citas.ui.theme.AzulNiebla

fun inicialesDe(nombre: String): String =
    nombre.split(" ")
        .filter { it.isNotBlank() && it != "Dr." && it != "Dra." }
        .take(2)
        .joinToString("") { it.first().uppercase() }


@Composable
fun FotoMedico(
    nombre: String,
    foto: String,
    modifier: Modifier = Modifier,
    tamano: Dp = 64.dp
) {
    SubcomposeAsyncImage(
        model = foto,
        contentDescription = nombre,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(tamano)
            .clip(CircleShape)
            .background(AzulNiebla),
        loading = { Iniciales(nombre, tamano) },
        error = { Iniciales(nombre, tamano) }
    )
}

@Composable
private fun Iniciales(nombre: String, tamano: Dp) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = inicialesDe(nombre),
            fontSize = (tamano.value * 0.32f).sp,
            fontWeight = FontWeight.Bold,
            color = AzulClinica
        )
    }
}