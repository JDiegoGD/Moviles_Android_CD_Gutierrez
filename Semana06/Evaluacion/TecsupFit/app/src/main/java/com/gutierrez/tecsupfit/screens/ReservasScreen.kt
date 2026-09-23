package com.gutierrez.tecsupfit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gutierrez.tecsupfit.datos.DatosGym
import com.gutierrez.tecsupfit.datos.EstadoReserva
import com.gutierrez.tecsupfit.datos.Reserva

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservasScreen(navController: NavController) {

    // Lista observable: se actualiza sola cuando se reserva un cupo
    val reservas = DatosGym.reservas

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis reservas", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        if (reservas.isEmpty()) {
            // Mensaje cuando todavía no hay reservas
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aún no tienes reservas",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            // ── Lista de reservas (LazyColumn) ──
            LazyColumn(
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = padding.calculateTopPadding() + 8.dp,
                    bottom = padding.calculateBottomPadding() + 8.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(reservas) { reserva ->
                    ReservaCard(reserva)
                }
            }
        }
    }
}

@Composable
fun ReservaCard(reserva: Reserva) {
    val confirmada = reserva.estado == EstadoReserva.CONFIRMADA

    // Estado diferenciado visualmente: verde = Confirmada, gris = Completada
    val colorBarra = if (confirmada) Color(0xFF12705A) else Color.Transparent
    val colorTexto = if (confirmada) Color(0xFF12705A) else Color(0xFF757575)
    val colorFondo = if (confirmada) Color(0xFFE1F3EC) else Color(0xFFE6E6E6)
    val etiqueta = if (confirmada) "Confirmada" else "Completada"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F2F2))
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // Barra lateral de color (solo en las confirmadas)
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(colorBarra)
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = reserva.clase,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${reserva.dia}, ${reserva.hora}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Etiqueta del estado
                Surface(color = colorFondo, shape = RoundedCornerShape(50)) {
                    Text(
                        text = etiqueta,
                        color = colorTexto,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}