package com.gutierrez.citas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gutierrez.citas.ui.theme.AzulClinica
import com.gutierrez.citas.ui.theme.AzulNoche
import com.gutierrez.citas.ui.theme.GrisMedio
import com.gutierrez.citas.ui.theme.RojoAlerta

/**
 * Campo de texto del diseño: una caja grande con el ícono a la izquierda y, pegada a ella,
 * la caja de texto con su título encima. [ayuda] es el ejemplo que se ve mientras está vacío.
 * Con [esClave] oculta lo escrito y agrega el ojito para mostrarlo.
 */
@Composable
fun CampoConIcono(
    valor: String,
    alCambiar: (String) -> Unit,
    etiqueta: String,
    icono: ImageVector,
    modifier: Modifier = Modifier,
    ayuda: String = "",
    error: String? = null,
    esClave: Boolean = false,
    tipoTeclado: KeyboardType = KeyboardType.Text
) {
    var verClave by remember { mutableStateOf(false) }
    val forma = RoundedCornerShape(16.dp)
    val colorBorde = if (error != null) RojoAlerta else Color(0xFFBFC8D6)

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
        ) {
            // Título y caja de texto: arrancan a la mitad del ícono y quedan por detrás de él
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(start = 33.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = etiqueta,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = GrisMedio,
                    modifier = Modifier.padding(start = 45.dp, bottom = 4.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(forma)
                        .background(Color.White)
                        .border(1.5.dp, colorBorde, forma)
                        .padding(start = 45.dp, end = if (esClave) 4.dp else 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = valor,
                        onValueChange = alCambiar,
                        singleLine = true,
                        textStyle = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = AzulNoche
                        ),
                        cursorBrush = SolidColor(AzulClinica),
                        visualTransformation = if (esClave && !verClave) PasswordVisualTransformation()
                        else VisualTransformation.None,
                        keyboardOptions = KeyboardOptions(keyboardType = if (esClave) KeyboardType.Password else tipoTeclado),
                        modifier = Modifier.weight(1f),
                        decorationBox = { campoInterno ->
                            Box(contentAlignment = Alignment.CenterStart) {
                                if (valor.isEmpty()) {
                                    Text(text = ayuda, fontSize = 15.sp, color = Color(0xFF9CA3AF))
                                }
                                campoInterno()
                            }
                        }
                    )
                    if (esClave) {
                        IconButton(onClick = { verClave = !verClave }, modifier = Modifier.size(36.dp)) {
                            Icon(
                                imageVector = if (verClave) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (verClave) "Ocultar contraseña" else "Mostrar contraseña",
                                tint = GrisMedio,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // La caja grande del ícono va dibujada encima
            Box(
                modifier = Modifier
                    .size(66.dp)
                    .align(Alignment.CenterStart)
                    .clip(forma)
                    .background(Color(0xFFF7FAFF))
                    .border(1.5.dp, colorBorde, forma),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = AzulClinica,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        if (error != null) {
            Text(
                text = error,
                fontSize = 12.sp,
                color = RojoAlerta,
                modifier = Modifier.padding(start = 78.dp, top = 3.dp)
            )
        }
    }
}
