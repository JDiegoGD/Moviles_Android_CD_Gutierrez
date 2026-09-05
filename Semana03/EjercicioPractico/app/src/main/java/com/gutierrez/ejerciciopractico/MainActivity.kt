package com.gutierrez.ejerciciopractico

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gutierrez.ejerciciopractico.ui.theme.EjercicioPracticoTheme

val fondo = Color(0xFFDABED8)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Surface(
                color = fondo
            )  {
                EjercicioPracticoTheme {
                    Column() {
                        TitleApp()
                        SubTitleApp()
                    }
                }
            }
        }
    }
}

@Composable
fun TitleApp(){
    Column() {
        Text(
            text = "Registro de Notas",
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
            ,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun SubTitleApp(){
    var nota1 by remember { mutableFloatStateOf(0f) }
    var nota2 by remember { mutableFloatStateOf(0f) }
    var nota3 by remember { mutableFloatStateOf(0f) }
    var nota4 by remember { mutableFloatStateOf(0f) }
    var redondear by remember { mutableStateOf(false) }
    var confirmado by remember { mutableStateOf(false) }

    Column() {
        Text(
            text = "Notas de ciclo",
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp)
            ,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Desliza para asignar cada nota (0 a 20)",
            Modifier
                .padding(start = 16.dp, bottom = 5.dp),
            color = Color.Gray,
            fontSize = 14.sp,
        )

        ItemCurso(
            name = "Fundamentos de Programación",
            Porcentaje = "(20%)",
            value = nota1,
            onValueChange = { nota1 = it }
        )

        ItemCurso(
            name = "Programación Orientada a Objetos",
            Porcentaje = "(25%)",
            value = nota2,
            onValueChange = { nota2 = it }
        )

        ItemCurso(
            name = "Programación en Móviles",
            Porcentaje = "(30%)",
            value = nota3,
            onValueChange = { nota3 = it }
        )

        ItemCurso(
            name = "Bases de Datos",
            Porcentaje = "(25%)",
            value = nota4,
            onValueChange = { nota4 = it }
        )


        //SwitchPromedio
        var checked by remember { mutableStateOf(true) }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Redondear promedio final",
                modifier = Modifier.weight(1f),
            )
            Switch(
                checked = redondear,
                onCheckedChange = {
                    checked = it
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = confirmado,
                onCheckedChange = { confirmado = it }
            )
            Text(text = "Confirmo que las notas son correctas")
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun ItemCurso(name: String, Porcentaje: String, value: Float, onValueChange: (Float) -> Unit){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Row {
                    Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    val MoradoHeader = null

                    Text(
                        text = Porcentaje,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .padding(vertical = 1.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${value.toInt()}",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                }

                Slider(
                    value = value,
                    onValueChange = onValueChange,
                    valueRange = 0f..20f,
                    modifier = Modifier.width(300.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EjercicioPracticoTheme {
        TitleApp()
    }
}