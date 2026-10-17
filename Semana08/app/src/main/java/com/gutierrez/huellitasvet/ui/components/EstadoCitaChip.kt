package com.gutierrez.huellitasvet.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gutierrez.huellitasvet.model.EstadoCita
import com.gutierrez.huellitasvet.ui.theme.ColorAtendida
import com.gutierrez.huellitasvet.ui.theme.ColorCancelada
import com.gutierrez.huellitasvet.ui.theme.ColorConfirmada
import com.gutierrez.huellitasvet.ui.theme.ColorPendiente

fun colorEstado(estado: EstadoCita): Color = when (estado) {
    EstadoCita.PENDIENTE -> ColorPendiente
    EstadoCita.CONFIRMADA -> ColorConfirmada
    EstadoCita.ATENDIDA -> ColorAtendida
    EstadoCita.CANCELADA -> ColorCancelada
}

fun etiquetaEstado(estado: EstadoCita): String = when (estado) {
    EstadoCita.PENDIENTE -> "Pendiente"
    EstadoCita.CONFIRMADA -> "Confirmada"
    EstadoCita.ATENDIDA -> "Atendida"
    EstadoCita.CANCELADA -> "Cancelada"
}

@Composable
fun EstadoCitaChip(estado: EstadoCita, modifier: Modifier = Modifier) {
    val color = colorEstado(estado)
    Surface(
        modifier = modifier,
        color = color.copy(alpha = 0.15f),
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = etiquetaEstado(estado),
            color = color,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
        )
    }
}
