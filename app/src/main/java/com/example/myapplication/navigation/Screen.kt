package com.example.myapplication.navigation

sealed class Screen(val route: String) {
    object LogInPage : Screen("login")
    object GithubScreen : Screen("github")
    object MoviesScreen : Screen("movies")
    object Dollar: Screen("dollar")
    object Profile: Screen("profile")
    object MovieDetail: Screen("movieDetail")
    object Atulado: Screen("atulado")
}