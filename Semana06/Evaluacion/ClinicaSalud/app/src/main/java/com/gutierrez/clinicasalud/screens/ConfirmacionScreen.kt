package com.gutierrez.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gutierrez.clinicasalud.navigation.Screen

@Composable
fun ConfirmacionScreen(navController: NavController, medicoId: Int, fecha: Int, hora: Int) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("¡Cita agendada!", style = MaterialTheme.typography.headlineMedium)
        Text("Médico: $medicoId · Fecha: $fecha · Hora: $hora")
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            navController.navigate(Screen.Home.route) {
                // Limpia el back stack: evita apilar pantallas
                popUpTo(Screen.Home.route) { inclusive = true }
            }
        }) {
            Text("Volver al inicio")
        }
    }
}