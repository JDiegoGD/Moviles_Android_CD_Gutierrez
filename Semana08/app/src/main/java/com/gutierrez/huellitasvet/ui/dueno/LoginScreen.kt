package com.gutierrez.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gutierrez.huellitasvet.model.Rol
import com.gutierrez.huellitasvet.ui.components.VetButton
import com.gutierrez.huellitasvet.ui.components.VetTextField
import com.gutierrez.huellitasvet.ui.theme.VetTheme
import com.gutierrez.huellitasvet.viewmodel.SesionViewModel

/** Un solo Composable para ambos roles: el selector cambia el color de la pantalla. */
@Composable
fun LoginScreen(vm: SesionViewModel, onLogin: (Rol) -> Unit) {
    val oscuro by vm.modoOscuro.collectAsState()
    val error by vm.error.collectAsState()
    var rol by remember { mutableStateOf(Rol.CLIENTE) }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }

    // El tema local sigue a la pestaña elegida (verde = dueño, azul = veterinario)
    VetTheme(rol = rol, oscuro = oscuro) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(
                Modifier.verticalScroll(rememberScrollState()).padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Spacer(Modifier.height(24.dp))
                Text("Bienvenido", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Text("Ingresa para continuar", color = MaterialTheme.colorScheme.onSurfaceVariant)

                SelectorRol(rol) { rol = it; vm.limpiarError() }

                VetTextField(
                    valor = correo, onCambio = { correo = it; vm.limpiarError() },
                    etiqueta = "Correo", icono = Icons.Filled.Email,
                    teclado = KeyboardType.Email
                )
                VetTextField(
                    valor = clave, onCambio = { clave = it; vm.limpiarError() },
                    etiqueta = "Contraseña", icono = Icons.Filled.Lock,
                    password = true, teclado = KeyboardType.Password
                )

                if (error != null) {
                    Text(error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                VetButton("Ingresar", onClick = { vm.login(correo, clave, rol, onLogin) })

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Modo práctica", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text("Dueño: ana@correo.test / 1234", style = MaterialTheme.typography.bodySmall)
                        Text("Veterinario: vet@huellitas.test / admin", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectorRol(seleccionado: Rol, onElegir: (Rol) -> Unit) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(4.dp)) {
            listOf(Rol.CLIENTE to "Dueño", Rol.ADMIN to "Veterinario").forEach { (r, texto) ->
                val activo = r == seleccionado
                Surface(
                    onClick = { onElegir(r) },
                    shape = RoundedCornerShape(11.dp),
                    color = if (activo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        texto,
                        modifier = Modifier.padding(vertical = 10.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = if (activo) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
