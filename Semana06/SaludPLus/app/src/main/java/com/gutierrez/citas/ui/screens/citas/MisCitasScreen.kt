package com.gutierrez.citas.ui.screens.citas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gutierrez.citas.data.model.Cita
import com.gutierrez.citas.data.repository.Repositorio
import com.gutierrez.citas.navigation.Rutas
import com.gutierrez.citas.ui.components.BarraInferior
import com.gutierrez.citas.ui.components.BotonPrimario
import com.gutierrez.citas.ui.components.FotoMedico
import com.gutierrez.citas.ui.theme.AzulClinica
import com.gutierrez.citas.ui.theme.AzulNiebla
import com.gutierrez.citas.ui.theme.AzulNoche
import com.gutierrez.citas.ui.theme.GrisMedio
import com.gutierrez.citas.util.rangoDeHora

private val MesesCortos = listOf(
    "ENE", "FEB", "MAR", "ABR", "MAY", "JUN",
    "JUL", "AGO", "SET", "OCT", "NOV", "DIC"
)

// Pantalla 10 · Mis citas: título a la izquierda con su contador y una tarjeta por cada cita
@Composable
fun MisCitasScreen(
    onNavegar: (String) -> Unit,
    onAgendar: () -> Unit,
    onCita: (Int) -> Unit
) {
    // Solo las citas del usuario en sesión, de la más próxima a la más lejana
    val citas = Repositorio.citasDelUsuario()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { BarraInferior(rutaActual = Rutas.MIS_CITAS, onNavegar = onNavegar) }
    ) { relleno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(relleno)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mis citas",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AzulNoche,
                    modifier = Modifier.weight(1f)
                )
                if (citas.isNotEmpty()) {
                    Text(
                        text = if (citas.size == 1) "1 reservada" else "${citas.size} reservadas",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulClinica,
                        modifier = Modifier
                            .background(AzulNiebla, RoundedCornerShape(50))
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }
            }

            if (citas.isEmpty()) {
                EstadoVacio(onAgendar = onAgendar)
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(citas, key = { it.id }) { cita ->
                        TarjetaCita(cita = cita, onClick = { onCita(cita.id) })
                    }
                }
            }
        }
    }
}

// Lista vacía: ícono en un círculo, un mensaje corto y el atajo para reservar
@Composable
private fun EstadoVacio(onAgendar: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .background(AzulNiebla, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.EventAvailable,
                contentDescription = null,
                tint = AzulClinica,
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "Tu agenda está vacía",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AzulNoche,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Cuando reserves una consulta la verás aquí.",
            fontSize = 14.sp,
            color = GrisMedio,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        BotonPrimario(texto = "Reservar consulta", onClick = onAgendar)
    }
}

// Tarjeta de una cita: mosaico con el día a la izquierda, datos al centro y la foto del médico
@Composable
private fun TarjetaCita(cita: Cita, onClick: () -> Unit) {
    val medico = Repositorio.obtenerMedico(cita.medicoId)
    val especialidad = Repositorio.obtenerEspecialidad(cita.especialidadId)

    // La fecha viene como "2026-10-12": de ahí salen el número del día y el mes corto
    val partes = cita.fecha.split("-")
    val dia = partes.getOrNull(2)?.toIntOrNull()?.toString() ?: "--"
    val mes = partes.getOrNull(1)?.toIntOrNull()?.let { MesesCortos.getOrNull(it - 1) } ?: ""

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .width(62.dp)
                    .background(AzulNiebla, RoundedCornerShape(16.dp))
                    .padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = dia,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AzulClinica
                )
                Text(
                    text = mes,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulNoche
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = medico?.nombre ?: "Médico",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulNoche,
                    maxLines = 1
                )
                Text(
                    text = especialidad?.nombre.orEmpty(),
                    fontSize = 13.sp,
                    color = GrisMedio,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .background(AzulNiebla, RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccessTime,
                        contentDescription = null,
                        tint = AzulClinica,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = rangoDeHora(cita.hora),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AzulNoche
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))
            FotoMedico(
                nombre = medico?.nombre.orEmpty(),
                foto = medico?.foto.orEmpty(),
                tamano = 48.dp
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = GrisMedio
            )
        }
    }
}
