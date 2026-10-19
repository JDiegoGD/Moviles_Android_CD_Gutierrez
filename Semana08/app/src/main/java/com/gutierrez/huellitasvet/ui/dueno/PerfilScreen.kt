package com.gutierrez.huellitasvet.ui.dueno

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gutierrez.huellitasvet.data.local.entity.Usuario
import com.gutierrez.huellitasvet.ui.components.AppTopBar
import com.gutierrez.huellitasvet.ui.components.FilaSwitch
import com.gutierrez.huellitasvet.ui.components.VetButton

@Composable
fun PerfilScreen(
    usuario: Usuario,
    mascotas: Int,
    proximas: Int,
    atenciones: Int,
    oscuro: Boolean,
    notificaciones: Boolean,
    onOscuro: (Boolean) -> Unit,
    onNotificaciones: (Boolean) -> Unit,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    Scaffold(
        topBar = { AppTopBar("Perfil") },
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
                            Text(
                                usuario.nombre.take(1).uppercase(), fontSize = 22.sp,
                                fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Column {
                        Text(usuario.nombre, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text(usuario.correo, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            // Contadores
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Contador("Mascotas", mascotas, Modifier.weight(1f))
                Contador("Citas próximas", proximas, Modifier.weight(1f))
                Contador("Atenciones", atenciones, Modifier.weight(1f))
            }

            VetButton("Cambiar de perfil", onClick = onCambiarPerfil)

            Text("Preferencias", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surface, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(horizontal = 14.dp, vertical = 6.dp)) {
                    FilaSwitch("Modo oscuro", "Cambia el tema de la app", oscuro, onOscuro)
                    FilaSwitch("Recordatorios", "Avisar antes de mis citas", notificaciones, onNotificaciones)
                }
            }

            VetButton("Cerrar sesión", onClick = onCerrarSesion, secundario = true, peligro = true)
        }
    }
}

@Composable
private fun Contador(titulo: String, valor: Int, modifier: Modifier) {
    Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.primaryContainer, modifier = modifier) {
        Column(Modifier.padding(vertical = 14.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(valor.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(titulo, style = MaterialTheme.typography.labelSmall)
        }
    }
}
