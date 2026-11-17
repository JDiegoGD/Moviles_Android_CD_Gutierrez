package com.gutierrez.campusgps.data

import com.gutierrez.campusgps.model.Lugar

class LugaresRepositoryLocal : LugaresRepository {
    override fun obtenerLugares(): List<Lugar> = listOf(
        Lugar(1, "Biblioteca", "Estudio", -12.045124069368782, -76.95285201895858),
        Lugar(2, "Auditorio", "Eventos", -12.044772269913807, -76.95280431691197),
        Lugar(3, "Cafetería", "Comida", -12.044062871724222, -76.95218310894187),
        Lugar(4, "Polideportivo", "Deportes", -12.043776332646424, -76.95368754644164),
        Lugar(5, "Estacionamiento", "Servicios", -12.044412683818752, -76.95175670825616)
    )
}
