package com.example.myapplication.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationDrawer(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
) {
    data object Profile : NavigationDrawer(
        "Profile",
        Icons.Filled.Home,
        Icons.Outlined.Home,
        Screen.Profile.route)
    data object Dollar : NavigationDrawer(
        "Dollar",
        Icons.Filled.ShoppingCart,
        Icons.Outlined.ShoppingCart,
        Screen.Dollar.route)
    data object Github : NavigationDrawer(
        "Github",
        Icons.Filled.Favorite,
        Icons.Outlined.FavoriteBorder,
        Screen.GithubScreen.route)
    data object Movie : NavigationDrawer(
        "Movie",
        Icons.Filled.DateRange,
        Icons.Outlined.DateRange,
        Screen.MoviesScreen.route)
}