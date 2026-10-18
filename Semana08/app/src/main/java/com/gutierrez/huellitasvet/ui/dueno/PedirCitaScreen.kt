package com.gutierrez.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Icon
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
import com.gutierrez.huellitasvet.ui.components.iconoServicio
import com.gutierrez.huellitasvet.viewmodel.PedirCitaViewModel

@Composable
fun PedirCitaScreen(
    vm: PedirCitaViewModel,
    onBack: () -> Unit,
    onCreada: () -> Unit
) {
    val mascotas by vm.mascotas.collectAsState()
    val servicios by vm.servicios.collectAsState()
    val mascotaId by vm.mascotaId.collectAsState()
    val servicioId by vm.servicioId.collectAsState()
    val fecha by vm.fecha.collectAsState()
    val hora by vm.hora.collectAsState()
    val ocupadas by vm.horasOcupadas.collectAsState()
    val puedeConfirmar by vm.puedeConfirmar.collectAsState()
    val acepta by vm.aceptaCitas.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(mensaje) {
        mensaje?.let { snackbar.showSnackbar(it); vm.limpiarMensaje() }
    }

    val mascotaElegida = mascotas.firstOrNull { it.id == mascotaId }
    val servicioElegido = servicios.firstOrNull { it.id == servicioId }

    Scaffold(
        topBar = { AppTopBar("Pedir cita", onBack) },
        snackbarHost = { SnackbarHost(snackbar) },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (!acepta) {
                Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.errorContainer) {
                    Text(
                        "La clínica no está recibiendo citas",
                        Modifier.padding(12.dp).fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onErrorContainer, fontWeight = FontWeight.Bold
                    )
                }
            }

            Titulo("¿Quién viene?")
            if (mascotas.isEmpty()) {
                Text("Primero registra una mascota.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                mascotas.forEach { m ->
                    val sel = m.id == mascotaId
                    Surface(
                        onClick = { vm.elegirMascota(m.id) },
                        shape = RoundedCornerShape(50),
                        color = if (sel) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, if (sel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline)
                    ) {
                        Row(Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                            MascotaAvatar(m.especie, tamano = 28.dp)
                            Text("  ${m.nombre}", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            Titulo("Servicio")
            servicios.forEach { s ->
                val sel = s.id == servicioId
                Surface(
                    onClick = { vm.elegirServicio(s.id) },
                    shape = RoundedCornerShape(14.dp),
                    color = if (sel) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, if (sel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(iconoServicio(s.icono), null, tint = MaterialTheme.colorScheme.primary)
                        Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                            Text(s.nombre, fontWeight = FontWeight.Bold)
                            Text(
                                "${s.duracionMin} min", style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(Formato.soles(s.precio), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            Titulo("Fecha")
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                vm.dias.forEach { d -> FechaChip(d, d == fecha, { vm.elegirFecha(d) }) }
            }

            Titulo("Hora")
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

            // Resumen
            if (mascotaElegida != null && servicioElegido != null && hora != null) {
                Surface(shape = RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("${mascotaElegida.nombre} · ${servicioElegido.nombre}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text("${Formato.fechaCorta(fecha.toString())} · $hora · ${Formato.soles(servicioElegido.precio)}")
                    }
                }
            }

            VetButton("Confirmar cita", onClick = { vm.confirmar(onCreada) }, enabled = puedeConfirmar)
        }
    }
}

@Composable
private fun Titulo(texto: String) {
    Text(texto, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
}
