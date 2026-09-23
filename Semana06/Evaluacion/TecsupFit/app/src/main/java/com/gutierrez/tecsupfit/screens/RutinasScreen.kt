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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gutierrez.tecsupfit.datos.DatosGym
import com.gutierrez.tecsupfit.navigation.BarraInferior

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutinasScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Rutinas", fontWeight = FontWeight.Bold) })
        },
        bottomBar = { BarraInferior(navController) }
    ) { padding ->
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
            items(DatosGym.rutinas) { rutina ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F2F2))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Ícono de pesa (el mismo del Inicio)
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            IconoPesa(color = MaterialTheme.colorScheme.primary, ancho = 28.dp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(rutina.nombre, fontWeight = FontWeight.Bold)
                            Text(
                                text = rutina.detalle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}