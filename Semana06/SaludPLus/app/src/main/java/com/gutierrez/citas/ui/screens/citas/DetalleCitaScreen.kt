package com.gutierrez.citas.ui.screens.citas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gutierrez.citas.data.repository.Repositorio
import com.gutierrez.citas.ui.components.BarraSuperior
import com.gutierrez.citas.ui.components.FotoMedico
import com.gutierrez.citas.ui.components.TarjetaBase
import com.gutierrez.citas.ui.theme.AzulClinica
import com.gutierrez.citas.ui.theme.AzulNiebla
import com.gutierrez.citas.ui.theme.AzulNoche
import com.gutierrez.citas.ui.theme.GrisMedio
import com.gutierrez.citas.ui.theme.RojoAlerta
import com.gutierrez.citas.util.codigoDeCita
import com.gutierrez.citas.util.fechaLegible
import com.gutierrez.citas.util.rangoDeHora

private val FondoReservada = Color(0xFFDDF7E8)
private val VerdeReservada = Color(0xFF1E9E5A)
private val FondoCancelar = Color(0xFFFDECEC)
private val LineaPaso = Color(0xFFD5DEEC)

// Pantalla 12 · Detalle de una cita: médico al centro y los datos como una línea de pasos
@Composable
fun DetalleCitaScreen(
    citaId: Int,
    onAtras: () -> Unit,
    onCancelada: () -> Unit
) {
    // Se guarda al abrir la pantalla: al cancelar, la cita desaparece del Repositorio pero la
    // pantalla no debe mostrar el aviso de "no encontrada" mientras se cierra
    val cita = remember(citaId) { Repositorio.obtenerCita(citaId) }
    val medico = cita?.let { Repositorio.obtenerMedico(it.medicoId) }
    val especialidad = cita?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }
    val local = cita?.let { Repositorio.obtenerLocal(it.localId) }
    val localTexto = if (local != null) "${local.nombre}, ${local.direccion}" else "Sede Los Olivos, Av. Los Olivos 123, Los Olivos"
    var mostrarDialogo by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
    ) {
        BarraSuperior(titulo = "Tu cita", onAtras = onAtras)

        if (cita == null || medico == null || especialidad == null) {
            Text(
                text = "No encontramos esta cita",
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Médico centrado, con su estado y el código de la cita
                FotoMedico(nombre = medico.nombre, foto = medico.foto, tamano = 84.dp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = medico.nombre,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AzulNoche,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = especialidad.nombre,
                    fontSize = 14.sp,
                    color = GrisMedio
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Reservada",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdeReservada,
                        modifier = Modifier
                            .background(FondoReservada, RoundedCornerShape(50))
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                    Text(
                        text = codigoDeCita(cita.id),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulClinica,
                        modifier = Modifier
                            .background(AzulNiebla, RoundedCornerShape(50))
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                TarjetaBase(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Paso(Icons.Filled.Event, "Día", fechaLegible(cita.fecha), mostrarLinea = true)
                        Paso(Icons.Filled.AccessTime, "Horario", rangoDeHora(cita.hora), mostrarLinea = true)
                        Paso(Icons.Filled.MedicalServices, "Modalidad", "Presencial", mostrarLinea = true)
                        Paso(
                            Icons.Filled.LocationOn,
                            "Sede",
                            localTexto,
                            mostrarLinea = false
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
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
            title = { Text("¿Seguro que quieres cancelarla?", color = AzulNoche, fontWeight = FontWeight.Bold) },
            text = {
                Text("Perderás este horario, pero podrás reservar otro cuando quieras.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogo = false
                        Repositorio.cancelarCita(citaId)
                        onCancelada()
                    }
                ) {
                    Text("Cancelar cita", color = RojoAlerta, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Volver")
                }
            }
        )
    }
}

// Un dato de la cita: círculo con ícono y, si no es el último, una línea que lo une con el siguiente
@Composable
private fun Paso(
    icono: ImageVector,
    titulo: String,
    valor: String,
    mostrarLinea: Boolean
) {
    Row {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(AzulNiebla, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = AzulClinica,
                    modifier = Modifier.size(20.dp)
                )
            }
            if (mostrarLinea) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(22.dp)
                        .background(LineaPaso)
                )
            }
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.padding(top = 2.dp)) {
            Text(text = titulo, fontSize = 12.sp, color = GrisMedio)
            Text(
                text = valor,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = AzulNoche
            )
        }
    }
}
