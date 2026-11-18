package com.gutierrez.huellitasvet.ui.veterinario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gutierrez.huellitasvet.data.local.entity.HorarioAtencion
import com.gutierrez.huellitasvet.model.Horarios
import com.gutierrez.huellitasvet.ui.components.AppTopBar
import com.gutierrez.huellitasvet.viewmodel.HorarioViewModel

/** El veterinario define que dias y a que horas se pueden reservar citas. */
@Composable
fun HorarioAtencionScreen(vm: HorarioViewModel, onBack: () -> Unit) {
    val horario by vm.horario.collectAsState()

    Scaffold(
        topBar = { AppTopBar("Horario de atención", onBack) },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Los dueños solo podrán pedir citas en los días y horas que configures aquí. Cada cita dura 30 minutos.",
                    Modifier.padding(12.dp), style = MaterialTheme.typography.bodySmall
                )
            }
            horario.forEach { h -> TarjetaDia(h, vm) }
        }
    }
}

@Composable
private fun TarjetaDia(h: HorarioAtencion, vm: HorarioViewModel) {
    Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surface, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(Horarios.nombresDias[h.diaSemana - 1], fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text(
                        if (h.abierto) "Abierto" else "Cerrado",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (h.abierto) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(checked = h.abierto, onCheckedChange = { vm.cambiarAbierto(h, it) })
            }
            if (h.abierto) {
                FilaHora("Apertura", h.apertura, { vm.moverApertura(h, -30) }, { vm.moverApertura(h, 30) })
                FilaHora("Cierre", h.cierre, { vm.moverCierre(h, -30) }, { vm.moverCierre(h, 30) })
                FilaHora("Inicio descanso", h.descansoInicio, { vm.moverDescansoInicio(h, -30) }, { vm.moverDescansoInicio(h, 30) })
                FilaHora("Fin descanso", h.descansoFin, { vm.moverDescansoFin(h, -30) }, { vm.moverDescansoFin(h, 30) })
                Text(
                    if (h.descansoInicio == h.descansoFin) "Sin descanso" else "Sin citas de ${h.descansoInicio} a ${h.descansoFin}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/** Una hora con botones − y + para moverla de 30 en 30 minutos. */
@Composable
private fun FilaHora(titulo: String, hora: String, onMenos: () -> Unit, onMas: () -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(titulo, Modifier.weight(1f))
        TextButton(onClick = onMenos) { Text("−") }
        Text(hora, fontWeight = FontWeight.SemiBold)
        TextButton(onClick = onMas) { Text("+") }
    }
}
