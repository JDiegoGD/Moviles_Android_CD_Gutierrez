package com.gutierrez.citas.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gutierrez.citas.data.repository.Repositorio
import com.gutierrez.citas.navigation.Rutas
import com.gutierrez.citas.ui.components.BarraInferior
import com.gutierrez.citas.ui.components.EnlaceTexto
import com.gutierrez.citas.ui.components.IconoEspecialidad
import com.gutierrez.citas.ui.components.TarjetaBase
import com.gutierrez.citas.ui.theme.AzulNoche
import com.gutierrez.citas.ui.theme.GrisMedio

// Colores de los cuatro accesos de arriba: un fondo suave y el tono fuerte para ícono y texto
private val FondoAgendar = Color(0xFFE3EDFF)
private val TonoAgendar = Color(0xFF2563EB)
private val FondoCitas = Color(0xFFDDF3E6)
private val TonoCitas = Color(0xFF22A05B)
private val FondoDatos = Color(0xFFEBE3FF)
private val TonoDatos = Color(0xFF7C4DFF)
private val FondoResultados = Color(0xFFFFEBD6)
private val TonoResultados = Color(0xFFF28C28)

// Pantalla 3 · Inicio: saludo, cuatro accesos en cuadrícula y especialidades destacadas
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

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { BarraInferior(rutaActual = Rutas.HOME, onNavegar = onNavegar) }
    ) { relleno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(relleno)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            // Saludo y campana de notificaciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "¡Hola, $primerNombre!",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AzulNoche
                    )
                    Text(
                        text = "¿Qué deseas hacer hoy?",
                        fontSize = 15.sp,
                        color = GrisMedio
                    )
                }
                IconButton(onClick = onNotificaciones) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Notificaciones",
                        tint = AzulNoche
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Los cuatro accesos principales en dos filas de dos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                AccesoPrincipal(
                    icono = Icons.Filled.CalendarMonth,
                    texto = "Agendar cita",
                    fondo = FondoAgendar,
                    tono = TonoAgendar,
                    onClick = onAgendar,
                    modifier = Modifier.weight(1f)
                )
                AccesoPrincipal(
                    icono = Icons.Filled.EventAvailable,
                    texto = "Mis citas",
                    fondo = FondoCitas,
                    tono = TonoCitas,
                    onClick = onMisCitas,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                AccesoPrincipal(
                    icono = Icons.Filled.Person,
                    texto = "Mis datos",
                    fondo = FondoDatos,
                    tono = TonoDatos,
                    onClick = onMisDatos,
                    modifier = Modifier.weight(1f)
                )
                AccesoPrincipal(
                    icono = Icons.Filled.Description,
                    texto = "Mis resultados",
                    fondo = FondoResultados,
                    tono = TonoResultados,
                    onClick = onResultados,
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
                    text = "Especialidades destacadas",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulNoche,
                    modifier = Modifier.weight(1f)
                )
                EnlaceTexto(texto = "Ver todas", onClick = onVerEspecialidades)
            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(destacadas, key = { it.id }) { especialidad ->
                    TarjetaBase(
                        modifier = Modifier.width(120.dp),
                        onClick = { onEspecialidad(especialidad.id) }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            IconoEspecialidad(especialidadId = especialidad.id)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = especialidad.nombre,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
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
}

// Cuadro grande de color con el ícono arriba y el texto debajo
@Composable
private fun AccesoPrincipal(
    icono: ImageVector,
    texto: String,
    fondo: Color,
    tono: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(120.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(fondo)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = tono,
            modifier = Modifier.size(44.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = texto,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = tono
        )
    }
}
