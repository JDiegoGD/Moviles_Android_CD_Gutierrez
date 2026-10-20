package com.gutierrez.huellitasvet.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
        Box(Modifier.width(24.dp).fillMaxHeight()) {
            if (!esUltimo) {
                Canvas(Modifier.fillMaxSize()) {
                    val cx = size.width / 2
                    drawLine(linea, Offset(cx, 24.dp.toPx()), Offset(cx, size.height), strokeWidth = 3f)
                }
            }
            Surface(shape = CircleShape, color = primario, modifier = Modifier.size(24.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.Check, null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(15.dp))
                }
            }
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
                        Formato.fechaDiaMes(fecha), color = primario,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Text(
                    diagnostico, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (peso != null) {
                    Row(
                        Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Filled.Favorite, null, tint = Color(0xFFE11D48), modifier = Modifier.size(16.dp))
                        Text("Peso: ${Formato.peso(peso)}", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}
