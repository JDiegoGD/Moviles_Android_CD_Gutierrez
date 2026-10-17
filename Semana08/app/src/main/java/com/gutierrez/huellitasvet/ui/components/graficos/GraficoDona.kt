package com.gutierrez.huellitasvet.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

internal fun colorDe(d: DatoGrafico, indice: Int, paleta: List<Color>): Color =
    if (d.color == Color.Unspecified) paleta[indice % paleta.size] else d.color

/** Dona con leyenda. Recibe List<DatoGrafico>; el total se muestra al centro. */
@Composable
fun GraficoDona(
    datos: List<DatoGrafico>,
    modifier: Modifier = Modifier,
    textoCentro: String = "citas"
) {
    val paleta = listOf(
        MaterialTheme.colorScheme.primary, Color(0xFFD97706),
        Color(0xFF16A34A), Color(0xFF475569), Color(0xFFDC2626)
    )
    val total = datos.sumOf { it.valor.toDouble() }.toFloat()
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(120.dp), contentAlignment = Alignment.Center) {
            Canvas(Modifier.size(110.dp)) {
                val grosor = 26f
                val lado = size.minDimension - grosor
                val tope = Offset(grosor / 2, grosor / 2)
                if (total <= 0f) {
                    drawArc(
                        Color.LightGray, 0f, 360f, false, tope, Size(lado, lado),
                        style = Stroke(grosor)
                    )
                } else {
                    var inicio = -90f
                    datos.forEachIndexed { i, d ->
                        val barrido = d.valor / total * 360f
                        if (barrido > 0f) {
                            drawArc(
                                colorDe(d, i, paleta), inicio, barrido - 1.5f, false, tope,
                                Size(lado, lado), style = Stroke(grosor, cap = StrokeCap.Butt)
                            )
                        }
                        inicio += barrido
                    }
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(total.toInt().toString(), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                Text(textoCentro, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            datos.forEachIndexed { i, d ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(10.dp).background(colorDe(d, i, paleta), CircleShape))
                    Spacer(Modifier.width(8.dp))
                    Text(d.etiqueta, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                    Text(d.valor.toInt().toString(), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
