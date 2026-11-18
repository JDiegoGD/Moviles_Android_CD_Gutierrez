package com.gutierrez.huellitasvet.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gutierrez.huellitasvet.R
import com.gutierrez.huellitasvet.model.Especie

/** Avatar circular con la ilustracion de un perro, un gato u otro animal. [especie] = Especie.name */
@Composable
fun MascotaAvatar(especie: String, modifier: Modifier = Modifier, tamano: Dp = 56.dp) {
    val imagen = when (especie) {
        Especie.PERRO.name -> R.drawable.avatar_perro
        Especie.GATO.name -> R.drawable.avatar_gato
        else -> R.drawable.avatar_otro
    }
    Image(
        painter = painterResource(imagen),
        contentDescription = etiquetaEspecie(especie),
        modifier = modifier.size(tamano).clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}

/** "PERRO" -> "Perro" (para mostrar al usuario). */
fun etiquetaEspecie(especie: String): String =
    especie.lowercase().replaceFirstChar { it.uppercase() }

fun etiquetaSexo(sexo: String): String =
    sexo.lowercase().replaceFirstChar { it.uppercase() }
