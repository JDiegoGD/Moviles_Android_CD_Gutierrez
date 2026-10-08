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
import com.gutierrez.biblioteca.ui.lista.ListaScreen
import com.gutierrez.biblioteca.ui.lista.ListaViewModel

/** Rutas de la app. Las siguientes pantallas (favoritos, login, admin) se agregan aqui. */
object Rutas {
    const val LISTA = "lista"
    const val DETALLE = "detalle/{id}"

    fun detalle(id: Int) = "detalle/$id"
}

/** Grafo de navegacion: crea cada ViewModel con el repositorio del contenedor. */
@Composable
fun AppNav(
    container: AppContainer,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Rutas.LISTA) {
        composable(Rutas.LISTA) {
            val viewModel: ListaViewModel = viewModel(
                factory = ListaViewModel.factory(container.itemRepository)
            )
            ListaScreen(
                viewModel = viewModel,
                onItemClick = { id -> navController.navigate(Rutas.detalle(id)) }
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
    }
}
