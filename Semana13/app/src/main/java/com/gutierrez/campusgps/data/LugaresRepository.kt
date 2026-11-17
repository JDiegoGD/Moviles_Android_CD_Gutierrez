package com.gutierrez.campusgps.data

import com.gutierrez.campusgps.model.Lugar

interface LugaresRepository {
    fun obtenerLugares(): List<Lugar>
}
