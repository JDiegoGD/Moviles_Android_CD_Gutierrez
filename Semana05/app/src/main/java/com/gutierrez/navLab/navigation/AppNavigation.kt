package com.gutierrez.navLab.navigation

import com.gutierrez.navLab.navigation.Screen
import androidx.compose.runtime.Composable

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.gutierrez.navLab.screens.HomeScreen
import com.gutierrez.navLab.screens.ListScreen
import com.gutierrez.navLab.screens.ProfileScreen
import com.gutierrez.navLab.screens.DetailScreen





@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ){
        //Ruta Inicio
        composable(route = Screen.Home.route){
            HomeScreen(navController = navController)
        }

        //Ruta List
        composable(route = Screen.List.route){
            ListScreen(navController = navController)
        }

        //Ruta Profile
        composable(route = Screen.Profile.route){
            ProfileScreen(navController = navController)
        }

        //Ruta Detail
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("itemId"){
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ){
            backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId")?:0
            DetailScreen(navController, itemId)
        }
    }
}