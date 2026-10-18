package com.gutierrez.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import com.gutierrez.huellitasvet.data.local.entity.Mascota
import com.gutierrez.huellitasvet.model.Formato
import com.gutierrez.huellitasvet.ui.components.AppTopBar
import com.gutierrez.huellitasvet.ui.components.HistorialItem
import com.gutierrez.huellitasvet.ui.components.MascotaAvatar
import com.gutierrez.huellitasvet.ui.components.OpcionChip
import com.gutierrez.huellitasvet.ui.components.VetButton
import com.gutierrez.huellitasvet.ui.components.etiquetaEspecie
import com.gutierrez.huellitasvet.ui.components.etiquetaSexo
import com.gutierrez.huellitasvet.viewmodel.MascotaViewModel

/** Ficha de la mascota con pestañas Datos / Historial clinico. */
@Composable
fun FichaMascotaScreen(
    id: Int,
    vm: MascotaViewModel,
    onBack: () -> Unit,
    onPedirCita: (Int) -> Unit,
    onEditar: (Int) -> Unit,
    soloLectura: Boolean = false // el veterinario solo consulta
) {
    val flujo = remember(id) { vm.mascota(id) }
    val mascota by flujo.collectAsState(initial = null)
    val historial by vm.historial(id).collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val snackbar = remember { SnackbarHostState() }
    var pestana by remember { mutableStateOf(0) }
    var confirmarBorrado by remember { mutableStateOf(false) }

    LaunchedEffect(mensaje) {
        mensaje?.let { snackbar.showSnackbar(it); vm.limpiarMensaje() }
    }

    Scaffold(
        topBar = { AppTopBar(mascota?.nombre ?: "Mascota", onBack) },
        snackbarHost = { SnackbarHost(snackbar) },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        val m = mascota ?: return@Scaffold
        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Cabecera
            Card(
                Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    MascotaAvatar(m.especie, tamano = 72.dp)
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(m.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(
                            "${etiquetaEspecie(m.especie)} · ${m.raza} · ${Formato.edad(m.anioNacimiento)} años",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "Peso actual: ${Formato.peso(m.peso)}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            if (!soloLectura) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(onClick = { onPedirCita(m.id) }, Modifier.weight(1f), shape = RoundedCornerShape(14.dp)) {
                        Icon(Icons.Filled.CalendarMonth, null)
                        Text("  Pedir cita", fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(onClick = { onEditar(m.id) }, Modifier.weight(1f), shape = RoundedCornerShape(14.dp)) {
                        Icon(Icons.Filled.Edit, null)
                        Text("  Editar", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OpcionChip("Datos", pestana == 0, { pestana = 0 }, Modifier.weight(1f))
                OpcionChip("Historial clínico", pestana == 1, { pestana = 1 }, Modifier.weight(1f))
            }

            if (pestana == 0) {
                DatosMascota(m)
                if (!soloLectura) {
                    VetButton("Eliminar mascota", onClick = { confirmarBorrado = true }, secundario = true, peligro = true)
                }
            } else {
                if (historial.isEmpty()) {
                    Text(
                        "Aún no tiene atenciones",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                historial.forEachIndexed { i, c ->
                    HistorialItem(
                        fecha = c.cita.fecha,
                        servicio = c.servicio.nombre,
                        diagnostico = c.cita.diagnostico ?: "Sin diagnóstico",
                        peso = c.cita.pesoRegistrado,
                        esUltimo = i == historial.lastIndex
                    )
                }
            }
        }

        if (confirmarBorrado) {
            AlertDialog(
                onDismissRequest = { confirmarBorrado = false },
                title = { Text("Eliminar mascota") },
                text = { Text("¿Seguro que quieres eliminar a ${m.nombre}?") },
                confirmButton = {
                    TextButton(onClick = {
                        confirmarBorrado = false
                        vm.eliminar(m, onBack)
                    }) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
                },
                dismissButton = { TextButton(onClick = { confirmarBorrado = false }) { Text("Cancelar") } }
            )
        }
    }
}

@Composable
private fun DatosMascota(m: Mascota) {
    Card(
        Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Fila("Especie", etiquetaEspecie(m.especie))
            Fila("Raza", m.raza.ifBlank { "—" })
            Fila("Sexo", etiquetaSexo(m.sexo))
            Fila("Año de nacimiento", m.anioNacimiento.toString())
            Fila("Peso", Formato.peso(m.peso))
        }
    }
}

@Composable
private fun Fila(etiqueta: String, valor: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(etiqueta, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(valor, fontWeight = FontWeight.SemiBold)
    }
}
