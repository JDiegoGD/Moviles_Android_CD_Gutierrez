package com.gutierrez.biblioteca.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

/** Pestanas de la barra inferior. */
enum class Pestana { INICIO, FAVORITOS, CARRITO }

/** Barra de navegacion inferior (Inicio, Favoritos, Carrito, Salir). */
@Composable
fun BarraInferior(
    seleccionada: Pestana,
    cantidadCarrito: Int = 0,
    onInicio: () -> Unit,
    onFavoritos: () -> Unit,
    onCarrito: () -> Unit,
    onSalir: () -> Unit
) {
    val colores = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.primary,
        selectedTextColor = MaterialTheme.colorScheme.primary,
        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = seleccionada == Pestana.INICIO,
            onClick = onInicio,
            icon = { Icon(Icons.Filled.Home, contentDescription = null) },
            label = { Text("Inicio") },
            colors = colores
        )
        NavigationBarItem(
            selected = seleccionada == Pestana.FAVORITOS,
            onClick = onFavoritos,
            icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
            label = { Text("Favoritos") },
            colors = colores
        )
        NavigationBarItem(
            selected = seleccionada == Pestana.CARRITO,
            onClick = onCarrito,
            icon = {
                if (cantidadCarrito > 0) {
                    BadgedBox(
                        badge = { Badge { Text("$cantidadCarrito") } }
                    ) {
                        Icon(Icons.Filled.ShoppingCart, contentDescription = null)
                    }
                } else {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = null)
                }
            },
            label = { Text("Carrito") },
            colors = colores
        )
        NavigationBarItem(
            selected = false,
            onClick = onSalir,
            icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null) },
            label = { Text("Salir") },
            colors = colores
        )
    }
}
