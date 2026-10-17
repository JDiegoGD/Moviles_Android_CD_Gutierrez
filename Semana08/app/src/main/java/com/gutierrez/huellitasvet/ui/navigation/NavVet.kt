package com.gutierrez.huellitasvet.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gutierrez.huellitasvet.VetApp
import com.gutierrez.huellitasvet.model.Rol
import com.gutierrez.huellitasvet.ui.dueno.SplashScreen
import com.gutierrez.huellitasvet.ui.theme.VetTheme
import com.gutierrez.huellitasvet.ui.veterinario.ServiciosScreen
import com.gutierrez.huellitasvet.viewmodel.ServiciosViewModel
import com.gutierrez.huellitasvet.viewmodel.VetViewModelFactory

/** Proyecto base: solo Splash -> Servicios. Los demas destinos se agregan paso a paso. */
@Composable
fun NavVet() {
    val app = LocalContext.current.applicationContext as VetApp
    val nav = rememberNavController()
    val ruta = nav.currentBackStackEntryAsState().value?.destination?.route

    VetTheme(rol = if (ruta == Rutas.SERVICIOS) Rol.ADMIN else Rol.CLIENTE, oscuro = false) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            NavHost(navController = nav, startDestination = Rutas.SPLASH) {
                composable(Rutas.SPLASH) {
                    SplashScreen(onComenzar = { nav.navigate(Rutas.SERVICIOS) })
                }
                composable(Rutas.SERVICIOS) {
                    val vm: ServiciosViewModel = viewModel(
                        factory = VetViewModelFactory { ServiciosViewModel(app.repository) }
                    )
                    ServiciosScreen(vm, onEditar = { /* Paso 13 */ }, onNuevo = { /* Paso 13 */ },
                        onBack = { nav.popBackStack() })
                }
            }
        }
    }
}
