package com.gutierrez.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gutierrez.clinicasalud.datos.DatosClinica
import com.gutierrez.clinicasalud.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendarScreen(navController: NavController, medicoId: Int) {

    val medico = DatosClinica.buscarMedico(medicoId)
    val fechas = DatosClinica.fechas
    val horas = DatosClinica.horas

    // se guarda la poscicion elegida (-1 = nada elegido)
    var fechaSeleccionada by remember { mutableStateOf(-1) }
    var horaSeleccionada by remember { mutableStateOf(-1) }

    // Solo se puede confirmar si eligió fecha Y hora
    val puedeConfirmar = medico != null && fechaSeleccionada >= 0 && horaSeleccionada >= 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar cita") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    if (medico != null) {
                        DatosClinica.agendarCita(medico, fechas[fechaSeleccionada], horas[horaSeleccionada])
                        navController.navigate(
                            Screen.Confirmacion.createRoute(medicoId, fechaSeleccionada, horaSeleccionada)
                        )
                    }
                },
                enabled = puedeConfirmar,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp)
            ) {
                Text("Confirmar cita", modifier = Modifier.padding(vertical = 6.dp))
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = "Médico: ${medico?.nombre ?: "-"}",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "Selecciona fecha",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(fechas) { index, fecha ->
                    val partes = fecha.split(" ")
                    OpcionItem(
                        seleccionada = index == fechaSeleccionada,
                        onClick = { fechaSeleccionada = index }
                    ) {
                        Text(partes[0], style = MaterialTheme.typography.bodySmall)
                        Text(partes[1], fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "Selecciona hora",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(horas) { index, hora ->
                    OpcionItem(
                        seleccionada = index == horaSeleccionada,
                        onClick = { horaSeleccionada = index }
                    ) {
                        Text(hora, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
fun OpcionItem(
    seleccionada: Boolean,
    onClick: () -> Unit,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Surface(
        selected = seleccionada,
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (seleccionada) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant,
        contentColor = if (seleccionada) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = contenido
        )
    }
}