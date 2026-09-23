package com.gutierrez.tecsupfit.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gutierrez.tecsupfit.datos.DatosGym
import com.gutierrez.tecsupfit.navigation.Screen

@Composable
fun ConfirmacionScreen(navController: NavController, claseId: Int, horario: Int) {

    // Recupera los datos con los parámetros que llegaron por navegación
    val clase = DatosGym.buscarClase(claseId)
    val hora = clase?.horarios?.getOrNull(horario) ?: "-"

    // "Hoy, 6:00 pm · Sala 1"
    val resumen = if (clase != null) "${clase.dia}, $hora · ${clase.sala}" else "-"

    // El botón "atrás" del celular vuelve al Inicio limpiando la pila
    // (así no se regresa al Detalle y no se duplica la reserva)
    BackHandler {
        navController.navigate(Screen.Inicio.route) {
            popUpTo(Screen.Inicio.route) { inclusive = true }
        }
    }

    Scaffold(
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Check verde dentro de un círculo verde claro
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE1F3EC)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = Color(0xFF12705A),
                    modifier = Modifier.size(54.dp)
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "¡Cupo reservado!",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF222222)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Resumen de la reserva: clase y horario
            Text(text = clase?.nombre ?: "-", fontSize = 14.sp, color = Color(0xFF666666))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = resumen, fontSize = 14.sp, color = Color(0xFF666666))

            Spacer(modifier = Modifier.height(44.dp))

            // Botón claro "Ver mis reservas"
            Button(
                onClick = {
                    navController.navigate(Screen.Reservas.route) {
                        // Deja Inicio como base: "atrás" desde Reservas vuelve a Inicio
                        popUpTo(Screen.Inicio.route)
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF0F0F0),
                    contentColor = Color(0xFF333333)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .width(206.dp)
                    .height(58.dp)
            ) {
                Text("Ver mis reservas", fontSize = 15.sp)
            }
        }
    }
}