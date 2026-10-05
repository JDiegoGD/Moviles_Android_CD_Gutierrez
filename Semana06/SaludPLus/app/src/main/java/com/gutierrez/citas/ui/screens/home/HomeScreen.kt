package com.gutierrez.citas.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gutierrez.citas.data.repository.Repositorio
import com.gutierrez.citas.ui.components.EnlaceTexto
import com.gutierrez.citas.ui.components.IconoEspecialidad
import com.gutierrez.citas.ui.components.TarjetaBase
import com.gutierrez.citas.ui.theme.AzulClinica
import com.gutierrez.citas.ui.theme.AzulNoche
import com.gutierrez.citas.ui.theme.GrisMedio

// Pantalla 3 · Inicio: saludo, banner para agendar, accesos rápidos y especialidades destacadas
@Composable
fun HomeScreen(
    onNotificaciones: () -> Unit,
    onAgendar: () -> Unit,
    onMisCitas: () -> Unit,
    onMisDatos: () -> Unit,
    onResultados: () -> Unit,
    onEspecialidad: (Int) -> Unit,
    onVerEspecialidades: () -> Unit,
    onNavegar: (String) -> Unit
) {
    val primerNombre = Repositorio.usuarioActual
        ?.nombre?.trim()?.substringBefore(" ")
        ?.ifBlank { null } ?: "paciente"
    val destacadas = Repositorio.especialidadesDestacadas()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Saludo y campana de notificaciones
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Hola, $primerNombre",
                    style = MaterialTheme.typography.titleLarge,
                    color = AzulNoche
                )
                Text(
                    text = "¿En qué podemos ayudarte hoy?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisMedio
                )
            }
            IconButton(
                onClick = onNotificaciones,
                modifier = Modifier.background(Color.White, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Notificaciones",
                    tint = AzulClinica
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Banner principal para empezar a agendar
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = AzulClinica)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Agenda tu cita médica",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Elige especialidad, médico y horario en pocos pasos.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f)
                )
                Spacer(modifier = Modifier.height(14.dp))
                Button(
                    onClick = onAgendar,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = AzulClinica
                    )
                ) {
                    Text(text = "Agendar cita", fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Accesos rápidos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AccesoRapido(
                icono = Icons.Filled.CalendarMonth,
                texto = "Mis citas",
                onClick = onMisCitas,
                modifier = Modifier.weight(1f)
            )
            AccesoRapido(
                icono = Icons.Filled.Description,
                texto = "Resultados",
                onClick = onResultados,
                modifier = Modifier.weight(1f)
            )
            AccesoRapido(
                icono = Icons.Filled.Person,
                texto = "Mis datos",
                onClick = onMisDatos,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Especialidades destacadas
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Especialidades",
                style = MaterialTheme.typography.titleMedium,
                color = AzulNoche,
                modifier = Modifier.weight(1f)
            )
            EnlaceTexto(texto = "Ver todas", onClick = onVerEspecialidades)
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(destacadas, key = { it.id }) { especialidad ->
                TarjetaBase(
                    modifier = Modifier.width(132.dp),
                    onClick = { onEspecialidad(especialidad.id) }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp, horizontal = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconoEspecialidad(especialidadId = especialidad.id)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = especialidad.nombre,
                            style = MaterialTheme.typography.labelLarge,
                            color = AzulNoche,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            minLines = 2
                        )
                    }
                }
            }
        }
    }
}

// Tarjeta cuadrada con ícono y texto para los accesos de la parte superior
@Composable
private fun AccesoRapido(
    icono: ImageVector,
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TarjetaBase(modifier = modifier, onClick = onClick) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icono, contentDescription = null, tint = AzulClinica)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = texto,
                style = MaterialTheme.typography.labelLarge,
                color = AzulNoche
            )
        }
    }
}