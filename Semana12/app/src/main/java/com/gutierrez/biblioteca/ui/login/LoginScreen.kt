package com.gutierrez.biblioteca.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gutierrez.biblioteca.ui.components.Cargando
import com.gutierrez.biblioteca.ui.theme.BibliotecaTheme

/** Pantalla de ingreso. Navega cuando el ViewModel tiene una sesion. */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onIngreso: () -> Unit,
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
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier) { innerPadding ->
        if (estado.restaurando) {
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) { Cargando() }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Biblioteca Tecsup", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = estado.correo,
                    onValueChange = onCorreoCambio,
                    label = { Text("Correo") },
                    singleLine = true,
                    enabled = !estado.ingresando,
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
                    singleLine = true,
                    enabled = !estado.ingresando,
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
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (estado.ingresando) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Text("Ingresar")
                    }
                }
            }
        }
    }
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
            onIngresar = {}
        )
    }
}
