package com.gutierrez.centrotareas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gutierrez.centrotareas.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(viewModel: TareasViewModel) {
    val mensaje by viewModel.mensaje.collectAsState()
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
                Button(onClick = { viewModel.tareaLarga() }) { Text("Tarea larga") }
                // TODO Pasos 3 y 4: progreso, porcentaje y cancelar.
            }
            TarjetaTarea("Contar números primos") {
                Text("Pendiente de implementar en el paso 5")
                // TODO Paso 5: campo numérico, Contar, Cancelar y estado primos.
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