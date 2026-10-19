package com.gutierrez.huellitasvet.ui.veterinario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.gutierrez.huellitasvet.model.Formato
import com.gutierrez.huellitasvet.ui.components.graficos.GraficoBarras
import com.gutierrez.huellitasvet.ui.components.graficos.GraficoDona
import com.gutierrez.huellitasvet.ui.theme.ColorAtendida
import com.gutierrez.huellitasvet.ui.theme.ColorPendiente
import com.gutierrez.huellitasvet.viewmodel.PanelViewModel
import java.time.LocalDate

@Composable
fun PanelScreen(
    vm: PanelViewModel,
    onVerPendientes: () -> Unit,
    onCerrarSesion: (() -> Unit)? = null
) {
    val citasHoy by vm.citasHoy.collectAsState()
    val pendientes by vm.pendientes.collectAsState()
    val atendidas by vm.atendidas.collectAsState()
    val ingresos by vm.ingresosMes.collectAsState()
    val porEstado by vm.porEstado.collectAsState()
    val porServicio by vm.porServicio.collectAsState()
    val hoy = LocalDate.now()

    Column(
        Modifier.verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Panel de la clínica", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(
                    "Hoy · ${Formato.fechaCorta(hoy.toString())}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (onCerrarSesion != null) {
                IconButton(onClick = onCerrarSesion) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
                }
            }
        }

        // 3 contadores de color
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Contador("Citas hoy", citasHoy, MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.primary, Modifier.weight(1f))
            Contador("Pendientes", pendientes, ColorPendiente.copy(alpha = 0.18f), ColorPendiente, Modifier.weight(1f))
            Contador("Atendidas", atendidas, ColorAtendida.copy(alpha = 0.15f), ColorAtendida, Modifier.weight(1f))
        }

        // Ingresos del mes
        Surface(shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.primary, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(18.dp)) {
                Text("Ingresos del mes", color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f), style = MaterialTheme.typography.labelMedium)
                Text(
                    Formato.soles(ingresos), color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 28.sp, fontWeight = FontWeight.Bold
                )
                Text(
                    "Suma de las citas atendidas", color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Text("Citas por estado", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        Tarjeta { GraficoDona(porEstado) }

        Text("Citas por servicio", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        Tarjeta { GraficoBarras(porServicio) }

        if (pendientes > 0) {
            Surface(shape = RoundedCornerShape(16.dp), color = ColorPendiente.copy(alpha = 0.15f), modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Warning, null, tint = ColorPendiente)
                    Column(Modifier.weight(1f).padding(horizontal = 10.dp)) {
                        Text("$pendientes citas por confirmar", fontWeight = FontWeight.Bold)
                        Text("Revisa la agenda", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Button(onClick = onVerPendientes, colors = ButtonDefaults.buttonColors(containerColor = ColorPendiente, contentColor = Color.White)) {
                        Text("Ver")
                    }
                }
            }
        }
    }
}

@Composable
private fun Contador(titulo: String, valor: Int, fondo: Color, texto: Color, modifier: Modifier) {
    Surface(shape = RoundedCornerShape(16.dp), color = fondo, modifier = modifier) {
        Column(Modifier.padding(vertical = 14.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(valor.toString(), color = texto, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text(titulo, color = texto, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun Tarjeta(contenido: @Composable () -> Unit) {
    Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surface, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp)) { contenido() }
    }
}
