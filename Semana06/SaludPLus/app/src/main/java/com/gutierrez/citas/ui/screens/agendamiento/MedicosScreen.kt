package com.gutierrez.citas.ui.screens.agendamiento

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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.unit.sp
import com.gutierrez.citas.data.repository.Repositorio
import com.gutierrez.citas.ui.components.BarraSuperior
import com.gutierrez.citas.ui.components.FotoMedico
import com.gutierrez.citas.ui.components.TarjetaBase
import com.gutierrez.citas.ui.theme.AzulClinica
import com.gutierrez.citas.ui.theme.AzulNoche
import com.gutierrez.citas.ui.theme.GrisMedio

// Estrella dorada de la calificación
private val Dorado = Color(0xFFF5B301)

// Fondo claro de la etiqueta de disponibilidad
private val FondoDisponible = Color(0xFFDDF7E8)
private val VerdeDisponible = Color(0xFF1E9E5A)

// Pantalla 5 · Médicos de la especialidad elegida (del mejor al menos calificado) con buscador en la lupa
@Composable
fun MedicosScreen(
    especialidadId: Int,
    onAtras: () -> Unit,
    onMedico: (Int) -> Unit
) {
    var busqueda by rememberSaveable { mutableStateOf("") }
    var buscando by rememberSaveable { mutableStateOf(false) }
    val especialidad = Repositorio.obtenerEspecialidad(especialidadId)
    val medicos = Repositorio.buscarMedicos(especialidadId, busqueda)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
    ) {
        BarraSuperior(
            titulo = "Médicos de ${especialidad?.nombre ?: "la clínica"}",
            onAtras = onAtras,
            acciones = {
                IconButton(onClick = {
                    buscando = !buscando
                    if (!buscando) busqueda = "" // al cerrar el buscador se vuelve a ver la lista completa
                }) {
                    Icon(
                        imageVector = if (buscando) Icons.Filled.Close else Icons.Filled.Search,
                        contentDescription = if (buscando) "Cerrar búsqueda" else "Buscar médico",
                        tint = AzulNoche
                    )
                }
            }
        )

        if (buscando) OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            placeholder = { Text("Buscar médico...", color = Color(0xFF9CA3AF)) },
            leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF1F5FB),
                unfocusedContainerColor = Color(0xFFF1F5FB),
                focusedBorderColor = AzulClinica,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = AzulClinica
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        )

        if (medicos.isEmpty()) {
            Text(
                text = "No hay médicos que coincidan con tu búsqueda",
                style = MaterialTheme.typography.bodyLarge,
                color = GrisMedio,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(medicos, key = { it.id }) { medico ->
                    TarjetaBase(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onMedico(medico.id) }
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                FotoMedico(nombre = medico.nombre, foto = medico.foto, tamano = 56.dp)
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = medico.nombre,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AzulNoche
                                    )
                                    Text(
                                        text = especialidad?.nombre.orEmpty(),
                                        fontSize = 13.sp,
                                        color = GrisMedio
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Filled.Star,
                                            contentDescription = null,
                                            tint = Dorado,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "${medico.calificacion} (${medico.resenas})",
                                            fontSize = 13.sp,
                                            color = GrisMedio
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            // Etiqueta verde de disponibilidad, abajo a la derecha
                            Box(
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(FondoDisponible)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = Repositorio.calcularDisponibilidad(medico),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = VerdeDisponible
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
