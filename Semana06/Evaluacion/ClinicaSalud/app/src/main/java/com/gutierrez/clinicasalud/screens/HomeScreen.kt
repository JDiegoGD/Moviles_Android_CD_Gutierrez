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
fun HomeScreen(navController: NavController, onMenuClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Inicio", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { navController.navigate(Screen.Detail.createRoute(1)) }) {
            Text("Ver médico 1")
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(onClick = { navController.navigate(Screen.MisCitas.route) }) {
            Text("Mis citas")
        }
    }
}