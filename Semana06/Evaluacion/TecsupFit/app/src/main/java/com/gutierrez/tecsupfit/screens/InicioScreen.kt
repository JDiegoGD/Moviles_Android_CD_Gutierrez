package com.gutierrez.tecsupfit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gutierrez.tecsupfit.datos.*
import com.gutierrez.tecsupfit.datos.DatosGym
import com.gutierrez.tecsupfit.navigation.Screen
import com.gutierrez.tecsupfit.navigation.BarraInferior


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(navController: NavController) {

    // Filtro elegido en la fila de chips ("Hoy" por defecto)
    var filtroSeleccionado by remember { mutableStateOf("Hoy") }

    // Clases que se muestran según el filtro
    val clasesFiltradas = DatosGym.filtrarClases(filtroSeleccionado)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("TECSUP Fit", fontWeight = FontWeight.Bold)
                        Text("Hola, Diego", style = MaterialTheme.typography.bodySmall)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = { BarraInferior(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)   // respeta el espacio del TopAppBar
                .fillMaxSize()
        ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(DatosGym.filtros) { filtro ->
                    FilterChip(
                        selected = filtro == filtroSeleccionado,
                        onClick = { filtroSeleccionado = filtro },
                        label = { Text(filtro) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            Text(
                text = "Clases disponibles",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Lista principal de clases
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(clasesFiltradas) { clase ->
                    ClaseCard(
                        clase = clase,
                        onClick = {
                            navController.navigate(Screen.Detalle.createRoute(clase.id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ClaseCard(clase: Clase, onClick: () -> Unit) {
    val horarioTexto = if (clase.dia == "Hoy") {
        "${clase.horarios.first()} · ${clase.sala}"
    } else {
        "${clase.dia} · ${clase.horarios.first()} · ${clase.sala}"
    }

    // Consulta de cupos en vivo desde el objeto observable DatosGym
    val cuposRestantes = DatosGym.cuposRestantes(clase.id)
    val estaLlena = DatosGym.estaLlena(clase.id)

    // Configuración visual de la etiqueta de cupos a la derecha de la tarjeta
    val (textoEtiqueta, fondoEtiqueta, colorTextoEtiqueta) = when {
        estaLlena -> Triple(
            "Llena",
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.error
        )
        cuposRestantes in 1..3 -> Triple(
            "Últimos $cuposRestantes",
            Color(0xFFFFF3E0),
            Color(0xFFE65100)
        )
        else -> Triple(
            "$cuposRestantes cupos",
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.primary
        )
    }

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (estaLlena) 0.6f else 1.0f),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícono de pesa dentro de un cuadro de color claro
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

            // Nombre y horario con weight(1f) para empujar la etiqueta a la derecha
            Column(modifier = Modifier.weight(1f)) {
                Text(clase.nombre, fontWeight = FontWeight.Bold)
                Text(
                    text = horarioTexto,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Etiqueta de cupos (derecha de la tarjeta)
            Surface(
                shape = RoundedCornerShape(50),
                color = fondoEtiqueta
            ) {
                Text(
                    text = textoEtiqueta,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = colorTextoEtiqueta,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun IconoPesa(color: Color, ancho: Dp) {
    val alto = ancho * 0.5f
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .width(ancho * 0.16f)
                .height(alto)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        Box(
            modifier = Modifier
                .width(ancho * 0.68f)
                .height(alto * 0.32f)
                .background(color)
        )
        Box(
            modifier = Modifier
                .width(ancho * 0.16f)
                .height(alto)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
    }
}