package com.gutierrez.citas.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gutierrez.citas.ui.theme.AzulClinica

@Composable
fun EnlaceTexto(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = texto,
        color = AzulClinica,
        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp)
    )
}