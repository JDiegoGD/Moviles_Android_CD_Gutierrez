package com.gutierrez.citas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gutierrez.citas.data.model.Medico
import com.gutierrez.citas.ui.theme.AzulNoche
import com.gutierrez.citas.ui.theme.GrisMedio

/**
 * Tarjeta celeste con la foto, el nombre y la especialidad del médico (va arriba de varias pantallas).
 * [detalle] es una tercera línea opcional, por ejemplo el número de colegiatura.
 */
@Composable
fun TarjetaMedico(
    medico: Medico,
    especialidad: String,
    modifier: Modifier = Modifier,
    detalle: String? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFEFF4FC), RoundedCornerShape(18.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FotoMedico(nombre = medico.nombre, foto = medico.foto, tamano = 56.dp)
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = medico.nombre,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = AzulNoche
            )
            Text(text = especialidad, fontSize = 14.sp, color = GrisMedio)
            if (detalle != null) {
                Text(text = detalle, fontSize = 13.sp, color = GrisMedio)
            }
        }
    }
}
