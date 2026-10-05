package com.gutierrez.citas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gutierrez.citas.ui.theme.AzulClinica
import com.gutierrez.citas.ui.theme.AzulNiebla

fun iconoDeEspecialidad(especialidadId: Int): ImageVector = when (especialidadId) {
    1 -> Icons.Filled.MedicalServices
    2 -> Icons.Filled.ChildCare
    3 -> Icons.Filled.Female
    4 -> Icons.Filled.Favorite
    5 -> Icons.Filled.Face
    6 -> Icons.Filled.SentimentSatisfied
    7 -> Icons.Filled.RemoveRedEye
    8 -> Icons.Filled.Healing
    else -> Icons.Filled.MedicalServices
}

@Composable
fun IconoEspecialidad(
    especialidadId: Int,
    modifier: Modifier = Modifier,
    tamano: Dp = 48.dp
) {
    Box(
        modifier = modifier
            .size(tamano)
            .clip(CircleShape)
            .background(AzulNiebla),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = iconoDeEspecialidad(especialidadId),
            contentDescription = null,
            tint = AzulClinica,
            modifier = Modifier.size(tamano * 0.5f)
        )
    }
}