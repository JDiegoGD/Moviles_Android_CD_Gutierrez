package com.gutierrez.huellitasvet.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gutierrez.huellitasvet.VetApp
import com.gutierrez.huellitasvet.model.EstadoCita
import com.gutierrez.huellitasvet.model.Rol
import com.gutierrez.huellitasvet.ui.dueno.LoginScreen
import com.gutierrez.huellitasvet.ui.dueno.SplashScreen
import com.gutierrez.huellitasvet.ui.theme.VetTheme
import com.gutierrez.huellitasvet.viewmodel.SesionViewModel
import com.gutierrez.huellitasvet.viewmodel.VetViewModelFactory
import androidx.compose.material3.Button
import com.gutierrez.huellitasvet.ui.dueno.InicioScreen
import com.gutierrez.huellitasvet.ui.dueno.MascotasScreen
import com.gutierrez.huellitasvet.viewmodel.InicioViewModel
import com.gutierrez.huellitasvet.ui.dueno.FichaMascotaScreen
import com.gutierrez.huellitasvet.ui.dueno.FormularioMascotaScreen
import com.gutierrez.huellitasvet.viewmodel.MascotaViewModel
import com.gutierrez.huellitasvet.ui.dueno.PedirCitaScreen
import com.gutierrez.huellitasvet.viewmodel.PedirCitaViewModel

/** Una pestaña de la barra de navegacion inferior. */
private data class Tab(val ruta: String, val titulo: String, val icono: ImageVector)

