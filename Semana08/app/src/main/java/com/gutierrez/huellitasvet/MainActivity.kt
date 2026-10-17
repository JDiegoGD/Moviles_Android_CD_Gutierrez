package com.gutierrez.huellitasvet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gutierrez.huellitasvet.ui.navigation.NavVet

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavVet()
        }
    }
}

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