package com.gutierrez.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.gutierrez.huellitasvet.data.local.entity.Servicio
import com.gutierrez.huellitasvet.ui.components.AppTopBar
import com.gutierrez.huellitasvet.ui.components.FilaSwitch
import com.gutierrez.huellitasvet.ui.components.OpcionChip
import com.gutierrez.huellitasvet.ui.components.VetButton
import com.gutierrez.huellitasvet.ui.components.VetTextField
import com.gutierrez.huellitasvet.ui.components.iconoServicio
import com.gutierrez.huellitasvet.viewmodel.ServiciosViewModel

/** Sirve para crear (id = null) y para editar un servicio. */
@Composable
fun FormularioServicioScreen(id: Int?, vm: ServiciosViewModel, onBack: () -> Unit) {
    val servicios by vm.servicios.collectAsState()
    val errores by vm.errores.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val snackbar = remember { SnackbarHostState() }
    val existente = servicios.firstOrNull { it.id == id }

    var nombre by remember { mutableStateOf("") }
    var duracion by remember { mutableStateOf(30) }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var icono by remember { mutableStateOf("heart") }
    var activo by remember { mutableStateOf(true) }

    // Al editar, rellena los campos una sola vez
    LaunchedEffect(existente?.id) {
        existente?.let {
            nombre = it.nombre; duracion = it.duracionMin; precio = it.precio.toString()
            descripcion = it.descripcion; icono = it.icono; activo = it.activo
        }
    }
    LaunchedEffect(mensaje) {
        mensaje?.let { snackbar.showSnackbar(it); vm.limpiarMensaje() }
    }
    DisposableEffect(Unit) { onDispose { vm.limpiarErrores() } }

    Scaffold(
        topBar = { AppTopBar(if (id == null) "Nuevo servicio" else "Editar servicio", onBack) },
        snackbarHost = { SnackbarHost(snackbar) },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            VetTextField(nombre, { nombre = it }, "Nombre del servicio", error = errores["nombre"])

            Text("Duración", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(15, 30, 45, 60).forEach { d ->
                    OpcionChip("$d min", duracion == d, { duracion = d }, Modifier.weight(1f))
                }
            }

            VetTextField(
                precio, { precio = it }, "Precio (S/)",
                error = errores["precio"], teclado = KeyboardType.Decimal
            )
            VetTextField(descripcion, { descripcion = it }, "Descripción", lineas = 3)

            Text("Ícono", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf("heart", "syringe", "scissors").forEach { nombreIcono ->
                    val sel = icono == nombreIcono
                    Surface(
                        onClick = { icono = nombreIcono },
                        shape = RoundedCornerShape(14.dp),
                        color = if (sel) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, if (sel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline),
                        modifier = Modifier.weight(1f).height(52.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(iconoServicio(nombreIcono), null, tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }

            Surface(shape = RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.surface, modifier = Modifier.fillMaxWidth()) {
                FilaSwitch(
                    "Servicio activo", "Los dueños pueden pedirlo", activo, { activo = it },
                    Modifier.padding(horizontal = 14.dp)
                )
            }

            VetButton("Guardar servicio", onClick = {
                val servicio = Servicio(
                    id = existente?.id ?: 0,
                    nombre = nombre,
                    descripcion = descripcion,
                    duracionMin = duracion,
                    precio = precio.replace(',', '.').toDoubleOrNull() ?: 0.0,
                    activo = activo,
                    icono = icono
                )
                vm.guardar(servicio, onBack)
            })
            VetButton("Cancelar", onClick = onBack, secundario = true)
            if (existente != null) {
                VetButton(
                    "Eliminar servicio",
                    onClick = { vm.eliminar(existente, onBack) },
                    secundario = true, peligro = true
                )
            }
        }
    }
}
