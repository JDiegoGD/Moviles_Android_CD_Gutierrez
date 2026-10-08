package com.gutierrez.citas.ui.screens.resultados

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
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gutierrez.citas.data.model.Resultado
import com.gutierrez.citas.data.repository.Repositorio
import com.gutierrez.citas.navigation.Rutas
import com.gutierrez.citas.ui.components.BarraInferior
import com.gutierrez.citas.ui.components.IconoEspecialidad
import com.gutierrez.citas.ui.components.TarjetaBase
import com.gutierrez.citas.ui.theme.AzulNoche
import com.gutierrez.citas.ui.theme.GrisMedio
import com.gutierrez.citas.util.fechaCorta

// Colores de las dos etiquetas de estado: verde para lo que ya se puede ver, ámbar para lo pendiente
private val FondoDisponible = Color(0xFFDDF7E8)
private val TextoDisponible = Color(0xFF1E9E5A)
private val FondoEnProceso = Color(0xFFFFF1D6)
private val TextoEnProceso = Color(0xFFB7791F)

// Pantalla 13 · Resultados de exámenes con una lista fija de ejemplo (reto extra)
@Composable
fun ResultadosScreen(
    onNavegar: (String) -> Unit
) {
    val resultados = Repositorio.resultados
    val disponibles = resultados.count { it.estado == "Disponible" }
    val enProceso = resultados.size - disponibles

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { BarraInferior(rutaActual = Rutas.RESULTADOS, onNavegar = onNavegar) }
    ) { relleno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(relleno)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)) {
                Text(
                    text = "Mis resultados",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AzulNoche
                )
                Text(
                    text = "Tus exámenes más recientes",
                    fontSize = 14.sp,
                    color = GrisMedio
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Resumen rápido: cuántos ya se pueden ver y cuántos siguen en proceso
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Resumen(
                        cantidad = disponibles,
                        texto = "Disponibles",
                        fondo = FondoDisponible,
                        tono = TextoDisponible,
                        modifier = Modifier.weight(1f)
                    )
                    Resumen(
                        cantidad = enProceso,
                        texto = "En proceso",
                        fondo = FondoEnProceso,
                        tono = TextoEnProceso,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(resultados, key = { it.id }) { resultado ->
                    FilaResultado(resultado)
                }
            }
        }
    }
}

// Cuadro con el número grande y su etiqueta debajo
@Composable
private fun Resumen(
    cantidad: Int,
    texto: String,
    fondo: Color,
    tono: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(fondo, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = cantidad.toString(),
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = tono
        )
        Text(
            text = texto,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = tono
        )
    }
}

// Tarjeta de un examen: ícono de la especialidad, nombre, especialidad, fecha de emisión y estado
@Composable
private fun FilaResultado(resultado: Resultado) {
    val especialidad = Repositorio.obtenerEspecialidad(resultado.especialidadId)
    val disponible = resultado.estado == "Disponible"
    val colorEstado = if (disponible) TextoDisponible else TextoEnProceso

    TarjetaBase(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconoEspecialidad(especialidadId = resultado.especialidadId)
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = resultado.examen,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulNoche
                )
                Text(
                    text = especialidad?.nombre.orEmpty(),
                    fontSize = 13.sp,
                    color = GrisMedio
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Event,
                        contentDescription = null,
                        tint = GrisMedio,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Emitido el ${fechaCorta(resultado.fecha)}",
                        fontSize = 12.sp,
                        color = GrisMedio
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            // Estado con un puntito del mismo color delante del texto
            Row(
                modifier = Modifier
                    .background(
                        if (disponible) FondoDisponible else FondoEnProceso,
                        RoundedCornerShape(50)
                    )
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .background(colorEstado, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = resultado.estado,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorEstado
                )
            }
        }
    }
}
