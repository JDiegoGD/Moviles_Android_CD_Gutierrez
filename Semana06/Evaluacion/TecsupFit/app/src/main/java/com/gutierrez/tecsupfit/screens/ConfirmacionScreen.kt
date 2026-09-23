package com.gutierrez.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gutierrez.tecsupfit.navigation.Screen

@Composable
fun ConfirmacionScreen(navController: NavController, claseId: Int, horario: Int) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("¡Cupo reservado!", style = MaterialTheme.typography.headlineMedium)
        Text("Clase: $claseId · Horario: $horario")
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            navController.navigate(Screen.Reservas.route) {
                // Deja Inicio como base de la pila
                popUpTo(Screen.Inicio.route)
            }
        }) {
            Text("Ver mis reservas")
        }
    }
}