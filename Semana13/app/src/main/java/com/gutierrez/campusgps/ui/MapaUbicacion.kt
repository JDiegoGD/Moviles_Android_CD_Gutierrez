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
import androidx.compose.runtime.key
import com.google.android.gms.maps.model.BitmapDescriptorFactory

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

    val lugarSeleccionado = lugares
        .firstOrNull { it.lugar.id == seleccionado }
        ?.lugar

    LaunchedEffect(lugarSeleccionado, mapaCargado) {
        val lugar = lugarSeleccionado

        if (mapaCargado && lugar != null) {
            camara.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    LatLng(lugar.latitud, lugar.longitud),
                    17f
                ),
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

        lugares.forEach { lugarConDistancia ->
            val lugar = lugarConDistancia.lugar

            key(lugar.id) {
                val marcador = rememberUpdatedMarkerState(
                    position = LatLng(lugar.latitud, lugar.longitud)
                )

                Marker(
                    state = marcador,
                    title = lugar.nombre,
                    snippet = lugar.categoria,
                    icon = BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_AZURE
                    )
                )

                LaunchedEffect(seleccionado, mapaCargado) {
                    if (mapaCargado && seleccionado == lugar.id) {
                        marcador.showInfoWindow()
                    } else {
                        marcador.hideInfoWindow()
                    }
                }
            }
        }
        // TODO Paso 6: mover la cámara al lugar seleccionado.
        // TODO Paso 7: representar en verde el lugar más cercano.
    }
}