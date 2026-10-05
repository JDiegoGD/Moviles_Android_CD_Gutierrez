package com.gutierrez.citas.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gutierrez.citas.R
import com.gutierrez.citas.ui.components.BotonPrimario
import com.gutierrez.citas.ui.components.EnlaceTexto
import com.gutierrez.citas.ui.theme.AzulNiebla
import com.gutierrez.citas.ui.theme.AzulNoche
import com.gutierrez.citas.ui.theme.GrisMedio

// Pantalla 1 · Bienvenida: logo, ilustración y los dos caminos (crear cuenta o entrar)
@Composable
fun SplashScreen(
    onComenzar: () -> Unit,
    onYaTengoCuenta: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(AzulNiebla, Color.White)))
            .systemBarsPadding()
            .padding(horizontal = 28.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Image(
            painter = painterResource(id = R.drawable.logo_saludplus),
            contentDescription = "Logo de SaludPlus",
            modifier = Modifier.size(84.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Clínica SaludPlus",
            style = MaterialTheme.typography.titleLarge,
            color = AzulNoche
        )
        Text(
            text = "Tu salud, a un toque de distancia",
            style = MaterialTheme.typography.bodyLarge,
            color = GrisMedio,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.medico_splash),
            contentDescription = "Ilustración de un médico",
            modifier = Modifier.size(260.dp)
        )
        Spacer(modifier = Modifier.weight(1f))

        BotonPrimario(texto = "Comenzar", onClick = onComenzar)
        Spacer(modifier = Modifier.height(12.dp))
        EnlaceTexto(texto = "Ya tengo cuenta", onClick = onYaTengoCuenta)
        Spacer(modifier = Modifier.height(8.dp))
    }
}
