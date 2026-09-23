package com.gutierrez.clinicasalud.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gutierrez.clinicasalud.datos.Cita
import com.gutierrez.clinicasalud.datos.DatosClinica
import com.gutierrez.clinicasalud.datos.EstadoCita

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisCitasScreen(navController: NavController, onMenuClick: () -> Unit) {

    val citas = DatosClinica.citas

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis citas") },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(imageVector = Icons.Filled.Menu, contentDescription = "Abrir menú")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(citas) { cita ->
                CitaCard(cita)
            }
        }
    }
}

@Composable
fun CitaCard(cita: Cita) {
    val confirmada = cita.estado == EstadoCita.CONFIRMADA

    // Estado diferenciado visualmente: verde = Confirmada, gris = Completada
    val colorBarra = if (confirmada) MaterialTheme.colorScheme.primary else Color.Transparent
    val colorTexto = if (confirmada) Color(0xFF2E9E5B) else Color(0xFF757575)
    val colorFondo = if (confirmada) Color(0xFFE3F5EA) else Color(0xFFEAEAEA)
    val etiqueta = if (confirmada) "Confirmada" else "Completada"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // Barra lateral de color
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(colorBarra)
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(cita.medico, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${cita.fecha}, ${cita.hora}",
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