@Composable
fun NavVet() {
    val app = LocalContext.current.applicationContext as VetApp
    val repo = app.repository
    val nav = rememberNavController()

    // Sesion compartida por toda la app (usuario, modo oscuro y preferencias)
    val sesionVm: SesionViewModel = viewModel(
        factory = VetViewModelFactory { SesionViewModel(repo, app.prefs) }
    )
    val usuario by sesionVm.usuarioActual.collectAsState()
    val oscuro by sesionVm.modoOscuro.collectAsState()
    val rolTema = if (usuario?.rol == Rol.ADMIN.name) Rol.ADMIN else Rol.CLIENTE
    val ruta = nav.currentBackStackEntryAsState().value?.destination?.route

    // Barra inferior: 4 items para el dueño y 5 para el veterinario
    val tabsDueno: List<Tab> = listOf(
        Tab(Rutas.INICIO, "Inicio", Icons.Filled.Home),
        Tab(Rutas.MASCOTAS, "Mascotas", Icons.Filled.Pets),
    )
    val tabsVet: List<Tab> = listOf(
    )
    val rutasDueno = tabsDueno.map { it.ruta }
    val rutasVet = tabsVet.map { it.ruta }
    val tabs: List<Tab> = when (ruta) {
        in rutasDueno -> tabsDueno
        in rutasVet -> tabsVet
        else -> emptyList()
    }

    /** Va a la pantalla principal del perfil y limpia la pila de navegacion. */
    fun irAHome(rol: Rol) {
        val destino = if (rol == Rol.CLIENTE) Rutas.INICIO else Rutas.PANEL
        nav.navigate(destino) {
            popUpTo(nav.graph.id) { inclusive = true }
            launchSingleTop = true
        }
    }

    /** Cierra la sesion guardada y vuelve al login. */
    fun salir() {
        sesionVm.cerrarSesion {
            nav.navigate(Rutas.LOGIN) { popUpTo(nav.graph.id) { inclusive = true } }
        }
    }

    VetTheme(rol = rolTema, oscuro = oscuro) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                if (tabs.isNotEmpty()) {
                    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                        tabs.forEach { tab ->
                            NavigationBarItem(
                                selected = ruta == tab.ruta,
                                onClick = {
                                    nav.navigate(tab.ruta) {
                                        popUpTo(tabs.first().ruta) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Icon(tab.icono, contentDescription = tab.titulo) },
                                label = { Text(tab.titulo) }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = nav,
                startDestination = Rutas.SPLASH,
                modifier = Modifier.padding(innerPadding)
            ) {
                // ---------- Comunes ----------
                composable(Rutas.SPLASH) {
                    SplashScreen(onComenzar = {
                        // Si ya hay perfil guardado, salta el login y va a su pantalla principal
                        val u = usuario
                        if (u != null) {
                            irAHome(Rol.valueOf(u.rol))
                        } else {
                            nav.navigate(Rutas.LOGIN) { popUpTo(Rutas.SPLASH) { inclusive = true } }
                        }
                    })
                }
                composable(Rutas.LOGIN) {
                    LoginScreen(sesionVm, onLogin = { rol -> irAHome(rol) })
                }

                composable(Rutas.PANEL) { Provisional("Panel del veterinario", onSalir = { salir() }) }

                // ---------- Flujo del dueño ----------
                composable(Rutas.INICIO) {
                    val u = usuario ?: return@composable
                    val vm: InicioViewModel = viewModel(
                        factory = VetViewModelFactory { InicioViewModel(repo, u.id) }
                    )
                    InicioScreen(
                        nombre = u.nombre,
                        vm = vm,
                        onVerMascota = { id ->
                            nav.navigate(Rutas.fichaMascota(id))
                        },
                        onAgregar = {
                            nav.navigate(Rutas.formMascota())
                        },
                        onVerTodas = { nav.navigate(Rutas.MASCOTAS) { launchSingleTop = true } },
                        onPedirCita = {
                            nav.navigate(Rutas.pedirCita())
                        },
                        onCerrarSesion = { salir() }
                    )
                }
                composable(Rutas.MASCOTAS) {
                    val u = usuario ?: return@composable
                    val vm: InicioViewModel = viewModel(
                        factory = VetViewModelFactory { InicioViewModel(repo, u.id) }
                    )
                    MascotasScreen(
                        vm = vm,
                        onVerMascota = { id ->
                            nav.navigate(Rutas.fichaMascota(id))
                        },
                        onAgregar = {
                            nav.navigate(Rutas.formMascota())
                        }
                    )
                }
                composable(
                    Rutas.FICHA_MASCOTA,
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { entry ->
                    val id = entry.arguments?.getInt("id") ?: return@composable
                    val vm: MascotaViewModel = viewModel(
                        factory = VetViewModelFactory { MascotaViewModel(repo) }
                    )
                    FichaMascotaScreen(
                        id = id,
                        vm = vm,
                        onBack = { nav.popBackStack() },
                        onPedirCita = { mascotaId ->
                            nav.navigate(Rutas.pedirCita(mascotaId))
                        },
                        onEditar = { mascotaId -> nav.navigate(Rutas.formMascota(mascotaId)) },
                        soloLectura = usuario?.rol == Rol.ADMIN.name
                    )
                }
                composable(
                    Rutas.FORM_MASCOTA,
                    arguments = listOf(navArgument("id") {
                        type = NavType.IntType
                        defaultValue = -1
                    })
                ) { entry ->
                    val idArg = entry.arguments?.getInt("id") ?: -1
                    val u = usuario ?: return@composable
                    val vm: MascotaViewModel = viewModel(
                        factory = VetViewModelFactory { MascotaViewModel(repo) }
                    )
                    FormularioMascotaScreen(
                        id = if (idArg > 0) idArg else null,
                        duenoId = u.id,
                        vm = vm,
                        onBack = { nav.popBackStack() }
                    )
                }
                composable(
                    Rutas.PEDIR_CITA,
                    arguments = listOf(navArgument("mascotaId") {
                        type = NavType.IntType
                        defaultValue = -1
                    })
                ) { entry ->
                    val mascotaId = entry.arguments?.getInt("mascotaId") ?: -1
                    val u = usuario ?: return@composable
                    val vm: PedirCitaViewModel = viewModel(
                        factory = VetViewModelFactory {
                            PedirCitaViewModel(repo, app.prefs, u.id, if (mascotaId > 0) mascotaId else null)
                        }
                    )
                    PedirCitaScreen(
                        vm = vm,
                        onBack = { nav.popBackStack() },
                        onCreada = {
                            nav.popBackStack()
                        }
                    )
                }

                // ---------- Flujo del veterinario ----------
            }
        }
    }
}

/** Pantalla temporal para poder probar el login antes de crear las pantallas reales. */
@Composable
private fun Provisional(titulo: String, onSalir: () -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(titulo, style = MaterialTheme.typography.headlineSmall)
        Text("(pantalla provisional)")
        Button(onClick = onSalir) { Text("Cerrar sesión") }
    }
}
