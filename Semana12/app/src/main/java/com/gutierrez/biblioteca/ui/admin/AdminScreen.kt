package com.gutierrez.biblioteca.ui.admin

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
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
    val context = LocalContext.current

    // Android 13+ pide permiso para notificar; se sincroniza aunque lo nieguen (sin aviso).
    val pedirPermiso = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { viewModel.sincronizar() }

    val alSincronizar: () -> Unit = {
        val necesitaPermiso = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        if (necesitaPermiso) {
            pedirPermiso.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            viewModel.sincronizar()
        }
    }

    AdminContenido(
        estado = estado,
        onSincronizar = alSincronizar,
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
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Panel Admin", fontWeight = FontWeight.Bold) },
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
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Estadistica(
                        titulo = "Productos guardados",
                        valor = estado.totalProductos,
                        icono = Icons.Filled.ShoppingCart,
                        modifier = Modifier.weight(1f)
                    )
                    Estadistica(
                        titulo = "Favoritos",
                        valor = estado.totalFavoritos,
                        icono = Icons.Filled.Favorite,
                        modifier = Modifier.weight(1f)
                    )
                }

                if (estado.ocupado) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
                Button(
                    onClick = onSincronizar,
                    enabled = !estado.ocupado,
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Text("Sincronizar todas las categorías")
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
            text = { Text("Se borrarán los productos descargados. Los favoritos se conservan.") },
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
private fun Estadistica(
    titulo: String,
    valor: Int,
    icono: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = valor.toString(),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AdminContenidoPreview() {
    BibliotecaTheme {
        AdminContenido(
            estado = AdminUiState(permitido = true, totalProductos = 120, totalFavoritos = 7),
            onSincronizar = {},
            onLimpiarCache = {},
            onVolver = {}
        )
    }
}
