package com.gutierrez.huellitasvet.ui.administrador

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gutierrez.huellitasvet.data.local.entity.Usuario
import com.gutierrez.huellitasvet.data.local.entity.nombreCompleto
import com.gutierrez.huellitasvet.model.Rol
import com.gutierrez.huellitasvet.ui.components.AppTopBar
import com.gutierrez.huellitasvet.ui.components.VetButton
import com.gutierrez.huellitasvet.viewmodel.AdminViewModel

@Composable
fun AdminScreen(
    usuario: Usuario,
    vm: AdminViewModel,
    onCerrarSesion: () -> Unit
) {
    val usuarios by vm.usuarios.collectAsState()
    val duenos by vm.totalDuenos.collectAsState()
    val vets by vm.totalVeterinarios.collectAsState()
    val admins by vm.totalAdministradores.collectAsState()
    // Usuario que se quiere quitar (null = ningun dialogo abierto)
    var porQuitar by remember { mutableStateOf<Usuario?>(null) }

    porQuitar?.let { u ->
        AlertDialog(
            onDismissRequest = { porQuitar = null },
            title = { Text("¿Quitar usuario?") },
            text = {
                Text(
                    if (u.rol == Rol.CLIENTE.name)
                        "Se quitará a ${u.nombreCompleto} junto con sus mascotas, citas y vacunas. No se puede deshacer."
                    else
                        "Se quitará a ${u.nombreCompleto}. No se puede deshacer."
                )
            },
            confirmButton = {
                TextButton(onClick = { vm.quitar(u); porQuitar = null }) {
                    Text("Quitar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = { TextButton(onClick = { porQuitar = null }) { Text("Cancelar") } }
        )
    }

    Scaffold(
        topBar = { AppTopBar("Panel de Administrador") },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Ficha del administrador
            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(64.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Filled.AdminPanelSettings,
                                    null,
                                    modifier = Modifier.size(36.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(usuario.nombreCompleto, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Text(usuario.correo, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            if (!usuario.codigoAdmin.isNullOrBlank()) {
                                Text("Código: ${usuario.codigoAdmin}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Contadores de usuarios por rol
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    ContadorRol("Dueños", duenos, MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                    ContadorRol("Veterinarios", vets, Color(0xFFDBEAFE), Color(0xFF1D4ED8), Modifier.weight(1f))
                    ContadorRol("Admins", admins, Color(0xFFEDE9FE), Color(0xFF7C3AED), Modifier.weight(1f))
                }
            }

            item {
                Text("Usuarios registrados", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }

            items(usuarios, key = { it.id }) { u ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                ) {
                    Row(
                        Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Filled.Person, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(u.nombreCompleto, fontWeight = FontWeight.Bold)
                            Text(u.correo, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        val etiquetaRol = when (u.rol) {
                            Rol.CLIENTE.name -> "Dueño"
                            Rol.ADMIN.name -> "Veterinario"
                            else -> "Administrador"
                        }
                        Surface(shape = RoundedCornerShape(50), color = MaterialTheme.colorScheme.primaryContainer) {
                            Text(
                                etiquetaRol,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        // Boton para quitar (no aparece en administradores)
                        if (vm.puedeQuitar(u)) {
                            IconButton(onClick = { porQuitar = u }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Quitar usuario", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }

            item {
                VetButton("Cerrar sesión", onClick = onCerrarSesion, secundario = true, peligro = true)
            }
        }
    }
}

@Composable
private fun ContadorRol(titulo: String, valor: Int, fondo: Color, texto: Color, modifier: Modifier) {
    Surface(shape = RoundedCornerShape(16.dp), color = fondo, modifier = modifier) {
        Column(Modifier.padding(vertical = 14.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(valor.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = texto)
            Text(titulo, style = MaterialTheme.typography.labelSmall, color = texto)
        }
    }
}
