package com.gutierrez.citas.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.gutierrez.citas.data.repository.Repositorio
import com.gutierrez.citas.ui.components.BotonPrimario
import com.gutierrez.citas.ui.components.EnlaceTexto
import com.gutierrez.citas.ui.components.FilaDato
import com.gutierrez.citas.ui.components.TarjetaBase
import com.gutierrez.citas.ui.theme.AzulNoche
import com.gutierrez.citas.ui.theme.GrisMedio
import com.gutierrez.citas.util.fechaLegible

// Pantalla 9 · Confirmación (vista que no venía en el diseño, hecha con el mismo estilo)
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
        Spacer(modifier = Modifier.height(40.dp))
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color(0xFFDDF7E8), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF1E9E5A),
                modifier = Modifier.size(80.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "¡Cita agendada!",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AzulNoche
        )
        Text(
            text = "Tu cita fue registrada correctamente",
            fontSize = 15.sp,
            color = GrisMedio,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))

        if (cita != null && medico != null && especialidad != null) {
            TarjetaBase(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    FilaDato(etiqueta = "Médico", valor = medico.nombre, icono = Icons.Filled.Person)
                    FilaDato(etiqueta = "Fecha", valor = fechaLegible(cita.fecha), icono = Icons.Filled.Event)
                    FilaDato(etiqueta = "Hora", valor = cita.hora, icono = Icons.Filled.AccessTime)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        BotonPrimario(texto = "Ver mis citas", onClick = onVerMisCitas)
        Spacer(modifier = Modifier.height(8.dp))
        EnlaceTexto(texto = "Volver al inicio", onClick = onIrInicio)
    }
}
