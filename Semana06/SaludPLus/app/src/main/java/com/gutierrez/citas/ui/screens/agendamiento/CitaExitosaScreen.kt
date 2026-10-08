package com.gutierrez.citas.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gutierrez.citas.data.repository.Repositorio
import com.gutierrez.citas.ui.components.BotonPrimario
import com.gutierrez.citas.ui.components.EnlaceTexto
import com.gutierrez.citas.ui.components.FotoMedico
import com.gutierrez.citas.ui.components.TarjetaBase
import com.gutierrez.citas.ui.theme.AzulClinica
import com.gutierrez.citas.ui.theme.AzulNiebla
import com.gutierrez.citas.ui.theme.AzulNoche
import com.gutierrez.citas.ui.theme.GrisMedio
import com.gutierrez.citas.util.codigoDeCita
import com.gutierrez.citas.util.fechaCorta
import com.gutierrez.citas.util.rangoDeHora

private val VerdeReserva = Color(0xFF1E9E5A)
private val LineaPunteada = Color(0xFFC3CFE4)

// Pantalla 9 · Reserva lista: se muestra como un comprobante con el código de la cita
@Composable
fun CitaExitosaScreen(
    onVerMisCitas: () -> Unit,
    onIrInicio: () -> Unit
) {
    // La cita recién creada es la de id más alto del usuario
    val cita = Repositorio.citasDelUsuario().maxByOrNull { it.id }
    val medico = cita?.let { Repositorio.obtenerMedico(it.medicoId) }
    val especialidad = cita?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(28.dp))
        Box(
            modifier = Modifier
                .size(68.dp)
                .background(VerdeReserva, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(38.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "¡Reserva lista!",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AzulNoche
        )
        Text(
            text = "Te esperamos. Llega 10 minutos antes de tu hora.",
            fontSize = 14.sp,
            color = GrisMedio,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (cita != null && medico != null && especialidad != null) {
            // Comprobante: cabecera con el código, línea punteada y los datos de la consulta
            TarjetaBase(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AzulNiebla)
                            .padding(horizontal = 18.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "COMPROBANTE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GrisMedio,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = codigoDeCita(cita.id),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AzulClinica
                        )
                    }

                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FotoMedico(nombre = medico.nombre, foto = medico.foto, tamano = 52.dp)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = medico.nombre,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = AzulNoche
                            )
                            Text(
                                text = especialidad.nombre,
                                fontSize = 13.sp,
                                color = GrisMedio
                            )
                        }
                    }

                    // Línea punteada que separa al médico de la fecha y la hora
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .padding(horizontal = 18.dp)
                            .drawBehind {
                                drawLine(
                                    color = LineaPunteada,
                                    start = Offset(0f, 0f),
                                    end = Offset(size.width, 0f),
                                    strokeWidth = 2f,
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f))
                                )
                            }
                    )

                    Row(modifier = Modifier.padding(18.dp)) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Día", fontSize = 12.sp, color = GrisMedio)
                            Text(
                                text = fechaCorta(cita.fecha),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = AzulNoche
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Horario", fontSize = 12.sp, color = GrisMedio)
                            Text(
                                text = rangoDeHora(cita.hora),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = AzulNoche
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        BotonPrimario(texto = "Ver mis citas", onClick = onVerMisCitas)
        Spacer(modifier = Modifier.height(8.dp))
        EnlaceTexto(texto = "Ir al inicio", onClick = onIrInicio)
    }
}
