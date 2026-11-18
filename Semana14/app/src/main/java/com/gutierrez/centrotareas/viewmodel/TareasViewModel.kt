package com.gutierrez.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutierrez.centrotareas.model.data.TareasRepository
import com.gutierrez.centrotareas.model.CargaUi
import com.gutierrez.centrotareas.model.MedicionCarga
import com.gutierrez.centrotareas.model.PrimosUi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class TareasViewModel(private val repo: TareasRepository) : ViewModel() {
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    private val _progreso = MutableStateFlow(0f)
    val progreso: StateFlow<Float> = _progreso.asStateFlow()

    private var trabajo: Job? = null
    private val _corriendo = MutableStateFlow(false)
    val corriendo: StateFlow<Boolean> = _corriendo.asStateFlow()

    fun tareaLarga() {
        if (_corriendo.value) return
        _corriendo.value = true

        trabajo = viewModelScope.launch {
            try {
                _progreso.value = 0f
                _mensaje.value = "Trabajando..."
                Log.d("HILO", "Antes: ${Thread.currentThread().name}")

                repo.observarProgreso().collect { valor ->
                    _progreso.value = valor
                }

                Log.d("HILO", "Despues: ${Thread.currentThread().name}")
                _mensaje.value = "Terminó"
            } finally {
                _corriendo.value = false
            }
        }
    }

    fun cancelar() {
        if (!_corriendo.value) return
        trabajo?.cancel()
        _mensaje.value = "Cancelado"
    }

    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos: StateFlow<PrimosUi> = _primos.asStateFlow()
    private var trabajoPrimos: Job? = null

    fun contar(hasta: Int) {
        if (hasta < 2 || _primos.value is PrimosUi.Calculando) return
        _primos.value = PrimosUi.Calculando(0f, 0)

        trabajoPrimos = viewModelScope.launch {
            val inicio = System.currentTimeMillis()
            var ultimoProgreso = 0f
            try {
                repo.contarPrimos(hasta).collect { dto ->
                    ultimoProgreso = dto.progreso
                    _primos.value = PrimosUi.Calculando(dto.progreso, dto.cantidad)
                }
                val milisegundos = System.currentTimeMillis() - inicio
                val cantidadFinal = (_primos.value as? PrimosUi.Calculando)?.cantidad ?: 0
                _primos.value = PrimosUi.Listo(cantidadFinal, milisegundos)
            } catch (e: CancellationException) {
                _primos.value = PrimosUi.Cancelado(ultimoProgreso)
                throw e
            }
        }
    }

    fun cancelarPrimos() {
        trabajoPrimos?.cancel()
    }


    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga: StateFlow<CargaUi> = _carga.asStateFlow()
    private val _simularError = MutableStateFlow(false)
    val simularError: StateFlow<Boolean> = _simularError.asStateFlow()

    private val _limitarTiempo = MutableStateFlow(false)
    val limitarTiempo: StateFlow<Boolean> = _limitarTiempo.asStateFlow()

    private val _mediciones = MutableStateFlow<List<MedicionCarga>>(emptyList())
    val mediciones: StateFlow<List<MedicionCarga>> = _mediciones.asStateFlow()

    fun cambiarSimularError(valor: Boolean) {
        _simularError.value = valor
    }

    fun cambiarLimitarTiempo(valor: Boolean) {
        _limitarTiempo.value = valor
    }

    fun limpiarComparacion() {
        _mediciones.value = emptyList()
    }

    fun cargarUnaPorUna() {
        if (_carga.value is CargaUi.Cargando) return
        _carga.value = CargaUi.Cargando
        val provocarError = _simularError.value
        val limitarTiempo = _limitarTiempo.value

        viewModelScope.launch {
            try {
                val inicio = System.currentTimeMillis()
                val (perfil, pedidos) = if (limitarTiempo) {
                    withTimeout(2000L) {
                        val perfilRes = repo.cargarPerfil()
                        val pedidosRes = repo.cargarPedidos(provocarError)
                        Pair(perfilRes, pedidosRes)
                    }
                } else {
                    val perfilRes = repo.cargarPerfil()
                    val pedidosRes = repo.cargarPedidos(provocarError)
                    Pair(perfilRes, pedidosRes)
                }
                val milisegundos = System.currentTimeMillis() - inicio
                _carga.value = CargaUi.Exito("Una por una", perfil, pedidos.size, milisegundos)
                val nuevaMedicion = MedicionCarga(
                    id = _mediciones.value.size + 1,
                    modo = "Una por una",
                    milisegundos = milisegundos
                )
                _mediciones.value = _mediciones.value + nuevaMedicion
            } catch (e: TimeoutCancellationException) {
                if (currentCoroutineContext().isActive) {
                    _carga.value = CargaUi.Error("Tardó demasiado")
                } else {
                    throw e
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _carga.value = CargaUi.Error("No se pudo cargar: " + e.message)
            }
        }
    }

    fun cargarALaVez() {
        if (_carga.value is CargaUi.Cargando) return
        _carga.value = CargaUi.Cargando
        val provocarError = _simularError.value
        val limitarTiempo = _limitarTiempo.value

        viewModelScope.launch {
            try {
                val inicio = System.currentTimeMillis()
                val (perfil, pedidos) = if (limitarTiempo) {
                    withTimeout(2000L) {
                        coroutineScope {
                            val perfilPendiente = async { repo.cargarPerfil() }
                            val pedidosPendientes = async { repo.cargarPedidos(provocarError) }
                            Pair(perfilPendiente.await(), pedidosPendientes.await())
                        }
                    }
                } else {
                    coroutineScope {
                        val perfilPendiente = async { repo.cargarPerfil() }
                        val pedidosPendientes = async { repo.cargarPedidos(provocarError) }
                        Pair(perfilPendiente.await(), pedidosPendientes.await())
                    }
                }
                val milisegundos = System.currentTimeMillis() - inicio
                _carga.value = CargaUi.Exito("A la vez", perfil, pedidos.size, milisegundos)
                val nuevaMedicion = MedicionCarga(
                    id = _mediciones.value.size + 1,
                    modo = "A la vez",
                    milisegundos = milisegundos
                )
                _mediciones.value = _mediciones.value + nuevaMedicion
            } catch (e: TimeoutCancellationException) {
                if (currentCoroutineContext().isActive) {
                    _carga.value = CargaUi.Error("Tardó demasiado")
                } else {
                    throw e
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _carga.value = CargaUi.Error("No se pudo cargar: " + e.message)
            }
        }
    }
}
