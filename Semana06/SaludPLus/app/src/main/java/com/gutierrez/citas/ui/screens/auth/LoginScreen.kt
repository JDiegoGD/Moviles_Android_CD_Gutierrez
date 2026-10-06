package com.gutierrez.citas.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gutierrez.citas.ui.components.BarraSuperior
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import com.gutierrez.citas.data.repository.Repositorio
import com.gutierrez.citas.ui.components.BotonPrimario
import com.gutierrez.citas.ui.components.CampoConIcono
import com.gutierrez.citas.ui.components.EnlaceTexto
import com.gutierrez.citas.ui.theme.AzulNoche
import com.gutierrez.citas.ui.theme.GrisMedio
import com.gutierrez.citas.ui.theme.RojoAlerta

// Pantalla 8 · Inicio de sesión (vista que no venía en el diseño, hecha con el mismo estilo)
@Composable
fun LoginScreen(
    onLoginExitoso: () -> Unit,
    onIrRegistro: () -> Unit,
    onAtras: () -> Unit
) {
    var correo by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .imePadding()
    ) {
        BarraSuperior(titulo = "Iniciar sesión", onAtras = onAtras)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Bienvenido de nuevo",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AzulNoche,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Ingresa para gestionar tus citas",
            fontSize = 15.sp,
            color = GrisMedio,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        CampoConIcono(
            valor = correo,
            alCambiar = {
                correo = it
                mensajeError = null
            },
            etiqueta = "Correo electrónico",
            ayuda = "Ej. juan@correo.com",
            icono = Icons.Filled.Email,
            tipoTeclado = KeyboardType.Email
        )
        CampoConIcono(
            valor = contrasena,
            alCambiar = {
                contrasena = it
                mensajeError = null
            },
            etiqueta = "Contraseña",
            ayuda = "Tu contraseña",
            icono = Icons.Filled.Lock,
            esClave = true
        )

        mensajeError?.let { mensaje ->
            Text(
                text = mensaje,
                style = MaterialTheme.typography.bodyMedium,
                color = RojoAlerta
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        BotonPrimario(
            texto = "Iniciar sesión",
            onClick = {
                if (correo.isBlank() || contrasena.isEmpty()) {
                    mensajeError = "Completa tu correo y tu contraseña"
                } else if (Repositorio.iniciarSesion(correo, contrasena)) {
                    onLoginExitoso()
                } else {
                    mensajeError = "Correo o contraseña incorrectos"
                }
            }
        )

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "¿No tienes cuenta?",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = AzulNoche
            )
            EnlaceTexto(texto = "Regístrate", onClick = onIrRegistro)
        }
        }
    }
}
