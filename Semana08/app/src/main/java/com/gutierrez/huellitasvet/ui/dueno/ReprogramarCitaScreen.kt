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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gutierrez.huellitasvet.model.Formato
import com.gutierrez.huellitasvet.model.Horarios
import com.gutierrez.huellitasvet.ui.components.AppTopBar
import com.gutierrez.huellitasvet.ui.components.FechaChip
import com.gutierrez.huellitasvet.ui.components.HoraChip
import com.gutierrez.huellitasvet.ui.components.VetButton
import com.gutierrez.huellitasvet.viewmodel.ReprogramarCitaViewModel

@Composable
fun ReprogramarCitaScreen(
    vm: ReprogramarCitaViewModel,
    onBack: () -> Unit,
    onListo: () -> Unit
) {
    val detalle by vm.cita.collectAsState()
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
        val d = detalle ?: return@Scaffold
        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(shape = RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.surfaceVariant, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Cita actual", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${d.mascota.mascota.nombre} · ${d.servicio.nombre}", fontWeight = FontWeight.Bold)
                    Text(Formato.fechaHora(d.cita.fecha, d.cita.hora))
                }
            }

            Titulo("Nueva fecha")
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                vm.dias.forEach { dt -> FechaChip(dt, dt == fecha, { vm.elegirFecha(dt) }) }
            }

            Titulo("Nueva hora")
            Horarios.todos.chunked(4).forEach { fila ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    fila.forEach { h ->
                        HoraChip(
                            hora = h,
                            seleccionada = h == hora,
                            ocupada = h in ocupadas || vm.horaPasada(fecha, h),
                            onClick = { vm.elegirHora(h) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    repeat(4 - fila.size) { Spacer(Modifier.weight(1f)) }
                }
            }

            if (hora != null) {
                Surface(shape = RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Nuevo horario seleccionado", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text(Formato.fechaHora(fecha.toString(), hora!!))
                    }
                }
            }

            VetButton("Confirmar reprogramación", onClick = { vm.confirmar(onListo) }, enabled = puedeConfirmar && hora != null)
        }
    }
}

@Composable
private fun Titulo(texto: String) {
    Text(texto, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
}
