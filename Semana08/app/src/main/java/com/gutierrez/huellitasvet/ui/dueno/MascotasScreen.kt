package com.gutierrez.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gutierrez.huellitasvet.model.Formato
import com.gutierrez.huellitasvet.ui.components.AppTopBarScroll
import com.gutierrez.huellitasvet.ui.components.MascotaAvatar
import com.gutierrez.huellitasvet.ui.components.etiquetaEspecie
import com.gutierrez.huellitasvet.viewmodel.InicioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MascotasScreen(
    vm: InicioViewModel,
    onVerMascota: (Int) -> Unit,
    onAgregar: () -> Unit
) {
    val mascotas by vm.mascotas.collectAsState()
    // La barra de arriba se esconde al bajar la lista y reaparece al subir
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { AppTopBarScroll("Mis mascotas", scrollBehavior) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAgregar,
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("Agregar") }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        if (mascotas.isEmpty()) {
            Text(
                "Aún no tienes mascotas registradas",
                Modifier.padding(padding).padding(24.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        LazyColumn(
            Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(mascotas, key = { it.id }) { m ->
                Card(
                    onClick = { onVerMascota(m.id) },
                    modifier = Modifier.fillMaxWidth().animateItem(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        MascotaAvatar(m.especie, tamano = 56.dp)
                        Column {
                            Text(m.nombre, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Text(
                                "${etiquetaEspecie(m.especie)} · ${m.raza} · ${Formato.edad(m.anioNacimiento)} años",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
