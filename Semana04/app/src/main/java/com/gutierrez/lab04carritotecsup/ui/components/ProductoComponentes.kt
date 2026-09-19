package com.gutierrez.lab04carritotecsup.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gutierrez.lab04carritotecsup.Producto



@Composable
fun PantallaCarrito(modifier: Modifier = Modifier) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var mostrarResumen by remember { mutableStateOf(false)}
    val productos = remember { mutableStateListOf<Producto>() }

    Column {
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
        }

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
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedTextField(
                    value = cantidad,
                    onValueChange = { cantidad = it },
                    label = { Text("Cantidad") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    val precioNum = precio.toDoubleOrNull() ?: 0.0
                    val cantidadNum = cantidad.toIntOrNull() ?: 0
                    if (nombre.isNotBlank() && precioNum > 0 && cantidadNum > 0) {
                        productos.add(Producto(nombre, precioNum, cantidadNum))
                        // TODO: limpia los 3 campos asignándoles ""
                        nombre = ""
                        precio = ""
                        cantidad = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("AGREGAR")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Productos: ${productos.size}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )


        }
    }
}


