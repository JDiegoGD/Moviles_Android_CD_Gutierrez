package com.gutierrez.citas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gutierrez.citas.ui.screens.agendamiento.CitaExitosaScreen
import com.gutierrez.citas.ui.screens.agendamiento.ConfirmarCitaScreen
import com.gutierrez.citas.ui.screens.agendamiento.EspecialidadesScreen
import com.gutierrez.citas.ui.screens.agendamiento.FechaHoraScreen
import com.gutierrez.citas.ui.screens.agendamiento.MedicosScreen
import com.gutierrez.citas.ui.screens.auth.LoginScreen
import com.gutierrez.citas.ui.screens.auth.RegistroScreen
import com.gutierrez.citas.ui.screens.auth.SplashScreen
import com.gutierrez.citas.ui.screens.auth.TerminosScreen
import com.gutierrez.citas.ui.screens.citas.DetalleCitaScreen
import com.gutierrez.citas.ui.screens.citas.MisCitasScreen
import com.gutierrez.citas.ui.screens.home.HomeScreen
import com.gutierrez.citas.ui.screens.notificaciones.NotificacionesScreen
import com.gutierrez.citas.ui.screens.perfil.PerfilScreen
import com.gutierrez.citas.ui.screens.resultados.ResultadosScreen

/**
 * Grafo de navegación de toda la app. Cada pantalla recibe lambdas (onAtras,
 * onContinuar, ...) en vez del NavController, así no conocen de dónde vienen
 * ni a dónde van.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rutas.SPLASH) {
        // ---------- Autenticación ----------
        composable(Rutas.SPLASH) {
            SplashScreen(
                onComenzar = { navController.navigate(Rutas.REGISTRO) },
                onYaTengoCuenta = { navController.navigate(Rutas.LOGIN) }
            )
        }
        composable(Rutas.REGISTRO) {
            RegistroScreen(
                onRegistroExitoso = { navController.reemplazarCon(Rutas.LOGIN, Rutas.REGISTRO) },
                onIrLogin = { navController.reemplazarCon(Rutas.LOGIN, Rutas.REGISTRO) },
                onTerminos = { navController.navigate(Rutas.TERMINOS) }
            )
        }
        composable(Rutas.TERMINOS) {
            TerminosScreen(onAtras = { navController.popBackStack() })
        }
        composable(Rutas.LOGIN) {
            LoginScreen(
                onLoginExitoso = {
                    // Al entrar se borra Splash/Registro/Login: Atrás en Inicio cierra la app
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.SPLASH) { inclusive = true }
                    }
                },
                onIrRegistro = { navController.reemplazarCon(Rutas.REGISTRO, Rutas.LOGIN) },
                onAtras = { navController.popBackStack() }
            )
        }

        // ---------- Inicio y barra inferior ----------
        composable(Rutas.HOME) {
            HomeScreen(
                onNotificaciones = { navController.navigate(Rutas.NOTIFICACIONES) },
                onAgendar = { navController.navigate(Rutas.ESPECIALIDADES) },
                onMisCitas = { navController.irAPestana(Rutas.MIS_CITAS) },
                onMisDatos = { navController.irAPestana(Rutas.PERFIL) },
                onResultados = { navController.irAPestana(Rutas.RESULTADOS) },
                onEspecialidad = { id -> navController.navigate(Rutas.medicos(id)) },
                onVerEspecialidades = { navController.navigate(Rutas.ESPECIALIDADES) },
                onNavegar = { ruta -> navController.irAPestana(ruta) }
            )
        }
        composable(Rutas.NOTIFICACIONES) {
            NotificacionesScreen(onAtras = { navController.popBackStack() })
        }
        composable(Rutas.RESULTADOS) {
            ResultadosScreen(onNavegar = { ruta -> navController.irAPestana(ruta) })
        }
        composable(Rutas.PERFIL) {
            PerfilScreen(
                onNavegar = { ruta -> navController.irAPestana(ruta) },
                onCerrarSesion = {
                    // Se borra todo hasta Inicio (incluido): Atrás ya no regresa a la sesión cerrada
                    navController.navigate(Rutas.SPLASH) {
                        popUpTo(Rutas.HOME) { inclusive = true }
                    }
                }
            )
        }

        // ---------- Flujo de agendamiento ----------
        composable(Rutas.ESPECIALIDADES) {
            EspecialidadesScreen(
                onAtras = { navController.popBackStack() },
                onEspecialidad = { id -> navController.navigate(Rutas.medicos(id)) }
            )
        }
        composable(
            route = Rutas.MEDICOS,
            arguments = listOf(navArgument("especialidadId") { type = NavType.IntType })
        ) { entrada ->
            MedicosScreen(
                especialidadId = entrada.arguments?.getInt("especialidadId") ?: 0,
                onAtras = { navController.popBackStack() },
                onMedico = { medicoId -> navController.navigate(Rutas.fechaHora(medicoId)) }
            )
        }
        composable(
            route = Rutas.FECHA_HORA,
            arguments = listOf(navArgument("medicoId") { type = NavType.IntType })
        ) { entrada ->
            val medicoId = entrada.arguments?.getInt("medicoId") ?: 0
            FechaHoraScreen(
                medicoId = medicoId,
                onAtras = { navController.popBackStack() },
                onContinuar = { fecha, hora ->
                    navController.navigate(Rutas.confirmarCita(medicoId, fecha, hora))
                }
            )
        }
        composable(
            route = Rutas.CONFIRMAR_CITA,
            arguments = listOf(
                navArgument("medicoId") { type = NavType.IntType },
                navArgument("fecha") { type = NavType.StringType },
                navArgument("hora") { type = NavType.StringType }
            )
        ) { entrada ->
            ConfirmarCitaScreen(
                medicoId = entrada.arguments?.getInt("medicoId") ?: 0,
                fecha = entrada.arguments?.getString("fecha").orEmpty(),
                hora = entrada.arguments?.getString("hora").orEmpty(),
                onAtras = { navController.popBackStack() },
                onConfirmada = {
                    // popUpTo(HOME) saca del historial Especialidades, Médicos, Fecha y hora y Confirmar
                    navController.navigate(Rutas.CITA_EXITOSA) {
                        popUpTo(Rutas.HOME)
                    }
                }
            )
        }
        composable(Rutas.CITA_EXITOSA) {
            CitaExitosaScreen(
                onVerMisCitas = { navController.irAPestana(Rutas.MIS_CITAS) },
                onIrInicio = { navController.popBackStack(Rutas.HOME, inclusive = false) }
            )
        }

        // ---------- Citas ----------
        composable(Rutas.MIS_CITAS) {
            MisCitasScreen(
                onNavegar = { ruta -> navController.irAPestana(ruta) },
                onAgendar = { navController.navigate(Rutas.ESPECIALIDADES) },
                onCita = { citaId -> navController.navigate(Rutas.detalleCita(citaId)) }
            )
        }
        composable(
            route = Rutas.DETALLE_CITA,
            arguments = listOf(navArgument("citaId") { type = NavType.IntType })
        ) { entrada ->
            DetalleCitaScreen(
                citaId = entrada.arguments?.getInt("citaId") ?: 0,
                onAtras = { navController.popBackStack() },
                onCancelada = { navController.popBackStack() }
            )
        }
    }
}

/**
 * Cambio de pestaña de la barra inferior: deja Inicio como base de la pila y
 * evita apilar la misma pestaña dos veces.
 */
private fun NavHostController.irAPestana(ruta: String) {
    navigate(ruta) {
        popUpTo(Rutas.HOME)
        launchSingleTop = true
    }
}

/** Va a [destino] y elimina [salida] de la pila, para que Atrás no vuelva a ella. */
private fun NavHostController.reemplazarCon(destino: String, salida: String) {
    navigate(destino) {
        popUpTo(salida) { inclusive = true }
    }
}
