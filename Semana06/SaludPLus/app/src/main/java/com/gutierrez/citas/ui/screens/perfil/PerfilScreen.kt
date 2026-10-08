package com.gutierrez.citas.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gutierrez.citas.data.repository.Repositorio
import com.gutierrez.citas.navigation.Rutas
import com.gutierrez.citas.ui.components.BannerDegradado
import com.gutierrez.citas.ui.components.BarraInferior
import com.gutierrez.citas.ui.components.FilaDato
import com.gutierrez.citas.ui.theme.AzulClinica
import com.gutierrez.citas.ui.theme.AzulNiebla
import com.gutierrez.citas.ui.theme.AzulNoche
import com.gutierrez.citas.ui.theme.RojoAlerta

// Fondo rosado suave del botón de cerrar sesión
private val FondoCerrar = Color(0xFFFDECEC)

// Pantalla 11 · Mi perfil: banner con el avatar, contador de citas, datos y cierre de sesión
@Composable
fun PerfilScreen(
    onNavegar: (String) -> Unit,
    onCerrarSesion: () -> Unit
) {
    val usuario = Repositorio.usuarioActual
    val totalCitas = Repositorio.citasDelUsuario().size

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { BarraInferior(rutaActual = Rutas.PERFIL, onNavegar = onNavegar) }
    ) { relleno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(relleno)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp, bottom = 24.dp)
        ) {
            // Banner: avatar con la inicial, nombre y correo
            BannerDegradado {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = usuario?.nombre?.trim()?.firstOrNull()?.uppercase() ?: "?",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AzulClinica
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Mi perfil",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(alpha = 0.75f)
                        )
                        Text(
                            text = usuario?.nombre ?: "Sin sesión",
                            fontSize = 21.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            maxLines = 1
                        )
                        Text(
                            text = usuario?.correo.orEmpty(),
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.85f),
                            maxLines = 1
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(modifier = Modifier.height(18.dp))

                // Contador de citas en una tira celeste
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AzulNiebla, RoundedCornerShape(18.dp))
                        .padding(horizontal = 18.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = null,
                        tint = AzulClinica,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Citas reservadas",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AzulNoche,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = totalCitas.toString(),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AzulClinica
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))
                Text(
                    text = "Mis datos personales",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulNoche
                )
                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        FilaDato(
                            etiqueta = "Nombre",
                            valor = usuario?.nombre.orEmpty(),
                            icono = Icons.Filled.Person,
                            conLinea = true
                        )
                        FilaDato(
                            etiqueta = "Celular",
                            valor = usuario?.telefono.orEmpty(),
                            icono = Icons.Filled.Phone,
                            conLinea = true
                        )
                        FilaDato(
                            etiqueta = "Correo",
                            valor = usuario?.correo.orEmpty(),
                            icono = Icons.Filled.Email
                        )
                    }
                }

                Spacer(modifier = Modifier.height(26.dp))
                Button(
                    onClick = {
                        // Primero se borra la sesión y recién después se navega
                        Repositorio.cerrarSesion()
                        onCerrarSesion()
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FondoCerrar,
                        contentColor = RojoAlerta
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = null,
                        tint = RojoAlerta
                    )
                    Text(
                        text = "  Cerrar sesión",
                        fontWeight = FontWeight.SemiBold,
                        color = RojoAlerta
                    )
                }
            }
        }
    }
}
