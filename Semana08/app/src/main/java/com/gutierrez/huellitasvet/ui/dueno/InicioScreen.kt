package com.gutierrez.huellitasvet.ui.dueno

import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.Color

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gutierrez.huellitasvet.data.local.entity.Mascota
import com.gutierrez.huellitasvet.data.local.relation.CitaConDetalle
import com.gutierrez.huellitasvet.model.EstadoCita
import com.gutierrez.huellitasvet.model.Formato
import com.gutierrez.huellitasvet.ui.components.EstadoCitaChip
import com.gutierrez.huellitasvet.ui.components.MascotaAvatar
import com.gutierrez.huellitasvet.ui.components.VetButton
import com.gutierrez.huellitasvet.ui.components.iconoServicio
import com.gutierrez.huellitasvet.viewmodel.InicioViewModel

@Composable
fun InicioScreen(
    nombre: String,
    vm: InicioViewModel,
    onVerMascota: (Int) -> Unit,
    onAgregar: () -> Unit,
    onVerTodas: () -> Unit,
    onPedirCita: () -> Unit,
    onCerrarSesion: (() -> Unit)? = null
) {
    val mascotas by vm.mascotas.collectAsState()
    val proxima by vm.proximaCita.collectAsState()

    Column(
        Modifier.verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(
                    "Hola, ${nombre.substringBefore(' ')}",
                    style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold
                )
                Text(
                    "¿Cómo están tus mascotas hoy?",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (onCerrarSesion != null) {
                IconButton(onClick = onCerrarSesion) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
                }
            }
            Icon(
                Icons.Filled.Notifications, contentDescription = "Notificaciones",
                modifier = Modifier.size(28.dp)
            )
        }

        TarjetaProximaCita(proxima)

        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Mis mascotas", Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold
            )
            TextButton(onClick = onVerTodas) { Text("Ver todas") }
        }
        Row(
            Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            mascotas.forEach { m -> TarjetaMascota(m) { onVerMascota(m.id) } }
            Surface(
                onClick = onAgregar,
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                modifier = Modifier.width(100.dp)
            ) {
                Column(
                    Modifier.padding(vertical = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Filled.Add, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                    Text("Agregar", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
        }

        Text("Servicios", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AccesoRapido("Consulta", "heart", Modifier.weight(1f), onPedirCita)
            AccesoRapido("Vacunas", "syringe", Modifier.weight(1f), onPedirCita)
            AccesoRapido("Baño", "scissors", Modifier.weight(1f), onPedirCita)
        }

        proxima?.let { prox ->
            Surface(shape = RoundedCornerShape(14.dp), color = Color(0xFFFEF3C7), modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(Icons.Filled.Notifications, null, tint = Color(0xFFB45309))
                    Column {
                        Text("Recordatorio", fontWeight = FontWeight.Bold, color = Color(0xFF78350F))
                        Text(
                            "${prox.mascota.mascota.nombre}: ${prox.servicio.nombre.lowercase()} el ${Formato.fechaCorta(prox.cita.fecha)}",
                            style = MaterialTheme.typography.bodySmall, color = Color(0xFF78350F)
                        )
                    }
                }
            }
        }

        VetButton("Pedir una cita", onClick = onPedirCita)
    }
}

@Composable
private fun TarjetaProximaCita(proxima: CitaConDetalle?) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (proxima == null) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Próxima cita", color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f), style = MaterialTheme.typography.labelMedium)
                Text("Aún no tienes citas", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
            }
        } else {
            Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                        "${proxima.mascota.mascota.nombre} - ${proxima.servicio.nombre}",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Filled.Schedule, null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(16.dp))
                        Text(
                            Formato.fechaCorta(proxima.cita.fecha) + " - " + proxima.cita.hora,
                            color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.SemiBold
                        )
                    }
                    EstadoCitaChip(EstadoCita.valueOf(proxima.cita.estado), sobrePrimario = true)
                }
                Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surface) {
                    MascotaAvatar(proxima.mascota.mascota.especie, Modifier.padding(6.dp), tamano = 52.dp)
                }
            }
        }
    }
}

@Composable
private fun TarjetaMascota(m: Mascota, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)),
        modifier = Modifier.width(100.dp)
    ) {
        Column(
            Modifier.padding(vertical = 14.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            MascotaAvatar(m.especie, tamano = 60.dp)
            Text(m.nombre, fontWeight = FontWeight.Bold, maxLines = 1)
            Text("Ver ficha", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun AccesoRapido(texto: String, icono: String, modifier: Modifier, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer),
        modifier = modifier
    ) {
        Column(
            Modifier.padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(iconoServicio(icono), null, tint = MaterialTheme.colorScheme.primary)
            Text(texto, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        }
    }
}
