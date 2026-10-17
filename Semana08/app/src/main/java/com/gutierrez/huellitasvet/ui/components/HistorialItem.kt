package com.gutierrez.huellitasvet.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gutierrez.huellitasvet.model.Formato

/** Una atencion en la linea de tiempo del historial clinico. */
@Composable
fun HistorialItem(
    fecha: String,
    servicio: String,
    diagnostico: String,
    peso: Double?,
    esUltimo: Boolean,
    modifier: Modifier = Modifier
) {
    val primario = MaterialTheme.colorScheme.primary
    val linea = MaterialTheme.colorScheme.outline
    Row(modifier.fillMaxWidth().height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Canvas(Modifier.width(18.dp).fillMaxHeight()) {
            val cx = size.width / 2
            if (!esUltimo) drawLine(linea, Offset(cx, 18f), Offset(cx, size.height), strokeWidth = 3f)
            drawCircle(primario, radius = 8f, center = Offset(cx, 18f))
        }
        Card(
            Modifier.weight(1f).padding(bottom = 10.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(servicio, fontWeight = FontWeight.Bold)
                    Text(
                        Formato.fechaCorta(fecha), color = primario,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Text(
                    diagnostico, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (peso != null) {
                    Text(
                        "Peso: ${Formato.peso(peso)}", style = MaterialTheme.typography.labelMedium,
                        color = primario
                    )
                }
            }
        }
    }
}
