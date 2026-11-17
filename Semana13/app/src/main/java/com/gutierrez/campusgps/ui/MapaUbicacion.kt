package com.gutierrez.campusgps.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text("Mapa pendiente: paso 3")
    }
    // TODO Paso 3: rememberCameraPositionState, GoogleMap y marcador propio rojo.
    // TODO Paso 4: marcadores azules para los lugares, con key(id).
    // TODO Paso 6: animar la cámara al cambiar seleccionado.
    // TODO Paso 7: representar en verde el lugar más cercano.
}
