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
fun AgendarScreen(navController: NavController, medicoId: Int) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Agendar cita con médico #$medicoId", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { navController.navigate(Screen.Confirmacion.createRoute(medicoId, 0, 0)) }) {
            Text("Confirmar cita")
        }
    }
}