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
fun DetalleClaseScreen(navController: NavController, claseId: Int) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Detalle de la clase #$claseId", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { navController.navigate(Screen.Confirmacion.createRoute(claseId, 0)) }) {
            Text("Reservar cupo")
        }
    }
}