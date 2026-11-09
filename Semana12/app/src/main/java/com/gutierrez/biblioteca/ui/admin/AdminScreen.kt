package com.gutierrez.biblioteca.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gutierrez.biblioteca.ui.components.ErrorView
import com.gutierrez.biblioteca.ui.theme.BibliotecaTheme

/** Panel Admin: totales, sincronizar y limpiar cache. Solo observa el estado del ViewModel. */
@Composable
fun AdminScreen(
    viewModel: AdminViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    AdminContenido(
        estado = estado,
        onSincronizar = viewModel::sincronizar,
        onLimpiarCache = viewModel::limpiarCache,
        onVolver = onVolver,
        modifier = modifier
    )
}

/** Contenido sin ViewModel: facil de previsualizar y de probar. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminContenido(
    estado: AdminUiState,
    onSincronizar: () -> Unit,
    onLimpiarCache: () -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    var confirmarLimpieza by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Panel Admin") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (!estado.permitido) {
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                ErrorView(mensaje = "No tienes permiso para ver esta pantalla.")
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Estadistica(titulo = "Libros guardados", valor = estado.totalLibros)
                Estadistica(titulo = "Favoritos", valor = estado.totalFavoritos)

                if (estado.ocupado) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
                Button(
                    onClick = onSincronizar,
                    enabled = !estado.ocupado,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sincronizar todas las carreras")
                }
                OutlinedButton(
                    onClick = { confirmarLimpieza = true },
                    enabled = !estado.ocupado,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Limpiar caché")
                }
                estado.mensaje?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium) }
            }
        }
    }

    if (confirmarLimpieza) {
        AlertDialog(
            onDismissRequest = { confirmarLimpieza = false },
            title = { Text("Limpiar caché") },
            text = { Text("Se borrarán los libros descargados. Los favoritos se conservan.") },
            confirmButton = {
                TextButton(onClick = {
                    confirmarLimpieza = false
                    onLimpiarCache()
                }) { Text("Limpiar") }
            },
            dismissButton = {
                TextButton(onClick = { confirmarLimpieza = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun Estadistica(titulo: String, valor: Int) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = titulo, style = MaterialTheme.typography.titleSmall)
            Text(text = valor.toString(), style = MaterialTheme.typography.headlineMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AdminContenidoPreview() {
    BibliotecaTheme {
        AdminContenido(
            estado = AdminUiState(permitido = true, totalLibros = 120, totalFavoritos = 7),
            onSincronizar = {},
            onLimpiarCache = {},
            onVolver = {}
        )
    }
}
