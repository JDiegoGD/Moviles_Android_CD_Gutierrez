package com.gutierrez.biblioteca.ui.registro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gutierrez.biblioteca.ui.theme.BibliotecaTheme
import com.gutierrez.biblioteca.ui.theme.Naranja
import com.gutierrez.biblioteca.ui.theme.NaranjaClaro

/** Pantalla de registro. Navega cuando el ViewModel crea la sesion. */
@Composable
fun RegistroScreen(
    viewModel: RegistroViewModel,
    onRegistrado: () -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    LaunchedEffect(estado.sesion) {
        if (estado.sesion != null) onRegistrado()
    }

    RegistroContenido(
        estado = estado,
        onCorreoCambio = viewModel::onCorreoCambio,
        onContrasenaCambio = viewModel::onContrasenaCambio,
        onConfirmacionCambio = viewModel::onConfirmacionCambio,
        onRegistrar = viewModel::registrar,
        onVolver = onVolver,
        modifier = modifier
    )
}

/** Contenido sin ViewModel: facil de previsualizar y de probar. */
@Composable
fun RegistroContenido(
    estado: RegistroUiState,
    onCorreoCambio: (String) -> Unit,
    onContrasenaCambio: (String) -> Unit,
    onConfirmacionCambio: (String) -> Unit,
    onRegistrar: () -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            EncabezadoRegistro(onVolver = onVolver)
            Card(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .offset(y = (-36).dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    OutlinedTextField(
                        value = estado.correo,
                        onValueChange = onCorreoCambio,
                        label = { Text("Correo") },
                        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                        singleLine = true,
                        enabled = !estado.registrando,
                        shape = RoundedCornerShape(16.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = estado.contrasena,
                        onValueChange = onContrasenaCambio,
                        label = { Text("Contraseña") },
                        supportingText = { Text("Mínimo 6 caracteres") },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                        singleLine = true,
                        enabled = !estado.registrando,
                        shape = RoundedCornerShape(16.dp),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = estado.confirmacion,
                        onValueChange = onConfirmacionCambio,
                        label = { Text("Repite la contraseña") },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                        singleLine = true,
                        enabled = !estado.registrando,
                        shape = RoundedCornerShape(16.dp),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = { onRegistrar() }),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (estado.error != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = estado.error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = onRegistrar,
                        enabled = !estado.registrando,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth().height(52.dp)
                    ) {
                        if (estado.registrando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Crear cuenta", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                    TextButton(
                        onClick = onVolver,
                        enabled = !estado.registrando,
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 4.dp)
                    ) {
                        Text("¿Ya tienes cuenta? Inicia sesión")
                    }
                }
            }
        }
    }
}

/** Cabecera naranja con flecha para volver al login. */
@Composable
private fun EncabezadoRegistro(onVolver: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
            .background(Brush.verticalGradient(listOf(Naranja, NaranjaClaro)))
    ) {
        Box(
            modifier = Modifier
                .size(160.dp)
                .align(Alignment.TopEnd)
                .offset(x = 50.dp, y = (-40).dp)
                .background(Color.White.copy(alpha = 0.14f), CircleShape)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(start = 8.dp, end = 24.dp, bottom = 64.dp)
        ) {
            IconButton(onClick = onVolver) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }
            Text(
                text = "Crea tu cuenta",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            )
            Text(
                text = "Regístrate para guardar tus productos favoritos",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.92f),
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegistroContenidoPreview() {
    BibliotecaTheme {
        RegistroContenido(
            estado = RegistroUiState(
                correo = "nuevo@tecsup.edu.pe",
                error = "Las contraseñas no coinciden."
            ),
            onCorreoCambio = {},
            onContrasenaCambio = {},
            onConfirmacionCambio = {},
            onRegistrar = {},
            onVolver = {}
        )
    }
}
