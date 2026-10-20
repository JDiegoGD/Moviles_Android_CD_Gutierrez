package com.gutierrez.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gutierrez.huellitasvet.data.local.entity.Cita
import com.gutierrez.huellitasvet.model.EstadoCita
import com.gutierrez.huellitasvet.model.Formato
import com.gutierrez.huellitasvet.ui.components.AppTopBar
import com.gutierrez.huellitasvet.ui.components.CitaCard
import com.gutierrez.huellitasvet.ui.components.OpcionChip
import com.gutierrez.huellitasvet.viewmodel.MisCitasViewModel

@Composable
fun MisCitasScreen(
    vm: MisCitasViewModel,
    onPedirCita: () -> Unit,
    onReprogramarCita: (Int) -> Unit = {}
) {
    val proximas by vm.proximas.collectAsState()
    val historial by vm.historial.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    var pestana by remember { mutableStateOf(0) }
    var citaACancelar by remember { mutableStateOf<Cita?>(null) }
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(mensaje) {
        mensaje?.let { snackbar.showSnackbar(it); vm.limpiarMensaje() }
    }

    Scaffold(
        topBar = { AppTopBar("Mis citas") },
        snackbarHost = { SnackbarHost(snackbar) },
        floatingActionButton = {
            FloatingActionButton(onClick = onPedirCita) { Icon(Icons.Filled.Add, contentDescription = "Pedir cita") }
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Column(Modifier.padding(padding)) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OpcionChip("Próximas", pestana == 0, { pestana = 0 }, Modifier.weight(1f))
                OpcionChip("Historial", pestana == 1, { pestana = 1 }, Modifier.weight(1f))
            }

            val lista = if (pestana == 0) proximas else historial
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 88.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (lista.isEmpty()) {
                    item {
                        Text(
                            if (pestana == 0) "No tienes citas próximas" else "Aún no tienes citas en el historial",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
                items(lista, key = { it.cita.id }) { c ->
                    val estado = EstadoCita.valueOf(c.cita.estado)
                    val puedeReprogramar = pestana == 0 && vm.puedeReprogramar(c.cita)
                    val puedeCancelar = pestana == 0 && vm.puedeCancelar(c.cita)
                    CitaCard(
                        servicio = c.servicio.nombre,
                        icono = c.servicio.icono,
                        detalle = "${c.mascota.mascota.nombre} · ${Formato.fechaHora(c.cita.fecha, c.cita.hora)}",
                        estado = estado
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            if (puedeReprogramar) {
                                TextButton(onClick = { onReprogramarCita(c.cita.id) }) {
                                    Text("Reprogramar", fontWeight = FontWeight.Bold)
                                }
                            }
                            if (puedeCancelar) {
                                TextButton(onClick = { citaACancelar = c.cita }) {
                                    Text("Cancelar", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                                }
                            } else if (pestana == 0) {
                                Text(
                                    "Ya no puedes modificar esta cita",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
                if (pestana == 0 && proximas.isNotEmpty()) {
                    item {
                        Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.fillMaxWidth()) {
                            Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Schedule, null, tint = MaterialTheme.colorScheme.primary)
                                Text("  Puedes cancelar hasta 2 horas antes", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }

        citaACancelar?.let { cita ->
            AlertDialog(
                onDismissRequest = { citaACancelar = null },
                title = { Text("Cancelar cita") },
                text = { Text("¿Seguro que quieres cancelar esta cita?") },
                confirmButton = {
                    TextButton(onClick = { vm.cancelar(cita); citaACancelar = null }) {
                        Text("Sí, cancelar", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = { TextButton(onClick = { citaACancelar = null }) { Text("No") } }
            )
        }
    }
}
