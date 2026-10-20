package com.gutierrez.huellitasvet.ui.dueno

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gutierrez.huellitasvet.model.Formato
import com.gutierrez.huellitasvet.model.Horarios
import com.gutierrez.huellitasvet.ui.components.AppTopBar
import com.gutierrez.huellitasvet.ui.components.FechaChip
import com.gutierrez.huellitasvet.ui.components.HoraChip
import com.gutierrez.huellitasvet.ui.components.MascotaAvatar
import com.gutierrez.huellitasvet.ui.components.VetButton
import com.gutierrez.huellitasvet.viewmodel.ReprogramarCitaViewModel

@Composable
fun ReprogramarCitaScreen(
    vm: ReprogramarCitaViewModel,
    onBack: () -> Unit,
    onListo: () -> Unit
) {
    val cita by vm.cita.collectAsState()
    val fecha by vm.fecha.collectAsState()
    val hora by vm.hora.collectAsState()
    val ocupadas by vm.horasOcupadas.collectAsState()
    val puedeConfirmar by vm.puedeConfirmar.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(mensaje) {
        mensaje?.let { snackbar.showSnackbar(it); vm.limpiarMensaje() }
    }

    Scaffold(
        topBar = { AppTopBar("Reprogramar cita", onBack) },
        snackbarHost = { SnackbarHost(snackbar) },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        val c = cita ?: return@Scaffold
        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Cita actual
            Card(
                Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MascotaAvatar(c.mascota.mascota.especie, tamano = 48.dp)
                    Column(Modifier.weight(1f)) {
                        Text(c.mascota.mascota.nombre, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text(c.servicio.nombre, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            "Horario actual: ${Formato.fechaHora(c.cita.fecha, c.cita.hora)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Text("Nueva fecha", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                vm.dias.forEach { d -> FechaChip(d, d == fecha, { vm.elegirFecha(d) }) }
            }

            Text("Nueva hora", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Horarios.todos.chunked(4).forEach { fila ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    fila.forEach { h ->
                        // Si es el mismo horario de la cita original, no se marca ocupada para permitir seleccionarla/mantenerla
                        val estaOcupada = (h in ocupadas && !(fecha.toString() == c.cita.fecha && h == c.cita.hora)) || vm.horaPasada(fecha, h)
                        HoraChip(
                            hora = h,
                            seleccionada = h == hora,
                            ocupada = estaOcupada,
                            onClick = { vm.elegirHora(h) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    repeat(4 - fila.size) { Spacer(Modifier.weight(1f)) }
                }
            }

            // Resumen de la reprogramación
            if (hora != null) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Nuevo horario seleccionado", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text("${Formato.fechaCorta(fecha.toString())} · $hora")
                    }
                }
            }

            VetButton(
                "Confirmar reprogramación",
                onClick = { vm.confirmar(onListo) },
                enabled = puedeConfirmar
            )
        }
    }
}
