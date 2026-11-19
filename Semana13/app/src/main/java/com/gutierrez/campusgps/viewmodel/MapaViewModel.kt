package com.gutierrez.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutierrez.campusgps.data.LugaresRepository
import com.gutierrez.campusgps.data.UbicacionRepository
import com.gutierrez.campusgps.model.LugarConDistancia
import com.gutierrez.campusgps.model.UbicacionUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.gutierrez.campusgps.model.Posicion
import com.gutierrez.campusgps.util.distanciaMetros
class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModel() {
    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)
    val estado: StateFlow<UbicacionUi> = _estado.asStateFlow()

    private val _lugares = MutableStateFlow(
        lugaresRepo.obtenerLugares().map { LugarConDistancia(it, null) }
    )
    val lugares: StateFlow<List<LugarConDistancia>> = _lugares.asStateFlow()

    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado: StateFlow<Int?> = _seleccionado.asStateFlow()

    private fun actualizarDistancias(posicion: Posicion) {
        _lugares.value = lugaresRepo.obtenerLugares()
            .map { lugar ->
                LugarConDistancia(
                    lugar = lugar,
                    metros = distanciaMetros(posicion, lugar)
                )
            }
            .sortedBy { it.metros }
    }
    fun buscar() {
        if (_estado.value == UbicacionUi.Buscando) return
        _estado.value = UbicacionUi.Buscando
        viewModelScope.launch {
            ubicacionRepo.obtenerPosicion()
                .onSuccess { posicion ->
                    actualizarDistancias(posicion)
                    _lugares.value.forEach { item ->
                        android.util.Log.d(
                            "CampusGPS",
                            "${item.lugar.nombre}: ${item.metros} m"
                        )
                    }
                    _estado.value = UbicacionUi.Lista(posicion)
                }
                .onFailure { error ->
                    _estado.value = UbicacionUi.Error(mensajeDe(error))
                }
        }
    }

    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error(
            "Sin permiso de ubicación no puedo mostrar dónde estás"
        )
    }

    fun seleccionar(id: Int?) {
        _seleccionado.value = id
    }
    // TODO Paso 7: val masCercano: StateFlow<Lugar?>, derivado de lugares.
    // TODO Paso 8: distinguir tipos de error y comprobar Google Play Services.

    private fun mensajeDe(error: Throwable): String = when (error) {
        is SecurityException -> "Falta el permiso de ubicación"
        else -> error.message ?: "No se pudo obtener la ubicación"
    }
}
