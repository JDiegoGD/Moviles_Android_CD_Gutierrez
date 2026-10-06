package com.gutierrez.citas.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gutierrez.citas.R
import com.gutierrez.citas.ui.components.BotonPrimario
import com.gutierrez.citas.ui.components.EnlaceTexto
import com.gutierrez.citas.ui.theme.AzulNoche
import com.gutierrez.citas.ui.theme.GrisMedio

// Pantalla 1 · Bienvenida: logo, lema, ilustración del médico y los dos caminos (crear cuenta o entrar)
@Composable
fun SplashScreen(
    onComenzar: () -> Unit,
    onYaTengoCuenta: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Marca de la clínica
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.logo_saludplus),
            contentDescription = "Logo de SaludPlus",
            modifier = Modifier.height(110.dp)
        )
        Text(
            text = "Clínica",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = AzulNoche
        )
        Text(
            text = "SaludPlus",
            fontSize = 34.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AzulNoche
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Tu salud, nuestra prioridad",
            fontSize = 15.sp,
            color = GrisMedio,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.medico_splash),
            contentDescription = "Médico de la clínica",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )

        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BotonPrimario(texto = "Comenzar", onClick = onComenzar)
            Spacer(modifier = Modifier.height(4.dp))
            EnlaceTexto(texto = "Ya tengo una cuenta", onClick = onYaTengoCuenta)
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}
