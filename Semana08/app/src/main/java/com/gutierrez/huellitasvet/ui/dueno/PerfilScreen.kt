package com.gutierrez.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
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
import com.gutierrez.huellitasvet.ui.components.FilaSwitch
import com.gutierrez.huellitasvet.ui.theme.ColorCancelada

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
            Surface(
                shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.surfaceVariant, modifier = Modifier.size(64.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Filled.Person, null, modifier = Modifier.size(36.dp),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(usuario.nombreCompleto, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text(usuario.correo, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Surface(shape = RoundedCornerShape(50), color = MaterialTheme.colorScheme.primaryContainer) {
                            val rolTexto = when (usuario.rol) {
                                Rol.ADMIN.name -> "Veterinario"
                                Rol.ADMINISTRADOR.name -> "Administrador"
                                else -> "Dueño"
                            }
                            Text(
                                rolTexto,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Contadores de colores
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Contador("Mascotas", mascotas, MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                Contador("Próximas", proximas, Color(0xFFFEF3C7), Color(0xFFB45309), Modifier.weight(1f))
                Contador("Atenciones", atenciones, Color(0xFFDBEAFE), Color(0xFF1D4ED8), Modifier.weight(1f))
            }

            Text("Preferencias", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Surface(
                shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(horizontal = 14.dp, vertical = 6.dp)) {
                    FilaSwitch("Modo oscuro", "Activa el tema oscuro en la app", oscuro, onOscuro)
                    FilaSwitch("Recordatorios", "Recibe notificaciones de tus citas", notificaciones, onNotificaciones)
                }
            }

            Surface(shape = RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp)) {
                    Text("Se guarda en el dispositivo", style = MaterialTheme.typography.bodyMedium)
                    Text("con DataStore", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                }
            }

            OutlinedButton(
                onClick = onCerrarSesion,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, ColorCancelada),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = ColorCancelada.copy(alpha = 0.10f),
                    contentColor = ColorCancelada
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, null)
                Text("  Cerrar sesión", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun Contador(titulo: String, valor: Int, fondo: Color, texto: Color, modifier: Modifier) {
    Surface(shape = RoundedCornerShape(16.dp), color = fondo, modifier = modifier) {
        Column(Modifier.padding(vertical = 14.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(valor.toString(), fontSize = 26.sp, fontWeight = FontWeight.Bold, color = texto)
            Text(titulo, style = MaterialTheme.typography.labelMedium, color = texto)
        }
    }
}
