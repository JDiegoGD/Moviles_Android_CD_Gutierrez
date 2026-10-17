package com.gutierrez.huellitasvet.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gutierrez.huellitasvet.ui.theme.ColorCancelada

@Composable
fun VetButton(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    secundario: Boolean = false,
    peligro: Boolean = false
) {
    val forma = RoundedCornerShape(14.dp)
    val m = modifier
        .fillMaxWidth()
        .height(50.dp)
    if (secundario) {
        val color = if (peligro) ColorCancelada else MaterialTheme.colorScheme.primary
        OutlinedButton(
            onClick = onClick,
            modifier = m,
            enabled = enabled,
            shape = forma,
            border = BorderStroke(1.dp, color),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = color)
        ) { Text(texto, fontWeight = FontWeight.Bold) }
    } else {
        Button(onClick = onClick, modifier = m, enabled = enabled, shape = forma) {
            Text(texto, fontWeight = FontWeight.Bold)
        }
    }
}
