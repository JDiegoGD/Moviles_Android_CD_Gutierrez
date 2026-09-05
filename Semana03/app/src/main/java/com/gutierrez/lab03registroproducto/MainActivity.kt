package com.gutierrez.lab03registroproducto

import android.R
import android.R.attr.color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gutierrez.lab03registroproducto.ui.theme.Lab03RegistroProductoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab03RegistroProductoTheme {
                PantallaRegistro()
            }
        }
    }
}

@Composable
fun PantallaRegistro(modifier: Modifier = Modifier) {

    //VARIABLES DE ESTADO
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var mostrarResumen by remember { mutableStateOf(false) }

    // NUEVO ESTADO: Almacena el texto del error cuando una validación falla.
    // POR QUÉ: Permite mostrar en pantalla mensajes dinámicos sin alterar las demás variables.
    var mensajeError by remember { mutableStateOf("") }

    Column() {
        Text(
            text = "Registro de producto",
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = "Nuevo producto",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Completa los datos y presiona Agregar",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del producto") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") },
                    modifier = Modifier.weight(1f),
                    // NUEVO: Muestra teclado con punto decimal (123.45).
                    // POR QUÉ: Evita que el usuario ingrese texto o símbolos no numéricos por error.
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedTextField(
                    value = cantidad,
                    onValueChange = { cantidad = it },
                    label = { Text("Cantidad") },
                    modifier = Modifier.weight(1f),
                    // NUEVO: Muestra únicamente teclado de números enteros.
                    // POR QUÉ: Previene la entrada de decimales o letras en la cantidad.
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        // NUEVO: Conversión a tipo numérico seguro (devuelve null si no es un número válido).
                        // POR QUÉ: Evita errores de ejecución (crashes) al operar matemáticamente.
                        val precioNum = precio.toDoubleOrNull()
                        val cantidadNum = cantidad.toIntOrNull()

                        // NUEVO: Estructura de validación robusta con cuando (when).
                        // POR QUÉ: Evalúa en orden lógico los diferentes casos de error posibles.
                        when {
                            // Caso 1: Campos sin llenar.
                            nombre.isBlank() || precio.isBlank() || cantidad.isBlank() -> {
                                mensajeError = "Por favor completa todos los campos."
                                mostrarResumen = false
                            }
                            // Caso 2: El precio contiene caracteres inválidos o es menor/igual a cero.
                            precioNum == null || precioNum <= 0 -> {
                                mensajeError = "El precio debe ser un número mayor a 0."
                                mostrarResumen = false
                            }
                            // Caso 3: La cantidad contiene valores inválidos o es menor/igual a cero.
                            cantidadNum == null || cantidadNum <= 0 -> {
                                mensajeError = "La cantidad debe ser un entero mayor a 0."
                                mostrarResumen = false
                            }
                            // Caso de éxito: Si pasa todas las validaciones, limpia el error y habilita el resumen.
                            else -> {
                                mensajeError = ""
                                mostrarResumen = true
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("AGREGAR PRODUCTO")
                }

                Spacer(modifier = Modifier.width(16.dp))
                // NUEVO BOTÓN LIMPIAR: Reinicia el formulario a su estado original.
                // POR QUÉ: Permite al usuario resetear rápidamente todos los datos ingresados y ocultar alertas.
                OutlinedButton(
                    onClick = {
                        nombre = ""
                        precio = ""
                        cantidad = ""
                        mostrarResumen = false
                        mensajeError = ""
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("LIMPIAR")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            if (mensajeError.isNotEmpty()) {
                Text(
                    text = mensajeError,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            } else if (mostrarResumen) {
                val precioNum = precio.toDoubleOrNull() ?: 0.0
                val cantidadNum = cantidad.toIntOrNull() ?: 0
                val importe = precioNum * cantidadNum
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = nombre,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text("Precio: S/ " + String.format("%.2f", precioNum))
                        Text("Cantidad: " + cantidadNum)
                        Text(
                            text = "Importe total: S/ " + String.format("%.2f", importe),
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("✓ Producto registrado correctamente", color = Color(0xFF2E7D32))
            } else {
                Text(
                    "Aun no has registrado ningún producto",
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Lab03RegistroProductoTheme {
    }
}