package com.gutierrez.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import com.gutierrez.huellitasvet.data.local.entity.Mascota
import com.gutierrez.huellitasvet.model.Especie
import com.gutierrez.huellitasvet.model.Sexo
import com.gutierrez.huellitasvet.ui.components.AppTopBar
import com.gutierrez.huellitasvet.ui.components.MascotaAvatar
import com.gutierrez.huellitasvet.ui.components.OpcionChip
import com.gutierrez.huellitasvet.ui.components.VetButton
import com.gutierrez.huellitasvet.ui.components.VetTextField
import com.gutierrez.huellitasvet.ui.components.etiquetaEspecie
import com.gutierrez.huellitasvet.viewmodel.MascotaViewModel

/** Sirve para crear (id = null) y para editar (id de la mascota). */
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
    var especie by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("") }

    // Al editar, rellena los campos una sola vez cuando llega la mascota
    LaunchedEffect(existente?.id) {
        existente?.let {
            nombre = it.nombre; especie = it.especie; raza = it.raza
            anio = it.anioNacimiento.toString(); peso = it.peso.toString(); sexo = it.sexo
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
                    OpcionChip(etiquetaEspecie(e.name), especie == e.name, { especie = e.name }, Modifier.weight(1f))
                }
            }
            errores["especie"]?.let { TextoError(it) }

            VetTextField(raza, { raza = it }, "Raza")
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                VetTextField(
                    anio, { anio = it.filter(Char::isDigit).take(4) }, "Año de nacimiento",
                    Modifier.weight(1f), error = errores["anio"], teclado = KeyboardType.Number
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
                val mascota = Mascota(
                    id = id ?: 0,
                    duenoId = existente?.duenoId ?: duenoId,
                    nombre = nombre,
                    especie = especie,
                    raza = raza,
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

@Composable
private fun TextoError(texto: String) {
    Text(texto, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
}
