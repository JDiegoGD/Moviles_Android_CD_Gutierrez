package com.gutierrez.centrotareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gutierrez.centrotareas.model.data.TareasRepositoryLocal
import com.gutierrez.centrotareas.ui.PantallaTareas
import com.gutierrez.centrotareas.ui.theme.CentroTareasTheme
import com.gutierrez.centrotareas.viewmodel.TareasViewModel
import com.gutierrez.centrotareas.viewmodel.TareasViewModelFactory

class MainActivity : ComponentActivity() {
    private val factory by lazy { TareasViewModelFactory(TareasRepositoryLocal()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CentroTareasTheme(dynamicColor = false) {
                val tareasViewModel: TareasViewModel = viewModel(factory = factory)
                PantallaTareas(tareasViewModel)
            }
        }
    }
}
