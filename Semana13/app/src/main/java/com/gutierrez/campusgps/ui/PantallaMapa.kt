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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    val estado by vm.estado.collectAsState()
    val lugares by vm.lugares.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()

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

    // Si hay permiso: vm.buscar(); si se deniega: vm.permisoDenegado().
    // TODO Pasos 3 a 7: observar lugares, seleccionado y masCercano, e integrar mapa/lista.

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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
                    Text("Latitud: ${actual.posicion.latitud}")
                    Text("Longitud: ${actual.posicion.longitud}")
                }
                is UbicacionUi.Error -> Text(
                    text = actual.mensaje,
                    color = MaterialTheme.colorScheme.error
                )
            }

            MapaUbicacion(
                estado = estado,
                lugares = lugares,
                seleccionado = seleccionado,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            ListaLugares(
                lugares = lugares,
                seleccionado = seleccionado,
                onClick = { id -> vm.seleccionar(id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
        }
    }
}
