package com.gutierrez.citas.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.gutierrez.citas.data.repository.Repositorio
import com.gutierrez.citas.ui.components.BarraSuperior
import com.gutierrez.citas.ui.components.BotonPrimario
import com.gutierrez.citas.ui.components.FotoMedico
import com.gutierrez.citas.ui.components.TarjetaBase
import com.gutierrez.citas.ui.theme.AzulClinica
import com.gutierrez.citas.ui.theme.AzulNoche
import com.gutierrez.citas.ui.theme.GrisMedio

// Fase 1: los cinco días son fijos (lunes a viernes). La Fase 2 los vuelve dinámicos.
private data class DiaOpcion(val fecha: String, val diaSemana: String, val numero: String)

private val diasFijos = listOf(
    DiaOpcion("2026-10-12", "Lun", "12"),
    DiaOpcion("2026-10-13", "Mar", "13"),
    DiaOpcion("2026-10-14", "Mié", "14"),
    DiaOpcion("2026-10-15", "Jue", "15"),
    DiaOpcion("2026-10-16", "Vie", "16")
)

// Pantalla 6 · Fecha y hora: eliges un día y una de las horas libres de ese médico
@Composable
fun FechaHoraScreen(
    medicoId: Int,
    onAtras: () -> Unit,
    onContinuar: (fecha: String, hora: String) -> Unit
) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val especialidad = medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }

    var fechaElegida by rememberSaveable { mutableStateOf<String?>(null) }
    var horaElegida by rememberSaveable { mutableStateOf<String?>(null) }

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
        BarraSuperior(titulo = "Fecha y hora", onAtras = onAtras)

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
        ) {
            if (medico != null) {
                TarjetaBase(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FotoMedico(nombre = medico.nombre, foto = medico.foto, tamano = 56.dp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = medico.nombre,
                                style = MaterialTheme.typography.titleMedium,
                                color = AzulNoche
                            )
                            Text(
                                text = especialidad?.nombre.orEmpty(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = GrisMedio
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Mes y año con las flechas (todavía sin función: se activan en la Fase 2)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}, enabled = false) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Semana anterior")
                }
                Text(
                    text = "Octubre 2026",
                    style = MaterialTheme.typography.titleMedium,
                    color = AzulNoche,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {}, enabled = false) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Semana siguiente")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                diasFijos.forEach { dia ->
                    ChipDia(
                        dia = dia,
                        seleccionado = dia.fecha == fechaElegida,
                        onClick = {
                            fechaElegida = dia.fecha
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
    dia: DiaOpcion,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(14.dp)
    val colorTexto = if (seleccionado) Color.White else AzulNoche
    Column(
        modifier = modifier
            .clip(forma)
            .background(if (seleccionado) AzulClinica else Color.White)
            .border(1.dp, if (seleccionado) AzulClinica else Color(0xFFD5DEEE), forma)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = dia.diaSemana, style = MaterialTheme.typography.bodyMedium, color = colorTexto)
        Text(
            text = dia.numero,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = colorTexto
        )
    }
}

@Composable
private fun ChipHora(
    hora: String,
    seleccionada: Boolean,
    onClick: () -> Unit
) {
    val forma = RoundedCornerShape(12.dp)
    Text(
        text = hora,
        style = MaterialTheme.typography.labelLarge,
        color = if (seleccionada) Color.White else AzulNoche,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clip(forma)
            .background(if (seleccionada) AzulClinica else Color.White)
            .border(1.dp, if (seleccionada) AzulClinica else Color(0xFFD5DEEE), forma)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    )
}
