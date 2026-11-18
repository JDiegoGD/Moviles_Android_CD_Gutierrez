package com.gutierrez.centrotareas.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.gutierrez.centrotareas.model.CargaUi
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
    val carga by viewModel.carga.collectAsState()
    val simularError by viewModel.simularError.collectAsState()
    var numero by rememberSaveable { mutableStateOf("500000") }
    var toques by rememberSaveable { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CentroTareas", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Botón Tócame centrado debajo de la barra superior
            Button(
                onClick = { toques++ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Tócame: $toques")
            }

            // Tarjeta 1: Tarea con progreso y cancelación
            TarjetaTarea("Tarea con progreso") {
                Text(mensaje, style = MaterialTheme.typography.bodyLarge)
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
                Text("${(progreso * 100).toInt()} %", style = MaterialTheme.typography.bodyMedium)
            }

            // Tarjeta 2: Conteo de números primos con cancelación
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
                    is PrimosUi.Calculando -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Calculando...")
                                Text("${(estado.progreso * 100).toInt()} %", style = MaterialTheme.typography.bodyMedium)
                            }
                            LinearProgressIndicator(
                                progress = { estado.progreso },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    is PrimosUi.Listo -> Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("Primos encontrados: ${estado.cantidad}", fontWeight = FontWeight.Medium)
                            Text("Tiempo: ${estado.milisegundos} ms")
                        }
                    }
                    is PrimosUi.Cancelado -> Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("Cálculo cancelado (${(estado.progreso * 100).toInt()} %)")
                        LinearProgressIndicator(
                            progress = { estado.progreso },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Tarjeta 3: Cargas simuladas secuenciales y paralelas
            TarjetaTarea("Dos cargas") {
                val cargando = carga is CargaUi.Cargando
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Simular error", fontWeight = FontWeight.Medium)
                    Switch(
                        checked = simularError,
                        onCheckedChange = { viewModel.cambiarSimularError(it) },
                        enabled = !cargando
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = { viewModel.cargarUnaPorUna() },
                        enabled = !cargando
                    ) { Text("Una por una") }
                    Button(
                        onClick = { viewModel.cargarALaVez() },
                        enabled = !cargando
                    ) { Text("A la vez") }
                }
                when (val estado = carga) {
                    CargaUi.Inicial -> Text("Elige cómo cargar los datos ficticios")
                    CargaUi.Cargando -> {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                            Text("Cargando...")
                        }
                    }
                    is CargaUi.Exito -> Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("Modo: ${estado.modo}", fontWeight = FontWeight.Medium)
                            Text("Perfil: ${estado.perfil}")
                            Text("Pedidos: ${estado.pedidos}")
                            Text("Tiempo: ${estado.milisegundos} ms")
                        }
                    }
                    is CargaUi.Error -> Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(estado.mensaje, fontWeight = FontWeight.Medium)
                            Button(
                                onClick = { viewModel.cargarALaVez() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = MaterialTheme.colorScheme.onError
                                )
                            ) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaTarea(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            contenido()
        }
    }
}
