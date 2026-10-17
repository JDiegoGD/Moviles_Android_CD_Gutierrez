package com.gutierrez.huellitasvet.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.ui.graphics.vector.ImageVector

/** Convierte el texto guardado en Servicio.icono (heart, syringe, scissors) a un icono. */
fun iconoServicio(icono: String): ImageVector = when (icono) {
    "syringe" -> Icons.Filled.Vaccines
    "scissors" -> Icons.Filled.ContentCut
    else -> Icons.Filled.Favorite
}
