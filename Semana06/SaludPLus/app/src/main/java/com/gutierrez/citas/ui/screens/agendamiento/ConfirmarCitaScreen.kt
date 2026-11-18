package com.gutierrez.citas.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gutierrez.citas.data.repository.Repositorio
import com.gutierrez.citas.ui.components.BarraSuperior
import com.gutierrez.citas.ui.components.BotonPrimario
import com.gutierrez.citas.ui.components.FilaDato
import com.gutierrez.citas.ui.components.TarjetaMedico
import com.gutierrez.citas.ui.theme.AzulClinica
import com.gutierrez.citas.ui.theme.AzulNoche
import com.gutierrez.citas.ui.theme.GrisMedio
import com.gutierrez.citas.ui.theme.RojoAlerta
import com.gutierrez.citas.util.fechaLargaEs
import com.gutierrez.citas.util.rangoDeHora

// Pantalla 7 · Resumen de la cita elegida; aquí recién se guarda en el Repositorio
@Composable
fun ConfirmarCitaScreen(
    medicoId: Int,
    fecha: String,
    hora: String,
    onAtras: () -> Unit,
    onConfirmada: () -> Unit
) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val especialidad = medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }
    val localNombre = Repositorio.localElegido?.nombre ?: "Sede Los Olivos"

    // El motivo es opcional y solo se escribe; el modelo Cita no lo guarda
    var motivo by rememberSaveable { mutableStateOf("") }
    var horarioOcupado by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .imePadding()
    ) {
        BarraSuperior(titulo = "Confirmar cita", onAtras = onAtras)

        if (medico == null || especialidad == null) {
            Text(
                text = "No encontramos al médico de esta cita",
                style = MaterialTheme.typography.bodyLarge,
                color = GrisMedio,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            )
        } else {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TarjetaMedico(
                    medico = medico,
                    especialidad = especialidad.nombre,
                    detalle = "CMP: ${40000 + medico.id * 311}"
                )

                // Resumen de la cita, una línea por dato
                Column {
                    FilaDato(
                        etiqueta = "Fecha",
                        valor = fechaLargaEs(fecha),
                        icono = Icons.Filled.Event,
                        conLinea = true
                    )
                    FilaDato(
                        etiqueta = "Hora",
                        valor = rangoDeHora(hora),
                        icono = Icons.Filled.AccessTime,
                        conLinea = true
                    )
                    FilaDato(
                        etiqueta = "Tipo de atención",
                        valor = "Consulta presencial",
                        icono = Icons.Filled.MedicalServices,
                        conLinea = true
                    )
                    FilaDato(
                        etiqueta = "Local",
                        valor = localNombre,
                        icono = Icons.Filled.LocationOn
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Motivo de consulta",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulNoche
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "(opcional)", fontSize = 13.sp, color = GrisMedio)
                }
                OutlinedTextField(
                    value = motivo,
                    onValueChange = { motivo = it },
                    placeholder = { Text("Ej. Consulta de rutina", color = Color(0xFF9CA3AF)) },
                    minLines = 3,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = AzulClinica,
                        unfocusedBorderColor = Color(0xFFD5DEEC),
                        cursorColor = AzulClinica
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                if (horarioOcupado) {
                    Text(
                        text = "Ese horario acaba de ser reservado. Vuelve atrás y elige otro.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = RojoAlerta
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            BotonPrimario(
                texto = "Confirmar cita",
                onClick = {
                    val agendada = Repositorio.agendarCita(
                        medicoId = medico.id,
                        especialidadId = medico.especialidadId,
                        fecha = fecha,
                        hora = hora
                    )
                    if (agendada) onConfirmada() else horarioOcupado = true
                },
                modifier = Modifier.padding(20.dp)
            )
        }
    }
}
