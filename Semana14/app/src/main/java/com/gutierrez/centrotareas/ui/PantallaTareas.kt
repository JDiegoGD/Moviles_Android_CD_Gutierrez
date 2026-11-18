package com.gutierrez.centrotareas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import com.gutierrez.centrotareas.model.PrimosUi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gutierrez.centrotareas.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(viewModel: TareasViewModel) {
    val mensaje by viewModel.mensaje.collectAsState()
    val progreso by viewModel.progreso.collectAsState()
    val corriendo by viewModel.corriendo.collectAsState()
    val primos by viewModel.primos.collectAsState()
    var numero by rememberSaveable { mutableStateOf("500000") }
    var toques by rememberSaveable { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CentroTareas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding)
                .verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = { toques++ }) { Text("Tócame: $toques") }
            TarjetaTarea("Tarea con progreso") {
                Text(mensaje)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { viewModel.tareaLarga() },
                        enabled = !corriendo
                    ) { Text("Iniciar") }
                    OutlinedButton(
                        onClick = { viewModel.cancelar() },
                        enabled = corriendo
                    ) { Text("Cancelar") }
                }
                LinearProgressIndicator(
                    progress = { progreso },
                    modifier = Modifier.fillMaxWidth()
                )
                Text("${(progreso * 100).toInt()} %")
            }
            TarjetaTarea("Contar números primos") {
                val calculando = primos is PrimosUi.Calculando
                val limite = numero.toIntOrNull()?.takeIf { it >= 2 }

                OutlinedTextField(
                    value = numero,
                    onValueChange = { nuevo ->
                        if (nuevo.all { it in '0'..'9' }) numero = nuevo
                    },
                    label = { Text("Número límite (N)") },
                    supportingText = { Text("Ingresa un entero entre 2 y 2147483647") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    enabled = !calculando,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { limite?.let { viewModel.contar(it) } },
                        enabled = !calculando && limite != null
                    ) { Text("Contar") }
                    OutlinedButton(
                        onClick = { viewModel.cancelarPrimos() },
                        enabled = calculando
                    ) { Text("Cancelar") }
                }
                when (val estado = primos) {
                    PrimosUi.Inicial -> Text("Listo para contar")
                    PrimosUi.Calculando -> {
                        CircularProgressIndicator()
                        Text("Calculando...")
                    }
                    is PrimosUi.Listo -> Surface(
                        color = Color(0xFFDDF3E4),
                        contentColor = Color(0xFF164B2B),
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("Primos encontrados: ${estado.cantidad}")
                            Text("Tiempo: ${estado.milisegundos} ms")
                        }
                    }
                    PrimosUi.Cancelado -> Text("Cálculo cancelado")
                }
            }
            TarjetaTarea("Dos cargas") {
                Text("Pendiente de implementar en los pasos 6 y 7")
                // TODO Paso 6: botones Una por una y A la vez, carga y resultado.
                // TODO Paso 7: interruptor de error y Reintentar.
            }
        }
    }
}

@Composable
private fun TarjetaTarea(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(titulo, style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
            contenido()
        }
    }
}