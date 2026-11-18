package com.gutierrez.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.clickable
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.foundation.shape.RoundedCornerShape
import com.gutierrez.huellitasvet.data.local.entity.Mascota
import com.gutierrez.huellitasvet.model.Especie
import com.gutierrez.huellitasvet.model.Razas
import com.gutierrez.huellitasvet.model.Sexo
import com.gutierrez.huellitasvet.ui.components.AppTopBar
import com.gutierrez.huellitasvet.ui.components.MascotaAvatar
import com.gutierrez.huellitasvet.ui.components.OpcionChip
import com.gutierrez.huellitasvet.ui.components.VetButton
import com.gutierrez.huellitasvet.ui.components.VetTextField
import com.gutierrez.huellitasvet.ui.components.etiquetaEspecie
import com.gutierrez.huellitasvet.viewmodel.MascotaViewModel

/** Sirve para crear (id = null) y para editar (id de la mascota). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioMascotaScreen(
    id: Int?,
    duenoId: Int,
    vm: MascotaViewModel,
    onBack: () -> Unit
) {
    val errores by vm.errores.collectAsState()
    val flujoExistente = remember(id) { vm.mascota(id ?: -1) }
    val existente by flujoExistente.collectAsState(initial = null)

    var nombre by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf(Especie.PERRO.name) }
    var razaSeleccionada by remember { mutableStateOf("") }
    var razaManual by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("") }

    // Al editar, rellena los campos una sola vez cuando llega la mascota
    LaunchedEffect(existente?.id) {
        existente?.let {
            nombre = it.nombre
            especie = it.especie
            val especieEnum = runCatching { Especie.valueOf(it.especie) }.getOrElse { Especie.PERRO }
            val listaRazas = Razas.razasDe(especieEnum)
            if (it.raza in listaRazas) {
                razaSeleccionada = it.raza
                razaManual = ""
            } else {
                razaSeleccionada = "Otra"
                razaManual = it.raza
            }
            anio = it.anioNacimiento.toString()
            peso = it.peso.toString()
            sexo = it.sexo
        }
    }
    DisposableEffect(Unit) { onDispose { vm.limpiarErrores() } }

    Scaffold(
        topBar = { AppTopBar(if (id == null) "Nueva mascota" else "Editar mascota", onBack) },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                MascotaAvatar(especie, tamano = 96.dp) // cambia segun la especie elegida
            }
            VetTextField(nombre, { nombre = it }, "Nombre", error = errores["nombre"])

            Text("Especie", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Especie.values().forEach { e ->
                    OpcionChip(etiquetaEspecie(e.name), especie == e.name, {
                        if (especie != e.name) {
                            especie = e.name
                            razaSeleccionada = ""
                            razaManual = ""
                        }
                    }, Modifier.weight(1f))
                }
            }
            errores["especie"]?.let { TextoError(it) }

            // Selector de raza (ExposedDropdownMenuBox)
            SelectorRaza(
                especie = especie,
                razaSeleccionada = razaSeleccionada,
                razaManual = razaManual,
                onRazaSeleccionada = { razaSeleccionada = it },
                onRazaManualCambio = { razaManual = it },
                error = errores["raza"]
            )

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CampoNacimiento(
                    anio = anio,
                    onAnio = { anio = it },
                    error = errores["anio"],
                    modifier = Modifier.weight(1f)
                )
                VetTextField(
                    peso, { peso = it }, "Peso (kg)",
                    Modifier.weight(1f), error = errores["peso"], teclado = KeyboardType.Decimal
                )
            }

            Text("Sexo", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Sexo.values().forEach { s ->
                    OpcionChip(
                        s.name.lowercase().replaceFirstChar { it.uppercase() },
                        sexo == s.name, { sexo = s.name }, Modifier.weight(1f)
                    )
                }
            }
            errores["sexo"]?.let { TextoError(it) }

            VetButton("Guardar mascota", onClick = {
                val razaFinal = if (razaSeleccionada == "Otra") razaManual.trim() else razaSeleccionada.trim()
                val mascota = Mascota(
                    id = id ?: 0,
                    duenoId = existente?.duenoId ?: duenoId,
                    nombre = nombre,
                    especie = especie,
                    raza = razaFinal,
                    anioNacimiento = anio.toIntOrNull() ?: 0,
                    peso = peso.replace(',', '.').toDoubleOrNull() ?: 0.0,
                    sexo = sexo
                )
                vm.guardar(mascota, onBack)
            })
            VetButton("Cancelar", onClick = onBack, secundario = true)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectorRaza(
    especie: String,
    razaSeleccionada: String,
    razaManual: String,
    onRazaSeleccionada: (String) -> Unit,
    onRazaManualCambio: (String) -> Unit,
    error: String?
) {
    val especieEnum = runCatching { Especie.valueOf(especie) }.getOrElse { Especie.PERRO }
    val listaRazas = Razas.razasDe(especieEnum)
    var expanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = razaSeleccionada,
                onValueChange = {},
                readOnly = true,
                label = { Text("Raza") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                isError = error != null,
                shape = RoundedCornerShape(14.dp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listaRazas.forEach { raza ->
                    DropdownMenuItem(
                        text = { Text(raza) },
                        onClick = {
                            onRazaSeleccionada(raza)
                            expanded = false
                        }
                    )
                }
            }
        }
        if (error != null) {
            Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        if (razaSeleccionada == "Otra") {
            VetTextField(
                valor = razaManual,
                onCambio = onRazaManualCambio,
                etiqueta = "Especifica la raza",
                error = error
            )
        }
    }
}

@Composable
private fun TextoError(texto: String) {
    Text(texto, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
}


/** Campo de solo lectura: al tocarlo abre un calendario y se guarda el año elegido. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CampoNacimiento(
    anio: String,
    onAnio: (String) -> Unit,
    error: String?,
    modifier: Modifier = Modifier
) {
    var abierto by remember { mutableStateOf(false) }

    Box(modifier) {
        OutlinedTextField(
            value = anio,
            onValueChange = {},
            readOnly = true,
            label = { Text("Año de nacimiento") },
            placeholder = { Text("Elegir en calendario") },
            isError = error != null,
            supportingText = if (error != null) {
                { Text(error) }
            } else null,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        )
        // Capa transparente: el campo de solo lectura no recibe clics por si solo
        Box(Modifier.matchParentSize().clickable { abierto = true })
    }

    if (abierto) {
        val hoy = System.currentTimeMillis()
        val anioInicial = anio.toIntOrNull()
        val estado = rememberDatePickerState(
            initialSelectedDateMillis = if (anioInicial != null && anioInicial in 1990..LocalDate.now().year)
                LocalDate.of(anioInicial, 1, 1).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
            else null,
            yearRange = 1990..LocalDate.now().year,
            // No se puede elegir una fecha futura
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis <= hoy
            }
        )
        DatePickerDialog(
            onDismissRequest = { abierto = false },
            confirmButton = {
                TextButton(onClick = {
                    val ms = estado.selectedDateMillis
                    if (ms != null) {
                        onAnio(Instant.ofEpochMilli(ms).atZone(ZoneOffset.UTC).year.toString())
                    }
                    abierto = false
                }) { Text("Aceptar") }
            },
            dismissButton = { TextButton(onClick = { abierto = false }) { Text("Cancelar") } }
        ) {
            DatePicker(state = estado)
        }
    }
}
