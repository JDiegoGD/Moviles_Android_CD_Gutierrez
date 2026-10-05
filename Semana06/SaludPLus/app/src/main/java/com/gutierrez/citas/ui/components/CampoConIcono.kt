package com.gutierrez.citas.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun CampoConIcono(
    valor: String,
    alCambiar: (String) -> Unit,
    etiqueta: String,
    icono: ImageVector,
    modifier: Modifier = Modifier,
    error: String? = null,
    esClave: Boolean = false,
    tipoTeclado: KeyboardType = KeyboardType.Text
) {
    var verClave by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = valor,
        onValueChange = alCambiar,
        label = { Text(etiqueta) },
        leadingIcon = { Icon(imageVector = icono, contentDescription = null) },
        trailingIcon = if (esClave) {
            {
                IconButton(onClick = { verClave = !verClave }) {
                    Icon(
                        imageVector = if (verClave) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (verClave) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            }
        } else null,
        isError = error != null,
        supportingText = if (error != null) {
            { Text(error) }
        } else null,
        singleLine = true,
        visualTransformation = if (esClave && !verClave) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = if (esClave) KeyboardType.Password else tipoTeclado),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        modifier = modifier.fillMaxWidth()
    )
}