package com.gutierrez.huellitasvet.ui.veterinario

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.gutierrez.huellitasvet.data.local.entity.nombreCompleto
import com.gutierrez.huellitasvet.model.Formato
import com.gutierrez.huellitasvet.ui.components.AppTopBar
import com.gutierrez.huellitasvet.ui.components.MascotaAvatar
import com.gutierrez.huellitasvet.ui.components.OpcionChip
import com.gutierrez.huellitasvet.ui.components.VetButton
import com.gutierrez.huellitasvet.ui.components.VetTextField
import com.gutierrez.huellitasvet.ui.components.etiquetaEspecie
import com.gutierrez.huellitasvet.viewmodel.AtenderCitaViewModel
import java.time.LocalDate

@Composable
fun AtenderCitaScreen(vm: AtenderCitaViewModel, onBack: () -> Unit) {
    val detalle by vm.cita.collectAsState()
    val errores by vm.errores.collectAsState()

    var peso by remember { mutableStateOf("") }
    var diagnostico by remember { mutableStateOf("") }
    var tratamiento by remember { mutableStateOf("") }
    var opcionControl by remember { mutableStateOf("Sin control") }

    // Propone el peso anterior de la mascota (una sola vez, cuando llega la cita)
    LaunchedEffect(detalle?.cita?.id) {
        detalle?.let { peso = it.mascota.mascota.peso.toString() }
    }
    DisposableEffect(Unit) { onDispose { vm.limpiarErrores() } }

    Scaffold(
        topBar = { AppTopBar("Atender cita", onBack) },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        val d = detalle ?: return@Scaffold
        val m = d.mascota.mascota
        val pesoNuevo = peso.replace(',', '.').toDoubleOrNull() ?: 0.0

        val fechaControl = when (opcionControl) {
            "En 7 días" -> LocalDate.now().plusDays(7).toString()
            "En 15 días" -> LocalDate.now().plusDays(15).toString()
            "En 30 días" -> LocalDate.now().plusDays(30).toString()
            else -> null
        }

        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MascotaAvatar(m.especie, tamano = 60.dp)
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("${m.nombre} · ${etiquetaEspecie(m.especie)} ${m.raza}", fontWeight = FontWeight.Bold)
                        Text("Dueño: ${d.mascota.dueno.nombreCompleto}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            "${d.servicio.nombre} · ${Formato.fechaHora(d.cita.fecha, d.cita.hora)}",
                            style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            errores["general"]?.let {
                Text(it, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
            }

            Text("Registro clínico", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                VetTextField(
                    peso, { peso = it }, "Peso (kg)", Modifier.weight(1f),
                    error = errores["peso"], teclado = KeyboardType.Decimal
                )
                Surface(shape = RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.weight(1f)) {
                    Column(Modifier.padding(14.dp)) {
                        Text("Peso anterior", style = MaterialTheme.typography.labelSmall)
                        Text(Formato.peso(m.peso), fontWeight = FontWeight.Bold)
                    }
                }
            }
            VetTextField(diagnostico, { diagnostico = it }, "Diagnóstico", error = errores["diagnostico"], lineas = 3)
            VetTextField(tratamiento, { tratamiento = it }, "Tratamiento", lineas = 3)

            Text("Próximo control", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Row(
                Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Sin control", "En 7 días", "En 15 días", "En 30 días").forEach { opcion ->
                    OpcionChip(
                        texto = opcion,
                        seleccionada = opcionControl == opcion,
                        onClick = { opcionControl = opcion }
                    )
                }
            }
            errores["control"]?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            if (pesoNuevo > 0.0) {
                Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Se actualizará el peso de ${m.nombre} a ${Formato.peso(pesoNuevo)}",
                        Modifier.padding(12.dp), style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            VetButton("Registrar atención", onClick = { vm.registrar(diagnostico, tratamiento, pesoNuevo, fechaControl, onBack) })
            VetButton("Cancelar cita", onClick = { vm.cancelar(onBack) }, secundario = true, peligro = true)
        }
    }
}
