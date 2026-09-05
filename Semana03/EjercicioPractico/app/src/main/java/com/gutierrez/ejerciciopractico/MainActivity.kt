package com.gutierrez.ejerciciopractico

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gutierrez.ejerciciopractico.ui.theme.EjercicioPracticoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EjercicioPracticoTheme {
                Column() {
                    TitleApp()
                    SubTitleApp()
                }
            }
        }
    }
}

@Composable
fun TitleApp(){
    Column() {
        Text(
            text = "Registro de producto",
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
    Column() {
        Text(
            text = "Notas de ciclo",
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 5.dp, start = 16.dp )
            ,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Desliza para asignar cada nota (0 a 20)",
            Modifier
                .padding(start = 16.dp),
            color = Color.Gray,
            fontSize = 15.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EjercicioPracticoTheme {
        TitleApp()
    }
}