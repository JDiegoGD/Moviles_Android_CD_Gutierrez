package com.gutierrez.huellitasvet.ui.veterinario

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gutierrez.huellitasvet.data.local.relation.CitaConDetalle
import com.gutierrez.huellitasvet.model.EstadoCita
import com.gutierrez.huellitasvet.ui.components.AppTopBar
import com.gutierrez.huellitasvet.ui.components.EstadoCitaChip
import com.gutierrez.huellitasvet.ui.components.FechaChip
import com.gutierrez.huellitasvet.ui.components.MascotaAvatar
import com.gutierrez.huellitasvet.viewmodel.AgendaViewModel

@Composable
fun AgendaScreen(vm: AgendaViewModel, onAtender: (Int) -> Unit) {
    val fecha by vm.fechaSeleccionada.collectAsState()
    val citas by vm.citas.collectAsState()

    Scaffold(
        topBar = { AppTopBar("Agenda") },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Column(Modifier.padding(padding)) {
            Row(
                Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                vm.dias.forEach { d -> FechaChip(d, d == fecha, { vm.seleccionar(d) }) }
            }
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (citas.isEmpty()) {
                    item {
                        Text(
                            "No hay citas para este día",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                items(citas, key = { it.cita.id }) { c ->
                    ItemAgenda(
                        c,
                        onConfirmar = { vm.confirmar(c.cita) },
                        onAtender = { onAtender(c.cita.id) },
                        onCancelar = { vm.cancelar(c.cita) }
                    )
                }
            }
        }
    }
}

/** Linea de tiempo por hora con una tarjeta (avatar, servicio, dueño y estado). */
@Composable
private fun ItemAgenda(
    c: CitaConDetalle,
    onConfirmar: () -> Unit,
    onAtender: () -> Unit,
    onCancelar: () -> Unit
) {
    val estado = EstadoCita.valueOf(c.cita.estado)
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Text(
            c.cita.hora, Modifier.width(52.dp).padding(top = 14.dp),
            color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelLarge
        )
        Card(
            Modifier.weight(1f), shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MascotaAvatar(c.mascota.mascota.especie, tamano = 44.dp)
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text("${c.mascota.mascota.nombre} · ${c.servicio.nombre}", fontWeight = FontWeight.Bold)
                    Text(
                        "Dueño: ${c.mascota.dueno.nombre}", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    EstadoCitaChip(estado)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        when (estado) {
                            EstadoCita.PENDIENTE -> Button(onClick = onConfirmar) { Text("Confirmar") }
                            EstadoCita.CONFIRMADA -> Button(onClick = onAtender) { Text("Atender") }
                            else -> {}
                        }
                        if (estado == EstadoCita.PENDIENTE || estado == EstadoCita.CONFIRMADA) {
                            TextButton(onClick = onCancelar) {
                                Text("Cancelar", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}
