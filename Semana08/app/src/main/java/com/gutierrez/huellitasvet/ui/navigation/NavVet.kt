package com.gutierrez.huellitasvet.ui.navigation

import com.gutierrez.huellitasvet.ui.dueno.RegistroScreen
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
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.runtime.remember
import com.gutierrez.huellitasvet.data.local.relation.CitaConDetalle
import kotlinx.coroutines.flow.flowOf
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import com.gutierrez.huellitasvet.ui.dueno.InicioScreen
import com.gutierrez.huellitasvet.ui.dueno.MascotasScreen
import com.gutierrez.huellitasvet.viewmodel.InicioViewModel
import com.gutierrez.huellitasvet.ui.dueno.FichaMascotaScreen
import com.gutierrez.huellitasvet.ui.dueno.FormularioMascotaScreen
import com.gutierrez.huellitasvet.viewmodel.MascotaViewModel
import com.gutierrez.huellitasvet.ui.dueno.PedirCitaScreen
import com.gutierrez.huellitasvet.viewmodel.PedirCitaViewModel
import com.gutierrez.huellitasvet.ui.dueno.MisCitasScreen
import com.gutierrez.huellitasvet.viewmodel.MisCitasViewModel
import com.gutierrez.huellitasvet.ui.dueno.ReprogramarCitaScreen
import com.gutierrez.huellitasvet.viewmodel.ReprogramarCitaViewModel
import com.gutierrez.huellitasvet.ui.veterinario.AgendaScreen
import com.gutierrez.huellitasvet.ui.veterinario.PanelScreen
import com.gutierrez.huellitasvet.ui.veterinario.ServiciosScreen
import com.gutierrez.huellitasvet.viewmodel.AgendaViewModel
import com.gutierrez.huellitasvet.viewmodel.PanelViewModel
import com.gutierrez.huellitasvet.viewmodel.ServiciosViewModel
import com.gutierrez.huellitasvet.ui.veterinario.AtenderCitaScreen
import com.gutierrez.huellitasvet.ui.veterinario.PacientesScreen
import com.gutierrez.huellitasvet.viewmodel.AtenderCitaViewModel
import com.gutierrez.huellitasvet.viewmodel.PacientesViewModel
import com.gutierrez.huellitasvet.ui.dueno.PerfilScreen
import com.gutierrez.huellitasvet.ui.veterinario.AjustesScreen
import com.gutierrez.huellitasvet.ui.veterinario.FormularioServicioScreen

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

    // Numeros para los badges de la barra inferior (citas proximas del dueño y pendientes del veterinario)
    val idDueno = if (usuario?.rol == Rol.CLIENTE.name) usuario?.id else null
    val citasDueno by remember(idDueno) {
        if (idDueno != null) repo.observarCitasDeDueno(idDueno) else flowOf(emptyList<CitaConDetalle>())
    }.collectAsState(initial = emptyList())
    val citasProximas = citasDueno.count {
        it.cita.estado == EstadoCita.PENDIENTE.name || it.cita.estado == EstadoCita.CONFIRMADA.name
    }
    val pendientesVet by remember { repo.observarCantidadPorEstado(EstadoCita.PENDIENTE) }
        .collectAsState(initial = 0)

    // Barra inferior: 4 items para el dueño y 5 para el veterinario
    val tabsDueno: List<Tab> = listOf(
        Tab(Rutas.INICIO, "Inicio", Icons.Filled.Home),
        Tab(Rutas.MASCOTAS, "Mascotas", Icons.Filled.Pets),
        Tab(Rutas.MIS_CITAS, "Citas", Icons.Filled.CalendarMonth),
        Tab(Rutas.PERFIL, "Perfil", Icons.Filled.Person),
    )
    val tabsVet: List<Tab> = listOf(
        Tab(Rutas.PANEL, "Panel", Icons.Filled.BarChart),
        Tab(Rutas.AGENDA, "Agenda", Icons.Filled.CalendarMonth),
        Tab(Rutas.PACIENTES, "Pacientes", Icons.Filled.Pets),
        Tab(Rutas.SERVICIOS, "Servicios", Icons.Filled.LocalOffer),
        Tab(Rutas.AJUSTES, "Ajustes", Icons.Filled.Settings),
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
                                icon = {
                                    val cantidad = when (tab.ruta) {
                                        Rutas.MIS_CITAS -> citasProximas
                                        Rutas.AGENDA -> pendientesVet
                                        else -> 0
                                    }
                                    BadgedBox(badge = { if (cantidad > 0) Badge { Text("$cantidad") } }) {
                                        Icon(tab.icono, contentDescription = tab.titulo)
                                    }
                                },
                                label = { Text(tab.titulo) },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = nav,
                startDestination = Rutas.SPLASH,
                modifier = Modifier.padding(innerPadding),
                // Transicion suave entre pantallas
                enterTransition = { fadeIn(tween(250)) },
                exitTransition = { fadeOut(tween(150)) },
                popEnterTransition = { fadeIn(tween(250)) },
                popExitTransition = { fadeOut(tween(150)) }
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
                    LoginScreen(
                        sesionVm,
                        onLogin = { rol -> irAHome(rol) },
                        onRegistro = { nav.navigate(Rutas.REGISTRO) }
                    )
                }
                composable(Rutas.REGISTRO) {
                    RegistroScreen(
                        vm = sesionVm,
                        onRegistrado = { rol -> irAHome(rol) },
                        onVolver = { nav.popBackStack() }
                    )
                }


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
                            nav.navigate(Rutas.MIS_CITAS) { popUpTo(Rutas.INICIO) }
                        }
                    )
                }
                composable(
                    Rutas.REPROGRAMAR_CITA,
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { entry ->
                    val citaId = entry.arguments?.getInt("id") ?: return@composable
                    val vm: ReprogramarCitaViewModel = viewModel(
                        factory = VetViewModelFactory { ReprogramarCitaViewModel(repo, citaId) }
                    )
                    ReprogramarCitaScreen(
                        vm = vm,
                        onBack = { nav.popBackStack() },
                        onListo = {
                            nav.navigate(Rutas.MIS_CITAS) { popUpTo(Rutas.MIS_CITAS) { inclusive = true } }
                        }
                    )
                }
                composable(Rutas.MIS_CITAS) {
                    val u = usuario ?: return@composable
                    val vm: MisCitasViewModel = viewModel(
                        factory = VetViewModelFactory { MisCitasViewModel(repo, u.id) }
                    )
                    MisCitasScreen(
                        vm = vm,
                        onPedirCita = { nav.navigate(Rutas.pedirCita()) },
                        onReprogramar = { citaId -> nav.navigate(Rutas.reprogramarCita(citaId)) }
                    )
                }
                composable(Rutas.PERFIL) {
                    val u = usuario ?: return@composable
                    val inicioVm: InicioViewModel = viewModel(
                        factory = VetViewModelFactory { InicioViewModel(repo, u.id) }
                    )
                    val citasVm: MisCitasViewModel = viewModel(
                        factory = VetViewModelFactory { MisCitasViewModel(repo, u.id) }
                    )
                    val mascotas by inicioVm.mascotas.collectAsState()
                    val proximas by citasVm.proximas.collectAsState()
                    val historial by citasVm.historial.collectAsState()
                    val notificaciones by sesionVm.notificaciones.collectAsState()
                    PerfilScreen(
                        usuario = u,
                        mascotas = mascotas.size,
                        proximas = proximas.size,
                        atenciones = historial.count { it.cita.estado == EstadoCita.ATENDIDA.name },
                        oscuro = oscuro,
                        notificaciones = notificaciones,
                        onOscuro = { sesionVm.cambiarModoOscuro(it) },
                        onNotificaciones = { sesionVm.cambiarNotificaciones(it) },
                        onCambiarPerfil = { sesionVm.cambiarPerfil { rol -> irAHome(rol) } },
                        onCerrarSesion = { salir() }
                    )
                }

                // ---------- Flujo del veterinario ----------
                composable(Rutas.PANEL) {
                    val vm: PanelViewModel = viewModel(
                        factory = VetViewModelFactory { PanelViewModel(repo) }
                    )
                    PanelScreen(
                        vm = vm,
                        onVerPendientes = { nav.navigate(Rutas.AGENDA) { launchSingleTop = true } },
                    )
                }
                composable(Rutas.AGENDA) {
                    val vm: AgendaViewModel = viewModel(
                        factory = VetViewModelFactory { AgendaViewModel(repo) }
                    )
                    AgendaScreen(
                        vm = vm,
                        onAtender = { citaId ->
                            nav.navigate(Rutas.atenderCita(citaId))
                        }
                    )
                }
                composable(Rutas.SERVICIOS) {
                    val vm: ServiciosViewModel = viewModel(
                        factory = VetViewModelFactory { ServiciosViewModel(repo) }
                    )
                    ServiciosScreen(
                        vm = vm,
                        onEditar = { id ->
                            nav.navigate(Rutas.formServicio(id))
                        },
                        onNuevo = {
                            nav.navigate(Rutas.formServicio())
                        }
                    )
                }
                composable(
                    Rutas.ATENDER_CITA,
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { entry ->
                    val citaId = entry.arguments?.getInt("id") ?: return@composable
                    val vm: AtenderCitaViewModel = viewModel(
                        factory = VetViewModelFactory { AtenderCitaViewModel(repo, citaId) }
                    )
                    AtenderCitaScreen(vm, onBack = { nav.popBackStack() })
                }
                composable(Rutas.PACIENTES) {
                    val vm: PacientesViewModel = viewModel(
                        factory = VetViewModelFactory { PacientesViewModel(repo) }
                    )
                    PacientesScreen(vm, onVerMascota = { id -> nav.navigate(Rutas.fichaMascota(id)) })
                }
                composable(
                    Rutas.FORM_SERVICIO,
                    arguments = listOf(navArgument("id") {
                        type = NavType.IntType
                        defaultValue = -1
                    })
                ) { entry ->
                    val idArg = entry.arguments?.getInt("id") ?: -1
                    val vm: ServiciosViewModel = viewModel(
                        factory = VetViewModelFactory { ServiciosViewModel(repo) }
                    )
                    FormularioServicioScreen(
                        id = if (idArg > 0) idArg else null,
                        vm = vm,
                        onBack = { nav.popBackStack() }
                    )
                }
                composable(Rutas.AJUSTES) {
                    val u = usuario ?: return@composable
                    val notificaciones by sesionVm.notificaciones.collectAsState()
                    val aceptarCitas by sesionVm.aceptarCitas.collectAsState()
                    AjustesScreen(
                        usuario = u,
                        oscuro = oscuro,
                        notificaciones = notificaciones,
                        aceptarCitas = aceptarCitas,
                        onOscuro = { sesionVm.cambiarModoOscuro(it) },
                        onNotificaciones = { sesionVm.cambiarNotificaciones(it) },
                        onAceptarCitas = { sesionVm.cambiarAceptarCitas(it) },
                        onCerrarSesion = { salir() }
                    )
                }
            }
        }
    }
}
