package com.gutierrez.citas.ui.screens.notificaciones

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gutierrez.citas.data.repository.Repositorio
import com.gutierrez.citas.ui.components.BarraSuperior
import com.gutierrez.citas.ui.components.TarjetaBase
import com.gutierrez.citas.ui.theme.AzulClinica
import com.gutierrez.citas.ui.theme.AzulNiebla
import com.gutierrez.citas.ui.theme.AzulNoche
import com.gutierrez.citas.ui.theme.GrisMedio
import com.gutierrez.citas.util.fechaLegible
import com.gutierrez.citas.util.rangoDeHora

// Un aviso ya armado, listo para dibujarse
private data class Aviso(val id: Int, val titulo: String, val detalle: String)

// Pantalla 14 · Notificaciones: un aviso por cada cita del usuario (reto extra)
@Composable
fun NotificacionesScreen(
    onAtras: () -> Unit
) {
    // map convierte cada cita en un aviso con el médico, la fecha y el horario
    val avisos = Repositorio.citasDelUsuario().map { cita ->
        val medico = Repositorio.obtenerMedico(cita.medicoId)
        val especialidad = Repositorio.obtenerEspecialidad(cita.especialidadId)
        Aviso(
            id = cita.id,
            titulo = "Recordatorio de ${especialidad?.nombre ?: "consulta"}",
            detalle = "${medico?.nombre ?: "Tu médico"} te espera el ${fechaLegible(cita.fecha)}, " +
                "de ${rangoDeHora(cita.hora)}."
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
    ) {
        BarraSuperior(titulo = "Notificaciones", onAtras = onAtras)

        if (avisos.isEmpty()) {
            SinAvisos()
        } else {
            LazyColumn(
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(avisos, key = { it.id }) { aviso ->
                    TarjetaAviso(aviso)
                }
            }
        }
    }
}

// Estado vacío: campana dentro de un círculo y dos líneas de texto
@Composable
private fun SinAvisos() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .background(AzulNiebla, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = null,
                tint = AzulClinica,
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "Todo al día",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AzulNoche,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Aquí verás los recordatorios de tus citas",
            fontSize = 14.sp,
            color = GrisMedio,
            textAlign = TextAlign.Center
        )
    }
}

// Tarjeta con una franja azul a la izquierda, la campana y el texto del aviso
@Composable
private fun TarjetaAviso(aviso: Aviso) {
    TarjetaBase(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(AzulClinica)
            )
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(AzulNiebla),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = null,
                        tint = AzulClinica,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = aviso.titulo,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulNoche
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = aviso.detalle,
                        fontSize = 14.sp,
                        color = GrisMedio
                    )
                }
            }
        }
    }
}
