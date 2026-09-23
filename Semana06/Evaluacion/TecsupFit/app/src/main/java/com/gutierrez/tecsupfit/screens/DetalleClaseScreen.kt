package com.gutierrez.tecsupfit.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gutierrez.tecsupfit.datos.DatosGym
import com.gutierrez.tecsupfit.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleClaseScreen(navController: NavController, claseId: Int) {

    // Busca la clase con el id que llegó por navegación
    val clase = DatosGym.buscarClase(claseId)

    // Selección única de horario: guardamos la POSICIÓN elegida (-1 = nada elegido)
    var horarioSeleccionado by remember { mutableStateOf(-1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de clase", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        bottomBar = {
            // Botón principal: solo se habilita si eligió un horario
            Button(
                onClick = {
                    if (clase != null) {
                        DatosGym.reservar(clase, clase.horarios[horarioSeleccionado])
                        navController.navigate(
                            Screen.Confirmacion.createRoute(clase.id, horarioSeleccionado)
                        )
                    }
                },
                enabled = clase != null && horarioSeleccionado >= 0,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp)
                    .height(54.dp)
            ) {
                Text("Reservar cupo", fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        if (clase == null) {
            Text("Clase no encontrada", modifier = Modifier.padding(padding).padding(24.dp))
            return@Scaffold
        }

        // Si ya eligió horario lo muestra; si no, muestra el principal
        val horaMostrada =
            if (horarioSeleccionado >= 0) clase.horarios[horarioSeleccionado]
            else clase.horarios.first()

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Imagen de la clase: cuadro claro con la pesa en grande
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                IconoPesa(color = MaterialTheme.colorScheme.primary, ancho = 90.dp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(clase.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(
                text = "$horaMostrada · ${clase.sala} · ${clase.duracion} min",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(clase.descripcion, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${clase.cuposDisponibles} de ${clase.cuposTotales} cupos disponibles",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Selección única de horario ──
            Text("Selecciona un horario", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                itemsIndexed(clase.horarios) { index, hora ->
                    OpcionHorario(
                        texto = hora,
                        seleccionada = index == horarioSeleccionado,
                        onClick = { horarioSeleccionado = index }
                    )
                }
            }
        }
    }
}

// Opción de selección única: color principal si está elegida, gris si no
@Composable
fun OpcionHorario(texto: String, seleccionada: Boolean, onClick: () -> Unit) {
    Surface(
        selected = seleccionada,
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (seleccionada) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant,
        contentColor = if (seleccionada) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurfaceVariant,
        border = if (seleccionada) null
        else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Text(
            text = texto,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)
        )
    }
}