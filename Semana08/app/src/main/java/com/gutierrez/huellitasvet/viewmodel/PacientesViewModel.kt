package com.gutierrez.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutierrez.huellitasvet.data.local.relation.MascotaConDueno
import com.gutierrez.huellitasvet.data.repository.VetRepository
import com.gutierrez.huellitasvet.model.Especie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

/** Lista de pacientes filtrada por texto y por especie (combine de 3 flujos). */
class PacientesViewModel(repo: VetRepository) : ViewModel() {

    private val _texto = MutableStateFlow("")
    val texto: StateFlow<String> = _texto.asStateFlow()

    /** null = Todos. */
    private val _especie = MutableStateFlow<Especie?>(null)
    val especie: StateFlow<Especie?> = _especie.asStateFlow()

    val pacientes: StateFlow<List<MascotaConDueno>> = combine(
        repo.observarTodasLasMascotas(), _texto, _especie
    ) { lista, texto, especie ->
        lista.filter { p ->
            val coincideTexto = texto.isBlank() ||
                p.mascota.nombre.contains(texto.trim(), ignoreCase = true) ||
                p.dueno.nombre.contains(texto.trim(), ignoreCase = true)
            val coincideEspecie = especie == null || p.mascota.especie == especie.name
            coincideTexto && coincideEspecie
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun cambiarTexto(t: String) { _texto.value = t }

    fun cambiarEspecie(e: Especie?) { _especie.value = e }
}
