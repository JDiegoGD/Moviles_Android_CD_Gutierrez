package com.gutierrez.biblioteca.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gutierrez.biblioteca.AppContainer
import com.gutierrez.biblioteca.ui.admin.AdminScreen
import com.gutierrez.biblioteca.ui.admin.AdminViewModel
import com.gutierrez.biblioteca.ui.carrito.CarritoScreen
import com.gutierrez.biblioteca.ui.carrito.CarritoViewModel
import com.gutierrez.biblioteca.ui.detalle.DetalleScreen
import com.gutierrez.biblioteca.ui.detalle.DetalleViewModel
import com.gutierrez.biblioteca.ui.favoritos.FavoritosScreen
import com.gutierrez.biblioteca.ui.favoritos.FavoritosViewModel
import com.gutierrez.biblioteca.ui.lista.ListaScreen
import com.gutierrez.biblioteca.ui.lista.ListaViewModel
import com.gutierrez.biblioteca.ui.login.LoginScreen
import com.gutierrez.biblioteca.ui.login.LoginViewModel
import com.gutierrez.biblioteca.ui.registro.RegistroScreen
import com.gutierrez.biblioteca.ui.registro.RegistroViewModel

/** Rutas de la app. */
object Rutas {
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val LISTA = "lista"
    const val DETALLE = "detalle/{id}"
    const val FAVORITOS = "favoritos"
    const val CARRITO = "carrito"
    const val ADMIN = "admin"

    fun detalle(id: Int) = "detalle/$id"
}

/** Grafo de navegacion: crea cada ViewModel con el repositorio del contenedor. */
@Composable
fun AppNav(
    container: AppContainer,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Rutas.LOGIN) {
        composable(Rutas.LOGIN) {
            val viewModel: LoginViewModel = viewModel(
                factory = LoginViewModel.factory(container.authRepository)
            )
            LoginScreen(
                viewModel = viewModel,
                onIngreso = {
                    navController.navigate(Rutas.LISTA) {
                        popUpTo(Rutas.LOGIN) { inclusive = true }
                    }
                },
                onRegistrar = { navController.navigate(Rutas.REGISTRO) }
            )
        }
        composable(Rutas.REGISTRO) {
            val viewModel: RegistroViewModel = viewModel(
                factory = RegistroViewModel.factory(container.authRepository)
            )
            RegistroScreen(
                viewModel = viewModel,
                onRegistrado = {
                    navController.navigate(Rutas.LISTA) {
                        popUpTo(Rutas.LOGIN) { inclusive = true }
                    }
                },
                onVolver = { navController.popBackStack() }
            )
        }
        composable(Rutas.LISTA) {
            val viewModel: ListaViewModel = viewModel(
                factory = ListaViewModel.factory(
                    container.itemRepository,
                    container.carritoRepository,
                    container.authRepository
                )
            )
            ListaScreen(
                viewModel = viewModel,
                onItemClick = { id -> navController.navigate(Rutas.detalle(id)) },
                onVerFavoritos = { navController.navigate(Rutas.FAVORITOS) },
                onVerCarrito = { navController.navigate(Rutas.CARRITO) },
                onVerAdmin = { navController.navigate(Rutas.ADMIN) },
                onCerrarSesion = {
                    viewModel.cerrarSesion()
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(Rutas.LISTA) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = Rutas.DETALLE,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("id") ?: return@composable
            val viewModel: DetalleViewModel = viewModel(
                factory = DetalleViewModel.factory(
                    container.itemRepository,
                    container.carritoRepository,
                    id
                )
            )
            DetalleScreen(
                viewModel = viewModel,
                onVolver = { navController.popBackStack() }
            )
        }
        composable(Rutas.FAVORITOS) {
            val viewModel: FavoritosViewModel = viewModel(
                factory = FavoritosViewModel.factory(
                    container.itemRepository,
                    container.carritoRepository
                )
            )
            FavoritosScreen(
                viewModel = viewModel,
                onItemClick = { id -> navController.navigate(Rutas.detalle(id)) },
                onVolver = { navController.popBackStack() },
                onVerCarrito = { navController.navigate(Rutas.CARRITO) },
                onCerrarSesion = {
                    container.authRepository.cerrarSesion()
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(Rutas.LISTA) { inclusive = true }
                    }
                }
            )
        }
        composable(Rutas.CARRITO) {
            val viewModel: CarritoViewModel = viewModel(
                factory = CarritoViewModel.factory(container.carritoRepository)
            )
            CarritoScreen(
                viewModel = viewModel,
                onInicio = {
                    navController.navigate(Rutas.LISTA) {
                        popUpTo(Rutas.LISTA) { inclusive = true }
                    }
                },
                onFavoritos = { navController.navigate(Rutas.FAVORITOS) },
                onCerrarSesion = {
                    container.authRepository.cerrarSesion()
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(Rutas.LISTA) { inclusive = true }
                    }
                }
            )
        }
        composable(Rutas.ADMIN) {
            val viewModel: AdminViewModel = viewModel(
                factory = AdminViewModel.factory(
                    container.itemRepository,
                    container.authRepository,
                    container.notificador
                )
            )
            AdminScreen(
                viewModel = viewModel,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
