package com.gutierrez.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Factory generica: se le pasa una lambda que construye el ViewModel con sus
 * dependencias (normalmente el repository). Uso:
 *
 *   viewModel(factory = VetViewModelFactory { MiViewModel(repo) })
 */
class VetViewModelFactory<VM : ViewModel>(private val crear: () -> VM) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = crear() as T
}
