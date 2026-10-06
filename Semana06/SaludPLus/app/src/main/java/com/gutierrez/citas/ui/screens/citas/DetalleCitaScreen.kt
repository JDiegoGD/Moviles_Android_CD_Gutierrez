package com.gutierrez.citas.ui.screens.citas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.gutierrez.citas.ui.components.FilaDato
import com.gutierrez.citas.ui.components.FotoMedico
import com.gutierrez.citas.ui.components.TarjetaBase
import com.gutierrez.citas.ui.theme.AzulNoche
import com.gutierrez.citas.ui.theme.GrisMedio
import com.gutierrez.citas.ui.theme.RojoAlerta
import com.gutierrez.citas.util.fechaLegible
import com.gutierrez.citas.util.rangoDeHora

// Etiqueta verde de "Confirmada" (mismos tonos de la etiqueta de disponibilidad de Médicos)
private val FondoConfirmada = Color(0xFFDDF7E8)
private val VerdeConfirmada = Color(0xFF1E9E5A)
private val FondoCancelar = Color(0xFFFDECEC)

// Pantalla 12 · Detalle de una cita, con opción de cancelarla (reto extra)
@Composable
fun DetalleCitaScreen(
    citaId: Int,
    onAtras: () -> Unit,
    onCancelada: () -> Unit
) {
    // Se guarda al abrir la pantalla: al cancelar, la cita desaparece del Repositorio pero la
    // pantalla no debe mostrar "ya no existe" mientras se cierra
    val cita = remember(citaId) { Repositorio.obtenerCita(citaId) }
    val medico = cita?.let { Repositorio.obtenerMedico(it.medicoId) }
    val especialidad = cita?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
    ) {
        BarraSuperior(titulo = "Detalle de la cita", onAtras = onAtras)

        if (cita == null || medico == null || especialidad == null) {
            // Si el id ya no existe (por ejemplo, ya se canceló) se avisa en vez de cerrar la app
            Text(
                text = "Esta cita ya no existe",
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
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Médico con su etiqueta de estado
                TarjetaBase(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            FotoMedico(nombre = medico.nombre, foto = medico.foto, tamano = 64.dp)
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = medico.nombre,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AzulNoche
                                )
                                Text(
                                    text = especialidad.nombre,
                                    fontSize = 14.sp,
                                    color = GrisMedio
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Confirmada",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = VerdeConfirmada,
                            modifier = Modifier
                                .background(FondoConfirmada, RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Text(
                    text = "Datos de la cita",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulNoche
                )
                TarjetaBase(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        FilaDato(
                            etiqueta = "Fecha",
                            valor = fechaLegible(cita.fecha),
                            icono = Icons.Filled.Event,
                            conLinea = true
                        )
                        FilaDato(
                            etiqueta = "Hora",
                            valor = rangoDeHora(cita.hora),
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
                            etiqueta = "Dirección",
                            valor = "Av. Los Olivos 123, Lima",
                            icono = Icons.Filled.LocationOn
                        )
                    }
                }
            }

            // El botón queda fijo abajo; cancelar siempre pasa primero por el diálogo
            Button(
                onClick = { mostrarDialogo = true },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FondoCancelar,
                    contentColor = RojoAlerta
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(52.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = RojoAlerta,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "  Cancelar cita",
                    fontWeight = FontWeight.SemiBold,
                    color = RojoAlerta
                )
            }
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            containerColor = Color.White,
            title = { Text("¿Cancelar esta cita?", color = AzulNoche, fontWeight = FontWeight.Bold) },
            text = {
                Text("El horario quedará libre otra vez y tendrás que agendar de nuevo si cambias de opinión.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogo = false
                        Repositorio.cancelarCita(citaId)
                        onCancelada()
                    }
                ) {
                    Text("Sí, cancelar", color = RojoAlerta, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("No, mantener")
                }
            }
        )
    }
}
