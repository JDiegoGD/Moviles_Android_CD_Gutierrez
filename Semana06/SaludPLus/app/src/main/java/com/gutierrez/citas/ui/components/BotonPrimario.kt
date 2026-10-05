package com.gutierrez.citas.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gutierrez.citas.ui.theme.AzulClinica

/** Botón azul ancho que se usa como acción principal de cada pantalla. */
@Composable
fun BotonPrimario(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = habilitado,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AzulClinica,
            contentColor = Color.White,
            disabledContainerColor = Color(0xFFBFD0F5),
            disabledContentColor = Color.White
        )
    ) {
        Text(text = texto, style = MaterialTheme.typography.titleMedium)
    }
}