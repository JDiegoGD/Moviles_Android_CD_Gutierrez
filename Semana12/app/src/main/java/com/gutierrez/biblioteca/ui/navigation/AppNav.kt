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
import com.gutierrez.biblioteca.ui.detalle.DetalleScreen
import com.gutierrez.biblioteca.ui.detalle.DetalleViewModel
import com.gutierrez.biblioteca.ui.favoritos.FavoritosScreen
import com.gutierrez.biblioteca.ui.favoritos.FavoritosViewModel
import com.gutierrez.biblioteca.ui.lista.ListaScreen
import com.gutierrez.biblioteca.ui.lista.ListaViewModel
import com.gutierrez.biblioteca.ui.login.LoginScreen
import com.gutierrez.biblioteca.ui.login.LoginViewModel

/** Rutas de la app. La siguiente pantalla (admin) se agrega aqui. */
object Rutas {
    const val LOGIN = "login"
    const val LISTA = "lista"
    const val DETALLE = "detalle/{id}"
    const val FAVORITOS = "favoritos"

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
                }
            )
        }
        composable(Rutas.LISTA) {
            val viewModel: ListaViewModel = viewModel(
                factory = ListaViewModel.factory(container.itemRepository, container.authRepository)
            )
            ListaScreen(
                viewModel = viewModel,
                onItemClick = { id -> navController.navigate(Rutas.detalle(id)) },
                onVerFavoritos = { navController.navigate(Rutas.FAVORITOS) },
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
                factory = DetalleViewModel.factory(container.itemRepository, id)
            )
            DetalleScreen(
                viewModel = viewModel,
                onVolver = { navController.popBackStack() }
            )
        }
        composable(Rutas.FAVORITOS) {
            val viewModel: FavoritosViewModel = viewModel(
                factory = FavoritosViewModel.factory(container.itemRepository)
            )
            FavoritosScreen(
                viewModel = viewModel,
                onItemClick = { id -> navController.navigate(Rutas.detalle(id)) },
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
