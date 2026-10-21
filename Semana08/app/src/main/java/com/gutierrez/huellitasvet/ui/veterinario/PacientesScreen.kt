package com.gutierrez.huellitasvet.ui.veterinario

import com.gutierrez.huellitasvet.R
import com.gutierrez.huellitasvet.ui.components.EstadoVacio

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gutierrez.huellitasvet.model.Especie
import com.gutierrez.huellitasvet.model.Formato
import com.gutierrez.huellitasvet.ui.components.AppTopBarScroll
import com.gutierrez.huellitasvet.ui.components.MascotaAvatar
import com.gutierrez.huellitasvet.ui.components.OpcionChip
import com.gutierrez.huellitasvet.ui.components.VetTextField
import com.gutierrez.huellitasvet.ui.components.etiquetaEspecie
import com.gutierrez.huellitasvet.viewmodel.PacientesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacientesScreen(vm: PacientesViewModel, onVerMascota: (Int) -> Unit) {
    val pacientes by vm.pacientes.collectAsState()
    val texto by vm.texto.collectAsState()
    val especie by vm.especie.collectAsState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { AppTopBarScroll("Pacientes", scrollBehavior) },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Column(Modifier.padding(padding)) {
            VetTextField(
                texto, vm::cambiarTexto, "Buscar mascota o dueño…",
                Modifier.padding(horizontal = 16.dp), icono = Icons.Filled.Search
            )
            Row(
                Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OpcionChip("Todos", especie == null, { vm.cambiarEspecie(null) })
                OpcionChip("Perros", especie == Especie.PERRO, { vm.cambiarEspecie(Especie.PERRO) })
                OpcionChip("Gatos", especie == Especie.GATO, { vm.cambiarEspecie(Especie.GATO) })
                OpcionChip("Otros", especie == Especie.OTRO, { vm.cambiarEspecie(Especie.OTRO) })
            }
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (pacientes.isEmpty()) {
                    item {
                        EstadoVacio(R.drawable.vacio_pacientes, "No hay resultados")
                    }
                }
                items(pacientes, key = { it.mascota.id }) { p ->
                    Card(
                        onClick = { onVerMascota(p.mascota.id) },
                        modifier = Modifier.fillMaxWidth().animateItem(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
