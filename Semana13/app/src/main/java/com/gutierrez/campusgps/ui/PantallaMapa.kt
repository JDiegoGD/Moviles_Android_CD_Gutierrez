package com.gutierrez.campusgps.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gutierrez.campusgps.model.UbicacionUi
import com.gutierrez.campusgps.viewmodel.MapaViewModel
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedButton
import com.gutierrez.campusgps.util.formatearDistancia
import com.gutierrez.campusgps.model.TipoError
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.statusBarsPadding
import com.gutierrez.campusgps.R
import com.gutierrez.campusgps.ui.theme.NavyBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    val estado by vm.estado.collectAsState()
    val ultimaPosicion by vm.ultimaPosicion.collectAsState()
    val consulta by vm.consulta.collectAsState()
    val categoriaSeleccionada by vm.categoriaSeleccionada.collectAsState()
    val categorias by vm.categorias.collectAsState()
    val lugaresFiltrados by vm.lugaresFiltrados.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()
    val favoritos by vm.favoritos.collectAsState()
    val soloFavoritos by vm.soloFavoritos.collectAsState()
    val errorEscrituraFavoritos by vm.errorEscrituraFavoritos.collectAsState()

    val contexto = LocalContext.current

    val lanzadorPermisos = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        val concedido =
            resultado[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    resultado[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (concedido) {
            vm.buscar()
        } else {
            vm.permisoDenegado()
        }
    }

    val alPulsarUbicacion: () -> Unit = {
        val permisoPreciso = ContextCompat.checkSelfPermission(
            contexto,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val permisoAproximado = ContextCompat.checkSelfPermission(
            contexto,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (permisoPreciso || permisoAproximado) {
            vm.buscar()
        } else {
            lanzadorPermisos.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyBlue)
                    .statusBarsPadding()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.campus_topbar),
                    contentDescription = "CampusGPS Top Bar",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1274f / 192f),
                    contentScale = ContentScale.Fit
                )
            }
        }
    ) { espacio ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(espacio)
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Search field
            OutlinedTextField(
                value = consulta,
                onValueChange = { vm.actualizarConsulta(it) },
                label = { Text("Buscar lugar") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    if (consulta.isNotEmpty()) {
                        IconButton(onClick = { vm.actualizarConsulta("") }) {
                            Text("✕")
                        }
                    }
                }
            )

            // Single LazyRow for "Favoritos" and Categories
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                item(key = "favoritos_chip") {
                    FilterChip(
                        selected = soloFavoritos,
                        onClick = { vm.alternarSoloFavoritos() },
                        label = { Text("Favoritos") },
                        leadingIcon = {
                            Text(text = "★")
                        }
                    )
                }

                items(categorias) { cat ->
                    val seleccionada = (cat == "Todas" && (categoriaSeleccionada == null || categoriaSeleccionada == "Todas")) ||
                            (cat == categoriaSeleccionada)
                    FilterChip(
                        selected = seleccionada,
                        onClick = {
                            vm.seleccionarCategoria(if (cat == "Todas") null else cat)
                        },
                        label = { Text(cat) }
                    )
                }
            }

            errorEscrituraFavoritos?.let { mensajeError ->
                Text(
                    text = mensajeError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            val sinPlayServices = (estado as? UbicacionUi.Error)?.tipo == TipoError.SIN_PLAY_SERVICES

            if (sinPlayServices) {
                Text(
                    text = "El mapa requiere Google Play Services",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Location content: Map absorbs extra vertical space via weight(1f), or state illustration
            if (ultimaPosicion != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    MapaUbicacion(
                        estado = estado,
                        ultimaPosicion = ultimaPosicion,
                        lugares = lugaresFiltrados,
                        seleccionado = seleccionado,
                        masCercanoId = masCercano?.id,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )

                    when (val actual = estado) {
                        UbicacionUi.Cancelada -> {
                            Text("Búsqueda cancelada", style = MaterialTheme.typography.bodySmall, color = NavyBlue)
                        }
                        is UbicacionUi.Error -> {
                            Text(actual.mensaje, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                        }
                        is UbicacionUi.Lista -> {
                            Text(
                                text = "Lat: ${actual.posicion.latitud} · Lon: ${actual.posicion.longitud}",
                                style = MaterialTheme.typography.bodySmall,
                                color = NavyBlue
                            )
                        }
                        else -> Unit
                    }
                }
            } else {
                when (val actual = estado) {
                    UbicacionUi.Inicial -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.campus_inicio),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp),
                                contentScale = ContentScale.Fit
                            )
                            Text("Pulsa el botón para empezar", style = MaterialTheme.typography.bodySmall, color = NavyBlue)
                        }
                    }
                    UbicacionUi.Cancelada -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.campus_inicio),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp),
                                contentScale = ContentScale.Fit
                            )
                            Text("Búsqueda cancelada", style = MaterialTheme.typography.bodySmall, color = NavyBlue)
                        }
                    }
                    is UbicacionUi.Error -> {
                        val imgRes = when (actual.tipo) {
                            TipoError.GPS_APAGADO -> R.drawable.campus_gps_desactivado
                            else -> R.drawable.campus_error
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = imgRes),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp),
                                contentScale = ContentScale.Fit
                            )
                            MensajeError(
                                error = actual,
                                onReintentar = alPulsarUbicacion
                            )
                        }
                    }
                    else -> Box(modifier = Modifier.fillMaxWidth().weight(1f))
                }
            }

            // Centralized Location Action / Search Status Area (No spinner, exact text "Buscando ubicación....")
            if (estado is UbicacionUi.Buscando) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "Buscando ubicación....",
                        color = NavyBlue,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .semantics {
                                liveRegion = LiveRegionMode.Polite
                                contentDescription = "Buscando ubicación"
                            }
                    )

                    OutlinedButton(
                        onClick = { vm.cancelarBusqueda() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancelar búsqueda")
                    }
                }
            } else {
                Button(
                    onClick = alPulsarUbicacion,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Obtener ubicación", textAlign = TextAlign.Center)
                }
            }

            // Go to closest place action & text
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {
                        masCercano?.let { lugar ->
                            vm.seleccionar(lugar.id)
                        }
                    },
                    enabled = masCercano != null,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Ir al más cercano")
                }

                masCercano?.let { lugar ->
                    val metros = lugaresFiltrados
                        .firstOrNull { it.lugar.id == lugar.id }
                        ?.metros

                    if (metros != null) {
                        Text(
                            text = "${lugar.nombre} (${formatearDistancia(metros)})",
                            style = MaterialTheme.typography.bodySmall,
                            color = NavyBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Places List / Empty state (LazyColumn with bounded height height(130.dp))
            if (lugaresFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val mensajeVacio = if (soloFavoritos && favoritos.isEmpty()) {
                        "No hay favoritos marcados"
                    } else {
                        "No se encontraron lugares"
                    }
                    Text(
                        text = mensajeVacio,
                        style = MaterialTheme.typography.bodyMedium,
                        color = NavyBlue
                    )
                }
            } else {
                ListaLugares(
                    lugares = lugaresFiltrados,
                    seleccionado = seleccionado,
                    favoritos = favoritos,
                    onClick = { id -> vm.seleccionar(id) },
                    onToggleFavorito = { id -> vm.alternarFavorito(id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                )
            }

            // Footer with author identification (compact)
            Text(
                text = "Gutierrez Duran Juan Diego - C24D",
                style = MaterialTheme.typography.bodySmall,
                color = NavyBlue,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}
