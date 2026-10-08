package com.gutierrez.citas.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gutierrez.citas.ui.components.BarraSuperior
import com.gutierrez.citas.ui.components.BotonPrimario
import com.gutierrez.citas.ui.components.TarjetaBase
import com.gutierrez.citas.ui.theme.AzulClinica
import com.gutierrez.citas.ui.theme.AzulNiebla
import com.gutierrez.citas.ui.theme.AzulNoche
import com.gutierrez.citas.ui.theme.GrisMedio

// Cada apartado es un título con su texto. Es un texto de ejemplo con fines educativos.
private val apartados = listOf(
    "Uso de la aplicación" to
        "SaludPlus permite a los pacientes consultar especialidades y médicos, agendar citas y revisar " +
        "sus resultados. Debes registrar datos verdaderos y mantener tu contraseña en reserva.",
    "Datos personales" to
        "Tus datos (nombre, teléfono y correo) se usan solo para gestionar tus citas. El tratamiento de " +
        "datos personales se rige por la Ley N.° 29733, Ley de Protección de Datos Personales, y su reglamento. " +
        "Puedes pedir que los actualicemos o los eliminemos.",
    "Citas y cancelaciones" to
        "Cada horario pertenece a un médico y a una fecha, y no puede reservarse dos veces. Puedes cancelar " +
        "una cita desde su detalle; el horario quedará libre para otros pacientes.",
    "Alcance de la información" to
        "La aplicación no reemplaza una consulta médica ni constituye diagnóstico. Ante una urgencia, " +
        "acude de inmediato a un establecimiento de salud.",
    "Cambios en estos términos" to
        "Podemos actualizar estos términos para mejorar el servicio. Si continúas usando la aplicación " +
        "después de un cambio, entendemos que lo aceptas."
)

// Pantalla 15 · Términos y condiciones, con scroll (reto extra)
@Composable
fun TerminosScreen(
    onAtras: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
    ) {
        BarraSuperior(titulo = "Términos y condiciones", onAtras = onAtras)

        // Solo esta parte se desliza: la nota de arriba y el botón de abajo quedan fijos
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AzulNiebla, RoundedCornerShape(16.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = null,
                    tint = AzulClinica,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Texto de ejemplo con fines educativos; no constituye asesoría legal.",
                    fontSize = 13.sp,
                    color = AzulNoche
                )
            }

            apartados.forEachIndexed { indice, (titulo, texto) ->
                Spacer(modifier = Modifier.height(14.dp))
                TarjetaBase(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .background(AzulClinica, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${indice + 1}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = titulo,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = AzulNoche
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = texto,
                                fontSize = 14.sp,
                                color = GrisMedio
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        BotonPrimario(
            texto = "Entendido",
            onClick = onAtras,
            modifier = Modifier.padding(20.dp)
        )
    }
}
