package com.gutierrez.biblioteca.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gutierrez.biblioteca.R
import com.gutierrez.biblioteca.ui.components.Cargando
import com.gutierrez.biblioteca.ui.theme.BibliotecaTheme

/** Pantalla de ingreso. Navega cuando el ViewModel tiene una sesion. */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onIngreso: () -> Unit,
    onRegistrar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    LaunchedEffect(estado.sesion) {
        if (estado.sesion != null) onIngreso()
    }

    LoginContenido(
        estado = estado,
        onCorreoCambio = viewModel::onCorreoCambio,
        onContrasenaCambio = viewModel::onContrasenaCambio,
        onIngresar = viewModel::ingresar,
        onRegistrar = onRegistrar,
        modifier = modifier
    )
}

/** Contenido sin ViewModel: facil de previsualizar y de probar. */
@Composable
fun LoginContenido(
    estado: LoginUiState,
    onCorreoCambio: (String) -> Unit,
    onContrasenaCambio: (String) -> Unit,
    onIngresar: () -> Unit,
    onRegistrar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        if (estado.restaurando) {
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) { Cargando() }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .imePadding()
            ) {
                EncabezadoLogin()
                Card(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .offset(y = (-40).dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "CATÁLOGO DE PRODUCTOS",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(text = "Bienvenido", style = MaterialTheme.typography.headlineSmall)
                        Text(
                            text = "Ingresa con tu cuenta para ver el catálogo.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = estado.correo,
                            onValueChange = onCorreoCambio,
                            label = { Text("Correo") },
                            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                            singleLine = true,
                            enabled = !estado.ingresando,
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
                            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                            singleLine = true,
                            enabled = !estado.ingresando,
                            shape = RoundedCornerShape(16.dp),
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(onDone = { onIngresar() }),
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

                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = onIngresar,
                            enabled = !estado.ingresando,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth().height(52.dp)
                        ) {
                            if (estado.ingresando) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(22.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                Text("Ingresar", style = MaterialTheme.typography.titleMedium)
                            }
                        }
                        TextButton(
                            onClick = onRegistrar,
                            enabled = !estado.ingresando,
                            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 4.dp)
                        ) {
                            Text("¿No tienes cuenta? Regístrate")
                        }
                    }
                }
            }
        }
    }
}

/** Cabecera con la foto de compras (el lema ya viene dentro de la imagen). */
@Composable
private fun EncabezadoLogin() {
    Image(
        painter = painterResource(R.drawable.login_fondo),
        contentDescription = "Una mujer sonriente paseando con bolsas de compras",
        contentScale = ContentScale.Crop,
        alignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
    )
}

@Preview(showBackground = true)
@Composable
private fun LoginContenidoPreview() {
    BibliotecaTheme {
        LoginContenido(
            estado = LoginUiState(
                correo = "usuario@tecsup.edu.pe",
                restaurando = false,
                error = "Correo o contraseña incorrectos."
            ),
            onCorreoCambio = {},
            onContrasenaCambio = {},
            onIngresar = {},
            onRegistrar = {}
        )
    }
}
