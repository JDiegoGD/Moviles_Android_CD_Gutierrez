package com.gutierrez.campusgps.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
            TopAppBar(
                title = { Text("CampusGPS") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { espacio ->
        Column(
            modifier = Modifier.fillMaxSize().padding(espacio).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    selected = soloFavoritos,
                    onClick = { vm.alternarSoloFavoritos() },
                    label = { Text("Solo favoritos") }
                )
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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

            val sinPlayServices =
                (estado as? UbicacionUi.Error)?.tipo ==
                        TipoError.SIN_PLAY_SERVICES

            if (sinPlayServices) {
                Text(
                    text = "El mapa requiere Google Play Services",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            } else {
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
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = alPulsarUbicacion,
                    enabled = estado !is UbicacionUi.Buscando,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Obtener mi ubicación")
                }

                OutlinedButton(
                    onClick = { vm.cancelarBusqueda() },
                    enabled = estado is UbicacionUi.Buscando,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar búsqueda")
                }
            }

            when (val actual = estado) {
                UbicacionUi.Inicial -> Text("Pulsa el botón para obtener tu ubicación")
                UbicacionUi.Buscando -> {
                    CircularProgressIndicator()
                    Text("Buscando ubicación…")
                }
                is UbicacionUi.Lista -> {
                    Text(
                        text = "Lat: ${actual.posicion.latitud} · " +
                                "Lon: ${actual.posicion.longitud}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                UbicacionUi.Cancelada -> {
                    Text("Búsqueda cancelada")
                }
                is UbicacionUi.Error -> MensajeError(
                    error = actual,
                    onReintentar = alPulsarUbicacion
                )
            }

            OutlinedButton(
                onClick = {
                    masCercano?.let { lugar ->
                        vm.seleccionar(lugar.id)
                    }
                },
                enabled = masCercano != null
            ) {
                Text("Ir al más cercano")
            }

            masCercano?.let { lugar ->
                val metros = lugaresFiltrados
                    .firstOrNull { it.lugar.id == lugar.id }
                    ?.metros

                if (metros != null) {
                    Text(
                        text = "Más cercano: ${lugar.nombre} " +
                                "(${formatearDistancia(metros)})"
                    )
                }
            }

            if (lugaresFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
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
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
                        .height(180.dp)
                )
            }
        }
    }
}
