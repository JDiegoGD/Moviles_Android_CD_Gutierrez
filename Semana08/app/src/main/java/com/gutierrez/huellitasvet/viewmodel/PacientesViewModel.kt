package com.gutierrez.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutierrez.huellitasvet.data.local.relation.MascotaConDueno
import com.gutierrez.huellitasvet.data.repository.VetRepository
import com.gutierrez.huellitasvet.model.Especie
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import java.text.Normalizer

/** Elimina tildes/acentos y convierte a minúsculas para búsquedas insensibles a tildes y mayúsculas. */
private fun String.normalizar(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace("\\p{M}".toRegex(), "")
        .lowercase()

/** Lista de pacientes filtrada por texto (nombre, dueño, raza) y por especie, con debounce de 250ms. */
class PacientesViewModel(repo: VetRepository) : ViewModel() {

    private val _texto = MutableStateFlow("")
    val texto: StateFlow<String> = _texto.asStateFlow()

    /** null = Todos. */
    private val _especie = MutableStateFlow<Especie?>(null)
    val especie: StateFlow<Especie?> = _especie.asStateFlow()

    @OptIn(FlowPreview::class)
    private val textoDebounced = _texto.debounce(250L)

    @OptIn(FlowPreview::class)
    val conteos: StateFlow<Map<Especie?, Int>> = combine(
        repo.observarTodasLasMascotas(),
        textoDebounced
    ) { lista, texto ->
        val tNorm = texto.trim().normalizar()
        val filtradaTexto = if (tNorm.isBlank()) lista else lista.filter { p ->
            p.mascota.nombre.normalizar().contains(tNorm) ||
                p.dueno.nombre.normalizar().contains(tNorm) ||
                p.mascota.raza.normalizar().contains(tNorm)
        }
        mapOf(
            null to filtradaTexto.size,
            Especie.PERRO to filtradaTexto.count { it.mascota.especie == Especie.PERRO.name },
            Especie.GATO to filtradaTexto.count { it.mascota.especie == Especie.GATO.name },
            Especie.OTRO to filtradaTexto.count { it.mascota.especie == Especie.OTRO.name }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    @OptIn(FlowPreview::class)
    val pacientes: StateFlow<List<MascotaConDueno>> = combine(
        repo.observarTodasLasMascotas(),
        textoDebounced,
        _especie
    ) { lista, texto, especie ->
        val tNorm = texto.trim().normalizar()
        lista.filter { p ->
            val coincideTexto = tNorm.isBlank() ||
                p.mascota.nombre.normalizar().contains(tNorm) ||
                p.dueno.nombre.normalizar().contains(tNorm) ||
                p.mascota.raza.normalizar().contains(tNorm)
            val coincideEspecie = especie == null || p.mascota.especie == especie.name
            coincideTexto && coincideEspecie
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun cambiarTexto(t: String) { _texto.value = t }

    fun cambiarEspecie(e: Especie?) { _especie.value = e }

    fun limpiar() {
        _texto.value = ""
        _especie.value = null
    }
}
