package com.gutierrez.citas.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gutierrez.citas.data.repository.Repositorio
import com.gutierrez.citas.ui.components.BarraSuperior
import com.gutierrez.citas.ui.components.BotonPrimario
import com.gutierrez.citas.ui.components.TarjetaMedico
import com.gutierrez.citas.ui.theme.AzulClinica
import com.gutierrez.citas.ui.theme.AzulNoche
import com.gutierrez.citas.ui.theme.GrisMedio
import com.gutierrez.citas.util.abreviaturaDia
import com.gutierrez.citas.util.nombreMesAnio
import com.gutierrez.citas.util.proximosDiasHabiles
import java.time.LocalDate

// Pantalla 6 · Fecha y hora: eliges un día y una de las horas libres de ese médico
@Composable
fun FechaHoraScreen(
    medicoId: Int,
    onAtras: () -> Unit,
    onContinuar: (fecha: String, hora: String) -> Unit
) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val especialidad = medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }

    var semanasAdelante by rememberSaveable { mutableIntStateOf(0) }
    var fechaElegida by rememberSaveable { mutableStateOf<String?>(null) }
    var horaElegida by rememberSaveable { mutableStateOf<String?>(null) }

    val inicio = LocalDate.now().plusWeeks(semanasAdelante.toLong())
    val diasMostrados = proximosDiasHabiles(inicio, 5)

    // Los horarios dependen del día elegido y se recalculan solos cuando éste cambia
    val horarios = fechaElegida
        ?.let { Repositorio.horariosDisponibles(medicoId, it) }
        .orEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
    ) {
        BarraSuperior(titulo = "Seleccionar fecha y hora", onAtras = onAtras)

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
        ) {
            if (medico != null) {
                TarjetaMedico(medico = medico, especialidad = especialidad?.nombre.orEmpty())
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Mes y año dinámicos con navegación por semanas
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (semanasAdelante > 0) {
                            semanasAdelante--
                            fechaElegida = null
                            horaElegida = null
                        }
                    },
                    enabled = semanasAdelante > 0
                ) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Semana anterior")
                }
                Text(
                    text = nombreMesAnio(diasMostrados.first()),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulNoche,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = {
                        semanasAdelante++
                        fechaElegida = null
                        horaElegida = null
                    }
                ) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Semana siguiente")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                diasMostrados.forEach { fecha ->
                    val fechaIso = fecha.toString()
                    ChipDia(
                        fecha = fecha,
                        seleccionado = fechaIso == fechaElegida,
                        onClick = {
                            fechaElegida = fechaIso
                            horaElegida = null // al cambiar de día se reinicia la hora
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Horarios disponibles",
                style = MaterialTheme.typography.titleMedium,
                color = AzulNoche
            )
            Spacer(modifier = Modifier.height(10.dp))

            when {
                fechaElegida == null -> MensajeHorarios("Elige un día para ver los horarios")
                horarios.isEmpty() -> MensajeHorarios("Ya no quedan horarios libres este día")
                else -> LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(horarios, key = { it }) { hora ->
                        ChipHora(
                            hora = hora,
                            seleccionada = hora == horaElegida,
                            onClick = { horaElegida = hora }
                        )
                    }
                }
            }
        }

        // "Continuar" solo se habilita cuando ya hay día y hora
        val fecha = fechaElegida
        val hora = horaElegida
        BotonPrimario(
            texto = "Continuar",
            habilitado = fecha != null && hora != null,
            onClick = { if (fecha != null && hora != null) onContinuar(fecha, hora) },
            modifier = Modifier.padding(20.dp)
        )
    }
}

@Composable
private fun MensajeHorarios(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.bodyMedium,
        color = GrisMedio,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun ChipDia(
    fecha: LocalDate,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(14.dp)
    Column(
        modifier = modifier
            .height(72.dp)
            .clip(forma)
            .background(if (seleccionado) AzulClinica else Color(0xFFF1F5FB))
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = abreviaturaDia(fecha),
            fontSize = 12.sp,
            color = if (seleccionado) Color.White else GrisMedio
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = fecha.dayOfMonth.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (seleccionado) Color.White else AzulNoche
        )
    }
}

@Composable
private fun ChipHora(
    hora: String,
    seleccionada: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(if (seleccionada) AzulClinica else Color(0xFFF1F5FB))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = hora,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (seleccionada) Color.White else AzulNoche
        )
    }
}
