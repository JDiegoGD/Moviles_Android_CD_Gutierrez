package com.gutierrez.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutierrez.centrotareas.model.data.TareasRepository
import com.gutierrez.centrotareas.model.CargaUi
import com.gutierrez.centrotareas.model.PrimosUi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
        _primos.value = PrimosUi.Calculando

        trabajoPrimos = viewModelScope.launch {
            try {
                val inicio = System.currentTimeMillis()
                val cantidad = repo.contarPrimos(hasta)
                val milisegundos = System.currentTimeMillis() - inicio
                _primos.value = PrimosUi.Listo(cantidad, milisegundos)
            } catch (e: CancellationException) {
                _primos.value = PrimosUi.Cancelado
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

    fun cambiarSimularError(valor: Boolean) {
        _simularError.value = valor
    }

    fun cargarUnaPorUna() {
        if (_carga.value is CargaUi.Cargando) return
        _carga.value = CargaUi.Cargando
        val provocarError = _simularError.value

        viewModelScope.launch {
            try {
                val inicio = System.currentTimeMillis()
                val perfil = repo.cargarPerfil()
                val pedidos = repo.cargarPedidos(provocarError)
                val milisegundos = System.currentTimeMillis() - inicio
                _carga.value = CargaUi.Exito("Una por una", perfil, pedidos.size, milisegundos)
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

        viewModelScope.launch {
            try {
                val inicio = System.currentTimeMillis()
                val (perfil, pedidos) = coroutineScope {
                    val perfilPendiente = async { repo.cargarPerfil() }
                    val pedidosPendientes = async { repo.cargarPedidos(provocarError) }
                    Pair(perfilPendiente.await(), pedidosPendientes.await())
                }
                val milisegundos = System.currentTimeMillis() - inicio
                _carga.value = CargaUi.Exito("A la vez", perfil, pedidos.size, milisegundos)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _carga.value = CargaUi.Error("No se pudo cargar: " + e.message)
            }
        }
    }
}
