package com.gutierrez.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.gutierrez.campusgps.model.LugarConDistancia
import com.gutierrez.campusgps.model.UbicacionUi

@Suppress("UNUSED_PARAMETER")
@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    modifier: Modifier = Modifier
) {
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(-12.0464, -77.0428),
            12f
        )
    }

    var mapaCargado by remember { mutableStateOf(false) }

    LaunchedEffect(estado, mapaCargado) {
        if (mapaCargado && estado is UbicacionUi.Lista) {
            val posicion = LatLng(
                estado.posicion.latitud,
                estado.posicion.longitud
            )

            camara.animate(
                update = CameraUpdateFactory.newLatLngZoom(posicion, 16f),
                durationMs = 1_000
            )
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = camara,
        onMapLoaded = { mapaCargado = true }
    ) {
        if (estado is UbicacionUi.Lista) {
            val posicion = LatLng(
                estado.posicion.latitud,
                estado.posicion.longitud
            )

            Marker(
                state = rememberUpdatedMarkerState(position = posicion),
                title = "Estás aquí"
            )
        }

        // TODO Paso 4: dibujar los marcadores azules de los lugares.
        // TODO Paso 6: mover la cámara al lugar seleccionado.
        // TODO Paso 7: representar en verde el lugar más cercano.
    }
}