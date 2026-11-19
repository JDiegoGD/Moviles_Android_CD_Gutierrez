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
import com.gutierrez.campusgps.model.Lugar
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import com.gutierrez.campusgps.data.UbicacionNoDisponibleException
import com.gutierrez.campusgps.model.TipoError
import kotlinx.coroutines.flow.combine
import com.gutierrez.campusgps.util.normalizarTexto

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

    private val _consulta = MutableStateFlow("")
    val consulta: StateFlow<String> = _consulta.asStateFlow()

    private val _categoriaSeleccionada = MutableStateFlow<String?>("Todas")
    val categoriaSeleccionada: StateFlow<String?> = _categoriaSeleccionada.asStateFlow()

    val categorias: StateFlow<List<String>> = MutableStateFlow(
        listOf("Todas") + lugaresRepo.obtenerLugares().map { it.categoria }.distinct()
    ).asStateFlow()

    val lugaresFiltrados: StateFlow<List<LugarConDistancia>> = combine(
        _lugares,
        _consulta,
        _categoriaSeleccionada
    ) { lista, consultaTexto, categoria ->
        val queryNorm = normalizarTexto(consultaTexto)
        lista.filter { item ->
            val lugar = item.lugar
            val coincideNombre = queryNorm.isEmpty() || normalizarTexto(lugar.nombre).contains(queryNorm)
            val coincideCategoria = categoria == null || categoria == "Todas" || lugar.categoria.equals(categoria, ignoreCase = true)
            coincideNombre && coincideCategoria
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = lugaresRepo.obtenerLugares().map { LugarConDistancia(it, null) }
    )

    val masCercano: StateFlow<Lugar?> = lugaresFiltrados
        .map { lista ->
            lista.firstOrNull { it.metros != null }?.lugar
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado: StateFlow<Int?> = _seleccionado.asStateFlow()

    init {
        if (!ubicacionRepo.playServicesDisponible()) {
            _estado.value = errorPlayServices()
        }
        viewModelScope.launch {
            lugaresFiltrados.collect { filtrados ->
                val sel = _seleccionado.value
                if (sel != null && filtrados.none { it.lugar.id == sel }) {
                    _seleccionado.value = null
                }
            }
        }
    }

    private fun errorPlayServices() = UbicacionUi.Error(
        mensaje = "Google Play Services no está disponible en este dispositivo",
        tipo = TipoError.SIN_PLAY_SERVICES
    )

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

        if (!ubicacionRepo.playServicesDisponible()) {
            _estado.value = errorPlayServices()
            return
        }

        _estado.value = UbicacionUi.Buscando

        viewModelScope.launch {
            ubicacionRepo.obtenerPosicion()
                .onSuccess { posicion ->
                    actualizarDistancias(posicion)
                    _estado.value = UbicacionUi.Lista(posicion)
                }
                .onFailure { error ->
                    _estado.value = errorDe(error)
                }
        }
    }
    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error(
            mensaje = "Sin permiso de ubicación no puedo mostrar dónde estás",
            tipo = TipoError.SIN_PERMISO
        )
    }
    fun seleccionar(id: Int?) {
        _seleccionado.value = id
    }

    fun actualizarConsulta(nuevaConsulta: String) {
        _consulta.value = nuevaConsulta
    }

    fun seleccionarCategoria(nuevaCategoria: String?) {
        _categoriaSeleccionada.value = nuevaCategoria
    }

    private fun errorDe(error: Throwable): UbicacionUi.Error {
        return when (error) {
            is SecurityException -> UbicacionUi.Error(
                mensaje = "Falta el permiso de ubicación",
                tipo = TipoError.SIN_PERMISO
            )

            is UbicacionNoDisponibleException -> UbicacionUi.Error(
                mensaje = "Activa el GPS e intenta de nuevo",
                tipo = TipoError.GPS_APAGADO
            )

            else -> UbicacionUi.Error(
                mensaje = "No se pudo obtener la ubicación. Intenta de nuevo",
                tipo = TipoError.OTRO
            )
        }
    }
}
