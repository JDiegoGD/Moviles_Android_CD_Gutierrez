package com.gutierrez.huellitasvet.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gutierrez.huellitasvet.model.Formato
import java.time.LocalDate

/** Selector de fecha de la pantalla Pedir cita: dia de la semana + numero. */
@Composable
fun FechaChip(
    fecha: LocalDate,
    seleccionada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primario = MaterialTheme.colorScheme.primary
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = if (seleccionada) primario else MaterialTheme.colorScheme.surface,
        border = if (seleccionada) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val texto = if (seleccionada) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onSurface
            Text(Formato.diaSemana(fecha), style = MaterialTheme.typography.labelSmall, color = texto)
            Text(
                fecha.dayOfMonth.toString(), fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium, color = texto
            )
        }
    }
}
