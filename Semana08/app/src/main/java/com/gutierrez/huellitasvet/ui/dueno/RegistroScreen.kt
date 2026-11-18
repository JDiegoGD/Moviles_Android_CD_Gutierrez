package com.gutierrez.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gutierrez.huellitasvet.R
import com.gutierrez.huellitasvet.model.Rol
import com.gutierrez.huellitasvet.ui.components.CabeceraImagen
import com.gutierrez.huellitasvet.ui.components.VetButton
import com.gutierrez.huellitasvet.ui.components.VetTextField
import com.gutierrez.huellitasvet.ui.theme.VetTheme
import com.gutierrez.huellitasvet.viewmodel.SesionViewModel

/** Registro de cuenta nueva: sirve para dueños, veterinarios y administradores. */
@Composable
fun RegistroScreen(
    vm: SesionViewModel,
    onRegistrado: (Rol) -> Unit,
    onVolver: () -> Unit
) {
    val oscuro by vm.modoOscuro.collectAsState()
    val error by vm.error.collectAsState()
    var rol by remember { mutableStateOf(Rol.CLIENTE) }
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }
    var codigoAdmin by remember { mutableStateOf("") }

    // Al salir de la pantalla no dejamos un error viejo en el login
    DisposableEffect(Unit) { onDispose { vm.limpiarError() } }

    VetTheme(rol = rol, oscuro = oscuro) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface) {
            Column(Modifier.verticalScroll(rememberScrollState())) {
              CabeceraImagen(R.drawable.cabecera_veterinaria)
              Column(
                Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
              ) {
                Text("Crear cuenta", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Text("Elige tu perfil y completa tus datos", color = MaterialTheme.colorScheme.onSurfaceVariant)

                SelectorRol(rol) { rol = it; vm.limpiarError() }

                VetTextField(
                    valor = nombre, onCambio = { nombre = it; vm.limpiarError() },
                    etiqueta = "Nombre", icono = Icons.Filled.Person
                )
                VetTextField(
                    valor = apellido, onCambio = { apellido = it; vm.limpiarError() },
                    etiqueta = "Apellido", icono = Icons.Filled.Person
                )
                if (rol == Rol.ADMINISTRADOR) {
                    VetTextField(
                        valor = codigoAdmin, onCambio = { codigoAdmin = it; vm.limpiarError() },
                        etiqueta = "Código de administrador", icono = Icons.Filled.Lock
                    )
                }
                VetTextField(
                    valor = correo, onCambio = { correo = it; vm.limpiarError() },
                    etiqueta = "Correo", icono = Icons.Filled.Email, teclado = KeyboardType.Email
                )
                VetTextField(
                    valor = clave, onCambio = { clave = it; vm.limpiarError() },
                    etiqueta = "Contraseña", icono = Icons.Filled.Lock,
                    password = true, teclado = KeyboardType.Password
                )
                VetTextField(
                    valor = confirmar, onCambio = { confirmar = it; vm.limpiarError() },
                    etiqueta = "Confirmar contraseña", icono = Icons.Filled.Lock,
                    password = true, teclado = KeyboardType.Password
                )

                if (error != null) {
                    Text(error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                VetButton(
                    "Registrarme",
                    onClick = { vm.registrar(nombre, apellido, correo, clave, confirmar, codigoAdmin, rol, onRegistrado) }
                )
                TextButton(onClick = onVolver, modifier = Modifier.padding(horizontal = 0.dp)) {
                    Text("¿Ya tienes cuenta? Inicia sesión", fontWeight = FontWeight.SemiBold)
                }
              }
            }
        }
    }
}
