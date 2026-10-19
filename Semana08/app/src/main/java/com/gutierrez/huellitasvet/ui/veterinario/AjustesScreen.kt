package com.gutierrez.huellitasvet.ui.veterinario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gutierrez.huellitasvet.data.local.entity.Usuario
import com.gutierrez.huellitasvet.ui.components.AppTopBar
import com.gutierrez.huellitasvet.ui.components.FilaSwitch
import com.gutierrez.huellitasvet.ui.components.VetButton

@Composable
fun AjustesScreen(
    usuario: Usuario,
    oscuro: Boolean,
    notificaciones: Boolean,
    aceptarCitas: Boolean,
    onOscuro: (Boolean) -> Unit,
    onNotificaciones: (Boolean) -> Unit,
    onAceptarCitas: (Boolean) -> Unit,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    Scaffold(
        topBar = { AppTopBar("Ajustes") },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Datos del usuario
            Surface(shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surface, modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(56.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.Person, null, tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    Column {
                        Text(usuario.nombre, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text(usuario.correo, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("Perfil activo", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            // Cambiar de perfil (modo practica)
            Surface(
                onClick = onCambiarPerfil,
                shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.SwapHoriz, null, tint = MaterialTheme.colorScheme.onPrimary)
                    Column(Modifier.padding(start = 12.dp)) {
                        Text("Cambiar de perfil", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                        Text(
                            "Modo práctica: ir a la vista Dueño",
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Text("Preferencias", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surface, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(horizontal = 14.dp, vertical = 6.dp)) {
                    FilaSwitch("Modo oscuro", "Cambia el tema de la app", oscuro, onOscuro)
                    FilaSwitch("Notificaciones", "Avisar citas nuevas", notificaciones, onNotificaciones)
                    FilaSwitch("Aceptar citas", "Abrir o cerrar la agenda", aceptarCitas, onAceptarCitas)
                }
            }

            Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Estas preferencias se guardan en el dispositivo con DataStore.",
                    Modifier.padding(12.dp), style = MaterialTheme.typography.bodySmall
                )
            }

            VetButton("Cerrar sesión", onClick = onCerrarSesion, secundario = true, peligro = true)
        }
    }
}
