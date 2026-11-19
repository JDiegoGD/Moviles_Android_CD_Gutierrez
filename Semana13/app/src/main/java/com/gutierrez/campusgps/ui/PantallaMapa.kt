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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    val estado by vm.estado.collectAsState()
    val lugares by vm.lugares.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()

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
                    lugares = lugares,
                    seleccionado = seleccionado,
                    masCercanoId = masCercano?.id,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }



            Button(
                onClick = alPulsarUbicacion, // Se sustituye por el flujo de permisos en el paso 2.
                enabled = estado != UbicacionUi.Buscando
            ) {
                Text("Obtener mi ubicación")
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
                val metros = lugares
                    .firstOrNull { it.lugar.id == lugar.id }
                    ?.metros

                if (metros != null) {
                    Text(
                        text = "Más cercano: ${lugar.nombre} " +
                                "(${formatearDistancia(metros)})"
                    )
                }
            }

            ListaLugares(
                lugares = lugares,
                seleccionado = seleccionado,
                onClick = { id -> vm.seleccionar(id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
        }
    }
}
