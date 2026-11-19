package com.gutierrez.campusgps.util

import android.location.Location
import com.gutierrez.campusgps.model.Lugar
import com.gutierrez.campusgps.model.Posicion

fun distanciaMetros(desde: Posicion, hasta: Lugar): Float {
    val resultado = FloatArray(1)

    Location.distanceBetween(
        desde.latitud,
        desde.longitud,
        hasta.latitud,
        hasta.longitud,
        resultado
    )

    return resultado[0]
}