package com.gutierrez.clinicasalud.screens

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
import com.gutierrez.clinicasalud.datos.DatosClinica
import com.gutierrez.clinicasalud.navigation.Screen

@Composable
fun ConfirmacionScreen(navController: NavController, medicoId: Int, fecha: Int, hora: Int) {

    // Recupera los datos con los parámetros que llegaron por navegación
    val medico = DatosClinica.buscarMedico(medicoId)
    val fechaTexto = DatosClinica.fechas.getOrNull(fecha) ?: "-"
    val horaTexto = DatosClinica.horas.getOrNull(hora) ?: "-"

    val diasCompletos = mapOf(
        "Lun" to "Lunes", "Mar" to "Martes", "Mié" to "Miércoles",
        "Jue" to "Jueves", "Vie" to "Viernes", "Sáb" to "Sábado"
    )
    val partes = fechaTexto.split(" ")
    val fechaLarga = "${diasCompletos[partes[0]] ?: partes[0]} ${partes.getOrElse(1) { "" }}"

    // El botón atrás del celular vuelve al inicio limpiando el back stack
    BackHandler {
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Home.route) { inclusive = true }
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
            // Check verde dentro de un circulo
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE3F5EA)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = Color(0xFF2E9E5B),
                    modifier = Modifier.size(54.dp)
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "¡Cita agendada!",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF222222)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Resumen: médico, fecha y hora
            Text(
                text = medico?.nombre ?: "-",
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$fechaLarga, $horaTexto",
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )

            Spacer(modifier = Modifier.height(44.dp))

            // Botón de Ver mis citas
            Button(
                onClick = {
                    navController.navigate(Screen.MisCitas.route) {
                        popUpTo(Screen.Home.route)
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF1EEF6),
                    contentColor = Color(0xFF333333)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .width(206.dp)
                    .height(58.dp)
            ) {
                Text("Ver mis citas", fontSize = 15.sp, fontWeight = FontWeight.Normal)
            }
        }
    }
}