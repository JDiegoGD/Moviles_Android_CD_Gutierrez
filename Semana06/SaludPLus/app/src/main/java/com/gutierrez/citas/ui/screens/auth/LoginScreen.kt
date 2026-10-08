package com.gutierrez.citas.ui.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gutierrez.citas.R
import com.gutierrez.citas.data.repository.Repositorio
import com.gutierrez.citas.ui.components.BotonPrimario
import com.gutierrez.citas.ui.theme.AzulClinica
import com.gutierrez.citas.ui.theme.AzulNoche
import com.gutierrez.citas.ui.theme.GrisMedio
import com.gutierrez.citas.ui.theme.RojoAlerta

private val BordeCampo = Color(0xFFD5DEEC)
private val FondoError = Color(0xFFFDECEC)

// Pantalla 8 · Inicio de sesión: no sale en el diseño, así que tiene su propia composición
@Composable
fun LoginScreen(
    onLoginExitoso: () -> Unit,
    onIrRegistro: () -> Unit,
    onAtras: () -> Unit
) {
    var correo by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    var verContrasena by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    // Los dos campos comparten colores: borde gris, y azul de la clínica al enfocarlos
    val coloresCampo = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedBorderColor = AzulClinica,
        unfocusedBorderColor = BordeCampo,
        focusedLabelColor = AzulClinica,
        cursorColor = AzulClinica,
        focusedLeadingIconColor = AzulClinica,
        unfocusedLeadingIconColor = GrisMedio
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .imePadding()
    ) {
        IconButton(
            onClick = onAtras,
            modifier = Modifier.padding(start = 8.dp, top = 8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = AzulNoche
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_saludplus),
                contentDescription = "Clínica SaludPlus",
                contentScale = ContentScale.Fit,
                alignment = Alignment.CenterStart,
                modifier = Modifier.size(84.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Hola de nuevo",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AzulNoche
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Entra con tu correo para ver y reservar tus citas.",
                fontSize = 15.sp,
                color = GrisMedio
            )
            Spacer(modifier = Modifier.height(28.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                    mensajeError = null
                },
                label = { Text("Correo") },
                leadingIcon = { Icon(imageVector = Icons.Filled.Email, contentDescription = null) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(16.dp),
                colors = coloresCampo,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(14.dp))
            OutlinedTextField(
                value = contrasena,
                onValueChange = {
                    contrasena = it
                    mensajeError = null
                },
                label = { Text("Contraseña") },
                leadingIcon = { Icon(imageVector = Icons.Filled.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { verContrasena = !verContrasena }) {
                        Icon(
                            imageVector = if (verContrasena) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (verContrasena) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = GrisMedio
                        )
                    }
                },
                singleLine = true,
                visualTransformation = if (verContrasena) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(16.dp),
                colors = coloresCampo,
                modifier = Modifier.fillMaxWidth()
            )

            // El error aparece en una cajita rosada debajo de los campos
            mensajeError?.let { mensaje ->
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(FondoError, RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = null,
                        tint = RojoAlerta,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = mensaje, fontSize = 13.sp, color = RojoAlerta)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            BotonPrimario(
                texto = "Entrar",
                onClick = {
                    if (correo.isBlank() || contrasena.isEmpty()) {
                        mensajeError = "Escribe tu correo y tu contraseña."
                    } else if (Repositorio.iniciarSesion(correo.trim(), contrasena)) {
                        onLoginExitoso()
                    } else {
                        mensajeError = "No encontramos una cuenta con esos datos."
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "¿Aún no tienes cuenta?",
                fontSize = 14.sp,
                color = GrisMedio,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = onIrRegistro,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, AzulClinica),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AzulClinica),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(text = "Crear una cuenta", fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
