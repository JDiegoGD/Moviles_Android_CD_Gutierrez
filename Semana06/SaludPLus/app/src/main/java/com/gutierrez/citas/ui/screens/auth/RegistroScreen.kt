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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.text.font.FontWeight
import com.gutierrez.citas.data.model.Usuario
import com.gutierrez.citas.data.repository.Repositorio
import com.gutierrez.citas.ui.components.BotonPrimario
import com.gutierrez.citas.ui.components.CampoConIcono
import com.gutierrez.citas.ui.components.EnlaceTexto
import com.gutierrez.citas.ui.theme.AzulNoche
import com.gutierrez.citas.ui.theme.GrisMedio

// Cada validación devuelve el mensaje de error, o null cuando el dato está bien
private fun errorNombre(nombre: String): String? =
    if (nombre.isBlank()) "Escribe tu nombre completo" else null

private fun errorTelefono(telefono: String): String? =
    if (telefono.length != 9) "El teléfono debe tener 9 dígitos" else null

private fun errorCorreo(correo: String): String? =
    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()) {
        "Escribe un correo válido"
    } else null

private fun errorContrasena(contrasena: String): String? =
    if (contrasena.length < 6) "Mínimo 6 caracteres" else null

// Pantalla 2 · Registro de un paciente nuevo
@Composable
fun RegistroScreen(
    onRegistroExitoso: () -> Unit,
    onIrLogin: () -> Unit,
    onTerminos: () -> Unit
) {
    // rememberSaveable: si el paciente abre los términos y vuelve, no pierde lo escrito
    var nombre by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    var intentoEnviar by rememberSaveable { mutableStateOf(false) }
    var correoRepetido by rememberSaveable { mutableStateOf(false) }

    // Los errores solo se muestran después de pulsar "Registrarme" por primera vez
    val mensajeNombre = if (intentoEnviar) errorNombre(nombre) else null
    val mensajeTelefono = if (intentoEnviar) errorTelefono(telefono) else null
    val mensajeCorreo = (if (intentoEnviar) errorCorreo(correo) else null)
        ?: if (correoRepetido) "Este correo ya está registrado" else null
    val mensajeContrasena = if (intentoEnviar) errorContrasena(contrasena) else null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Crear cuenta",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AzulNoche,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Regístrate para agendar tus citas",
            fontSize = 15.sp,
            color = GrisMedio,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        CampoConIcono(
            valor = nombre,
            alCambiar = { nombre = it },
            etiqueta = "Nombre completo",
            ayuda = "Ej. Juan Pérez",
            icono = Icons.Filled.Person,
            error = mensajeNombre
        )
        CampoConIcono(
            valor = telefono,
            // Solo deja pasar dígitos y nunca más de 9
            alCambiar = { if (it.length <= 9 && it.all { c -> c.isDigit() }) telefono = it },
            etiqueta = "Teléfono",
            ayuda = "Ej. 987654321",
            icono = Icons.Filled.Phone,
            error = mensajeTelefono,
            tipoTeclado = KeyboardType.Phone
        )
        CampoConIcono(
            valor = correo,
            alCambiar = {
                correo = it
                correoRepetido = false
            },
            etiqueta = "Correo electrónico",
            ayuda = "Ej. juan@correo.com",
            icono = Icons.Filled.Email,
            error = mensajeCorreo,
            tipoTeclado = KeyboardType.Email
        )
        CampoConIcono(
            valor = contrasena,
            alCambiar = { contrasena = it },
            etiqueta = "Contraseña",
            ayuda = "Mínimo 6 caracteres",
            icono = Icons.Filled.Lock,
            error = mensajeContrasena,
            esClave = true
        )


        Spacer(modifier = Modifier.height(4.dp))

        BotonPrimario(
            texto = "Registrarme",
            onClick = {
                intentoEnviar = true
                val hayErrores = listOf(
                    errorNombre(nombre),
                    errorTelefono(telefono),
                    errorCorreo(correo),
                    errorContrasena(contrasena)
                ).any { it != null }

                if (!hayErrores) {
                    val nuevo = Usuario(
                        nombre = nombre.trim(),
                        telefono = telefono,
                        correo = correo.trim(),
                        contrasena = contrasena
                    )
                    if (Repositorio.registrarUsuario(nuevo)) {
                        onRegistroExitoso()
                    } else {
                        correoRepetido = true
                    }
                }
            }
        )

        Column(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Al registrarte aceptas nuestros",
                style = MaterialTheme.typography.bodyMedium,
                color = GrisMedio,
                textAlign = TextAlign.Center
            )
            EnlaceTexto(texto = "términos y condiciones", onClick = onTerminos)
        }

        Spacer(modifier = Modifier.height(200.dp))

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "¿Ya tienes cuenta?",
                style = MaterialTheme.typography.bodyMedium,
                color = GrisMedio
            )
            EnlaceTexto(texto = "Iniciar sesión", onClick = onIrLogin)
        }
    }
}
