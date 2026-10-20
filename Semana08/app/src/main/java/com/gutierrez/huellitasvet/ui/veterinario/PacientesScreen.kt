package com.gutierrez.huellitasvet.ui.veterinario

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gutierrez.huellitasvet.model.Especie
import com.gutierrez.huellitasvet.model.Formato
import com.gutierrez.huellitasvet.ui.components.AppTopBar
import com.gutierrez.huellitasvet.ui.components.MascotaAvatar
import com.gutierrez.huellitasvet.ui.components.OpcionChip
import com.gutierrez.huellitasvet.ui.components.VetButton
import com.gutierrez.huellitasvet.ui.components.VetTextField
import com.gutierrez.huellitasvet.ui.components.etiquetaEspecie
import com.gutierrez.huellitasvet.viewmodel.PacientesViewModel

@Composable
fun PacientesScreen(vm: PacientesViewModel, onVerMascota: (Int) -> Unit) {
    val pacientes by vm.pacientes.collectAsState()
    val texto by vm.texto.collectAsState()
    val especie by vm.especie.collectAsState()
    val conteos by vm.conteos.collectAsState()

    Scaffold(
        topBar = { AppTopBar("Pacientes") },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Column(Modifier.padding(padding)) {
            VetTextField(
                texto, vm::cambiarTexto, "Buscar mascota, raza o dueño…",
                Modifier.padding(horizontal = 16.dp), icono = Icons.Filled.Search
            )
            Row(
                Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OpcionChip("Todos (${conteos[null] ?: 0})", especie == null, { vm.cambiarEspecie(null) })
                OpcionChip("Perros (${conteos[Especie.PERRO] ?: 0})", especie == Especie.PERRO, { vm.cambiarEspecie(Especie.PERRO) })
                OpcionChip("Gatos (${conteos[Especie.GATO] ?: 0})", especie == Especie.GATO, { vm.cambiarEspecie(Especie.GATO) })
                OpcionChip("Otros (${conteos[Especie.OTRO] ?: 0})", especie == Especie.OTRO, { vm.cambiarEspecie(Especie.OTRO) })
            }
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (pacientes.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp, horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "No se encontraron pacientes",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            VetButton(
                                texto = "Limpiar filtros",
                                onClick = vm::limpiar,
                                secundario = true
                            )
                        }
                    }
                } else {
                    items(pacientes, key = { it.mascota.id }) { p ->
                        Card(
                            onClick = { onVerMascota(p.mascota.id) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                MascotaAvatar(p.mascota.especie, tamano = 52.dp)
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(p.mascota.nombre, fontWeight = FontWeight.Bold)
                                    Text(
                                        "${etiquetaEspecie(p.mascota.especie)} · ${p.mascota.raza} · ${Formato.edad(p.mascota.anioNacimiento)} años",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        "Dueño: ${p.dueno.nombre}", style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
