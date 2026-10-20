package com.gutierrez.huellitasvet.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import java.util.Locale

/** Gráfico de línea con Canvas para la evolución de peso de la mascota. */
@Composable
fun GraficoLineas(
    datos: List<DatoGrafico>,
    modifier: Modifier = Modifier
) {
    if (datos.size < 2) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(120.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Aún no hay suficientes datos",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    val textMeasurer = rememberTextMeasurer()
    val colorLinea = MaterialTheme.colorScheme.primary
    val colorPuntoInterior = MaterialTheme.colorScheme.surface
    val colorTextoEtiqueta = MaterialTheme.colorScheme.onSurfaceVariant
    val colorTextoValor = MaterialTheme.colorScheme.primary
    val colorGuia = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    val estiloLabel = MaterialTheme.typography.labelSmall

    val minValor = datos.minOf { it.valor }
    val maxValor = datos.maxOf { it.valor }
    val rangoBase = (maxValor - minValor).coerceAtLeast(0.5f)
    val minY = minValor - (rangoBase * 0.15f)
    val maxY = maxValor + (rangoBase * 0.15f)
    val rangoEfectivo = maxY - minY

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(vertical = 8.dp)
    ) {
        val padHorizontal = 36.dp.toPx()
        val padTop = 28.dp.toPx()
        val padBottom = 28.dp.toPx()

        val anchoDisponible = size.width - (padHorizontal * 2)
        val altoDisponible = size.height - padTop - padBottom

        // Puntos calculados en pantalla (x, y)
        val puntos = datos.mapIndexed { index, d ->
            val x = padHorizontal + (index.toFloat() / (datos.size - 1)) * anchoDisponible
            val normY = (d.valor - minY) / rangoEfectivo
            val y = padTop + altoDisponible * (1f - normY)
            Offset(x, y)
        }

        // Línea horizontal de guía en la base
        drawLine(
            color = colorGuia,
            start = Offset(padHorizontal, padTop + altoDisponible),
            end = Offset(size.width - padHorizontal, padTop + altoDisponible),
            strokeWidth = 1.dp.toPx()
        )

        // Dibujar el trazo conectando los puntos
        val path = Path().apply {
            moveTo(puntos[0].x, puntos[0].y)
            for (i in 1 until puntos.size) {
                lineTo(puntos[i].x, puntos[i].y)
            }
        }
        drawPath(
            path = path,
            color = colorLinea,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Dibujar los puntos y etiquetas
        puntos.forEachIndexed { i, pt ->
            val d = datos[i]

            // Círculo exterior e interior
            drawCircle(
                color = colorLinea,
                radius = 5.dp.toPx(),
                center = pt
            )
            drawCircle(
                color = colorPuntoInterior,
                radius = 2.5.dp.toPx(),
                center = pt
            )

            // Texto con valor formateado "X.X kg" encima del punto
            val textoValor = String.format(Locale.US, "%.1f kg", d.valor)
            val layoutValor = textMeasurer.measure(
                text = textoValor,
                style = estiloLabel.copy(color = colorTextoValor, fontWeight = FontWeight.Bold)
            )
            drawText(
                textLayoutResult = layoutValor,
                topLeft = Offset(
                    x = pt.x - layoutValor.size.width / 2f,
                    y = (pt.y - layoutValor.size.height - 4.dp.toPx()).coerceAtLeast(0f)
                )
            )

            // Texto con fecha abajo en el eje X
            val layoutEtiqueta = textMeasurer.measure(
                text = d.etiqueta,
                style = estiloLabel.copy(color = colorTextoEtiqueta)
            )
            drawText(
                textLayoutResult = layoutEtiqueta,
                topLeft = Offset(
                    x = pt.x - layoutEtiqueta.size.width / 2f,
                    y = size.height - layoutEtiqueta.size.height
                )
            )
        }
    }
}
