package com.gutierrez.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height

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
import com.gutierrez.huellitasvet.ui.components.CalendarioMes
import com.gutierrez.huellitasvet.ui.components.EstadoCitaChip
import com.gutierrez.huellitasvet.ui.components.MascotaAvatar
import com.gutierrez.huellitasvet.viewmodel.AgendaViewModel

@Composable
fun AgendaScreen(vm: AgendaViewModel, onAtender: (Int) -> Unit) {
    val fecha by vm.fechaSeleccionada.collectAsState()
    val mesVisible by vm.mesVisible.collectAsState()
    val diasConCitas by vm.diasConCitas.collectAsState()
    val diasMes by vm.diasMes.collectAsState()
    val citas by vm.citas.collectAsState()

    Scaffold(
        topBar = { AppTopBar("Agenda") },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                CalendarioMes(
                    mesVisible = mesVisible,
                    fechaSeleccionada = fecha,
                    diasConCitas = diasConCitas,
                    diasMes = diasMes,
                    onSeleccionar = { vm.seleccionar(it) },
                    onMesAnterior = { vm.mesAnterior() },
                    onMesSiguiente = { vm.mesSiguiente() }
                )
            }
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

/** Linea de tiempo por hora con una tarjeta (avatar, servicio, dueño y estado). */
@Composable
private fun ItemAgenda(
    c: CitaConDetalle,
    onConfirmar: () -> Unit,
    onAtender: () -> Unit,
    onCancelar: () -> Unit
) {
    val estado = EstadoCita.valueOf(c.cita.estado)
    Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min), verticalAlignment = Alignment.Top) {
        Text(
            c.cita.hora, Modifier.width(52.dp).padding(top = 14.dp),
            color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelLarge
        )
        Box(
            Modifier.width(2.dp).fillMaxHeight()
                .background(MaterialTheme.colorScheme.primaryContainer)
        )
        Spacer(Modifier.width(10.dp))
        Card(
            Modifier.weight(1f), shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
        ) {
            Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MascotaAvatar(c.mascota.mascota.especie, tamano = 48.dp)
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("${c.mascota.mascota.nombre} - ${c.servicio.nombre}", fontWeight = FontWeight.Bold)
                    Text(
                        "Dueño: ${c.mascota.dueno.nombre}", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        EstadoCitaChip(estado)
                        when (estado) {
                            EstadoCita.PENDIENTE -> Button(onClick = onConfirmar, shape = RoundedCornerShape(20.dp)) { Text("Confirmar") }
                            EstadoCita.CONFIRMADA -> Button(onClick = onAtender, shape = RoundedCornerShape(20.dp)) { Text("Atender") }
                            else -> {}
                        }
                    }
                    if (estado == EstadoCita.PENDIENTE || estado == EstadoCita.CONFIRMADA) {
                        TextButton(onClick = onCancelar, modifier = Modifier.align(Alignment.End)) {
                            Text("Cancelar", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}
