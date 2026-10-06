package com.gutierrez.citas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PregnantWoman
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Ícono, color y fondo suave de una especialidad. */
class EstiloEspecialidad(
    val icono: ImageVector,
    val color: Color,
    val fondo: Color
)

private val AzulEsp = Color(0xFF2563EB)
private val FondoAzulEsp = Color(0xFFE3EDFF)
private val NaranjaEsp = Color(0xFFF28C28)
private val FondoNaranjaEsp = Color(0xFFFFEBD6)

/** Cada especialidad (el id viene del Repositorio) tiene su propio ícono y su color. */
fun estiloDeEspecialidad(especialidadId: Int): EstiloEspecialidad = when (especialidadId) {
    1 -> EstiloEspecialidad(Icons.Filled.Person, AzulEsp, FondoAzulEsp)
    2 -> EstiloEspecialidad(Icons.Filled.ChildCare, NaranjaEsp, FondoNaranjaEsp)
    3 -> EstiloEspecialidad(Icons.Filled.PregnantWoman, Color(0xFFD946A8), Color(0xFFFCE4F3))
    4 -> EstiloEspecialidad(Icons.Filled.Favorite, Color(0xFFDC2626), Color(0xFFFDE4E4))
    5 -> EstiloEspecialidad(Icons.Filled.Face, NaranjaEsp, FondoNaranjaEsp)
    6 -> EstiloEspecialidad(Icons.Filled.SentimentSatisfied, Color(0xFF0EA5A4), Color(0xFFDDF5F4))
    7 -> EstiloEspecialidad(Icons.Filled.Visibility, AzulEsp, FondoAzulEsp)
    8 -> EstiloEspecialidad(Icons.Filled.Build, AzulEsp, FondoAzulEsp)
    else -> EstiloEspecialidad(Icons.Filled.Person, AzulEsp, FondoAzulEsp)
}

/** El ícono de la especialidad dentro de un círculo del color que le toca. */
@Composable
fun IconoEspecialidad(
    especialidadId: Int,
    modifier: Modifier = Modifier,
    tamano: Dp = 48.dp
) {
    val estilo = estiloDeEspecialidad(especialidadId)
    Box(
        modifier = modifier
            .size(tamano)
            .clip(CircleShape)
            .background(estilo.fondo),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = estilo.icono,
            contentDescription = null,
            tint = estilo.color,
            modifier = Modifier.size(tamano * 0.55f)
        )
    }
}
