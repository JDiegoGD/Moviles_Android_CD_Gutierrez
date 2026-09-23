package com.gutierrez.clinicasalud.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gutierrez.clinicasalud.datos.DatosClinica
import com.gutierrez.clinicasalud.datos.Medico
import com.gutierrez.clinicasalud.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, onMenuClick: () -> Unit) {

    // Estado para la especialidad seleccionada y el texto de búsqueda
    var especialidadSeleccionada by remember { mutableStateOf("Todos") }
    var textoBusqueda by remember { mutableStateOf("") }

    // Obtener los médicos filtrados combinando texto y especialidad
    val medicosFiltrados = DatosClinica.buscarMedicos(textoBusqueda, especialidadSeleccionada)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Clínica Salud+", fontWeight = FontWeight.Bold)
                        Text("Hola, Juan", style = MaterialTheme.typography.bodySmall)
                    }
                },
                navigationIcon = {
                    // Ícono que abrirá el menú lateral
                    IconButton(onClick = onMenuClick) {
                        Icon(imageVector = Icons.Filled.Menu, contentDescription = "Abrir menú")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Campo de texto para la búsqueda de médicos o especialidad
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                placeholder = { Text("Buscar médico o especialidad") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Buscar"
                    )
                },
                trailingIcon = {
                    // Botón para limpiar el texto de búsqueda, visible solo si hay contenido
                    if (textoBusqueda.isNotEmpty()) {
                        IconButton(onClick = { textoBusqueda = "" }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Limpiar búsqueda"
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(DatosClinica.especialidades) { especialidad ->
                    FilterChip(
                        selected = especialidad == especialidadSeleccionada,
                        onClick = { especialidadSeleccionada = especialidad },
                        label = { Text(especialidad) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            Text(
                text = "Médicos disponibles",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Lista principal de médicos
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(medicosFiltrados) { medico ->
                    MedicoCard(
                        medico = medico,
                        onClick = {
                            // Navega al perfil enviando el id del médico elegido
                            navController.navigate(Screen.Detail.createRoute(medico.id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MedicoCard(medico: Medico, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Nombre y especialidad
            Column(modifier = Modifier.weight(1f)) {
                Text(medico.nombre, fontWeight = FontWeight.Bold)
                Text(
                    text = medico.especialidad,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Calificación
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Calificación",
                tint = Color(0xFFF5B301),
                modifier = Modifier.size(16.dp)
            )
            Text(" ${medico.calificacion}", style = MaterialTheme.typography.bodySmall)
        }
    }
}