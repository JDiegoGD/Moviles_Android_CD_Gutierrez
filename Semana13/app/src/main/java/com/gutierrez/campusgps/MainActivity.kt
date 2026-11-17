package com.gutierrez.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gutierrez.campusgps.data.LugaresRepositoryLocal
import com.gutierrez.campusgps.data.UbicacionRepositoryGoogle
import com.gutierrez.campusgps.ui.PantallaMapa
import com.gutierrez.campusgps.viewmodel.CampusViewModelFactory
import com.gutierrez.campusgps.viewmodel.MapaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val factory = CampusViewModelFactory(
            ubicacionRepo = UbicacionRepositoryGoogle(applicationContext),
            lugaresRepo = LugaresRepositoryLocal()
        )

        setContent {
            val colores = remember {
                lightColorScheme(primary = Color(0xFF1565C0), onPrimary = Color.White)
            }

            MaterialTheme(colorScheme = colores) {
                val vm: MapaViewModel = viewModel(factory = factory)
                PantallaMapa(vm = vm)
            }
        }
    }
}