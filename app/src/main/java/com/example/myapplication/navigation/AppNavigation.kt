package com.example.myapplication.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.features.webview.presentation.AtuladoScreen
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.features.dollar.presentation.DollarScreen
import com.example.myapplication.features.github.presentation.GithubScreen
import com.example.myapplication.features.login.presentation.LogInPage
import com.example.myapplication.features.movies.presentation.MoviesScreen
import com.example.myapplication.features.profile.presentation.ProfileScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import com.example.myapplication.features.movies.domain.model.MovieModel
import com.example.myapplication.features.movies.presentation.MovieDetailScreen
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder
import kotlinx.serialization.encodeToString

@Composable
fun AppNavigation(navigationViewModel: NavigationViewModel ,
                  modifier: Modifier,
                  navController: NavHostController){

    // Manejar navegación desde el ViewModel
    LaunchedEffect (Unit) {
        navigationViewModel.navigationCommand.collect { command ->
            when (command) {
                is NavigationViewModel.NavigationCommand.NavigateTo -> {
                    navController.navigate(command.route) {
                        // Configuración del back stack según sea necesario
                        when (command.options) {
                            NavigationOptions.CLEAR_BACK_STACK -> {
                                popUpTo(0) // Limpiartodo el back stack
                            }
                            NavigationOptions.REPLACE_HOME -> {
                                popUpTo(Screen.Dollar.route) { inclusive = true }
                            }
                            else -> {
                                // Navegación normal
                            }
                        }
                    }
                }
                is NavigationViewModel.NavigationCommand.PopBackStack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Atulado.route,
        modifier = modifier
    ){
        composable(Screen.Dollar.route) {
            DollarScreen(navController)
        }

        composable(Screen.GithubScreen.route) {
            GithubScreen(modifier = Modifier.padding())
        }

        composable(Screen.MoviesScreen.route){
            MoviesScreen(navigateToDetail = { movie ->
                // Serializar el objeto Movie a JSON
                val movieJson = Json.encodeToString(movie)
                // Codificar el JSON para que sea seguro en URL
                val encodedMovieJson = URLEncoder.encode(movieJson, "UTF-8")

                // Navegar pasando el JSON codificado como parámetro
                navController.navigate(
                    "${Screen.MovieDetail.route}/${encodedMovieJson}"
                )
            })
        }

        composable(
            route = "${Screen.MovieDetail.route}/{movie}",
            arguments = listOf(
                navArgument("movie") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            // Obtener el JSON codificado desde los argumentos
            val movieJson = backStackEntry.arguments?.getString("movie") ?: ""
            // Decodificar el JSON
            val movieDecoded = URLDecoder.decode(movieJson, "UTF-8")
            // Deserializar el JSON de vuelta a objeto MovieModel
            val movie = Json.decodeFromString<MovieModel>(movieDecoded)

            MovieDetailScreen(
                movie = movie,
                back = {
                    navController.popBackStack()
                }
            )
        }


        composable(Screen.Profile.route){
            ProfileScreen()
        }

        composable(Screen.LogInPage.route){
            LogInPage(
                onSuccess = {
                    navController.navigate(Screen.GithubScreen.route)
                }
            )
        }

        composable(
            Screen.Atulado.route
        ) {
            AtuladoScreen(
                "https://www.bisa.com/atulado",
                postData = null,
                modifier = modifier,
                shouldStopBrowsing = { true }
            )
        }
    }
}