package com.gutierrez.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutierrez.huellitasvet.data.local.entity.Usuario
import com.gutierrez.huellitasvet.data.repository.VetRepository
import com.gutierrez.huellitasvet.model.Rol
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminViewModel(private val repo: VetRepository) : ViewModel() {

    private fun <T> comoStateFlow(f: Flow<T>, inicial: T): StateFlow<T> =
        f.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), inicial)

    val usuarios: StateFlow<List<Usuario>> = comoStateFlow(repo.observarUsuarios(), emptyList())
    val totalDuenos: StateFlow<Int> = comoStateFlow(repo.observarCantidadUsuariosPorRol(Rol.CLIENTE.name), 0)
    val totalVeterinarios: StateFlow<Int> = comoStateFlow(repo.observarCantidadUsuariosPorRol(Rol.ADMIN.name), 0)
    val totalAdministradores: StateFlow<Int> = comoStateFlow(repo.observarCantidadUsuariosPorRol(Rol.ADMINISTRADOR.name), 0)

    /** Solo se pueden quitar dueños y veterinarios; los administradores no. */
    fun puedeQuitar(u: Usuario): Boolean = u.rol != Rol.ADMINISTRADOR.name

    fun quitar(u: Usuario) {
        if (!puedeQuitar(u)) return
        viewModelScope.launch { repo.eliminarUsuario(u.id) }
    }
}
