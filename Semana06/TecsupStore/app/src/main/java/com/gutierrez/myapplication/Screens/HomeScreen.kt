package com.gutierrez.myapplication.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gutierrez.myapplication.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var expandedTopBar by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "TECSUP STORE") }
            )
        }
    ) { innerPadding -> // Aplicamos los márgenes requeridos por el Scaffold
        Box(modifier = Modifier.padding(innerPadding)) {
            CardProduct(
                name = "Audifonos",
                precio = 68.00,
                navController = navController
            )
        }
    }
}

@Composable
fun CardProduct(name: String, precio: Double, navController: NavController) {
    var expandedCard by remember { mutableStateOf(false) }

    val purplePrimary = Color(0xFF6A1B9A)
    val purpleBackground = Color(0xFFF3E5F5)
    val cardBackground = Color(0xFFFAFAFC)
    val strokeColor = Color(0xFF4A148C)

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = cardBackground),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(strokeColor),
            width = 1.5.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Contenedor cuadrado del ícono izquierdo
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(color = purpleBackground, shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = "Icono de producto",
                    tint = purplePrimary,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 2. Textos centrales (Título y Precio)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "S/ ${String.format("%.2f", precio)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = purplePrimary
                )
            }

            // 3. Botón para abrir el menú de la tarjeta
            Box {
                IconButton(onClick = { expandedCard = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Opciones de producto"
                    )
                }

                DropdownMenu(
                    expanded = expandedCard,
                    onDismissRequest = { expandedCard = false }
                ) {
                    DropdownMenuItem(
                        onClick = { expandedCard = false },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = "Favoritos"
                            )
                        },
                        text = { Text("Favoritos") }
                    )
                    DropdownMenuItem(
                        onClick = {
                            expandedCard = false
                            navController.navigate(Screen.Home.route)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = "Compartir"
                            )
                        },
                        text = { Text("Compartir") }
                    )
                    DropdownMenuItem(
                        onClick = {
                            expandedCard = false
                            navController.navigate(Screen.Home.route)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.ReportProblem,
                                contentDescription = "Reportar"
                            )
                        },
                        text = { Text("Home") }
                    )
                }
            }
        }
    }
}

@Composable
fun DropdownDefault(expanded: Boolean, onDismiss: () -> Unit, navController: NavController) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(
            onClick = onDismiss,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Favoritos"
                )
            },
            text = { Text("Favoritos") }
        )
        DropdownMenuItem(
            onClick = {
                onDismiss()
                navController.navigate(Screen.Home.route)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = "Compartir"
                )
            },
            text = { Text("Compartir") }
        )
        DropdownMenuItem(
            onClick = {
                onDismiss()
                navController.navigate(Screen.Home.route)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.ReportProblem,
                    contentDescription = "Reportar"
                )
            },
            text = { Text("Home") }
        )
    }
}
