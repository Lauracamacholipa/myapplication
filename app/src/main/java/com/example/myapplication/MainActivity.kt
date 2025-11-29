package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.features.maintenance.presentation.MaintenanceOverlay
import com.example.myapplication.features.maintenance.presentation.MaintenanceViewModel
import com.example.myapplication.navigation.AppNavigation
import com.example.myapplication.navigation.NavigationDrawer
import com.example.myapplication.navigation.NavigationViewModel
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    private val navigationViewModel: NavigationViewModel by viewModels()
    private var currentIntent: Intent? = null
    private var keepSplashOnScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {

        //splashscreen
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        currentIntent = intent

        splashScreen.setKeepOnScreenCondition {
            keepSplashOnScreen
        }

        lifecycleScope.launch {
            delay(4000)
            keepSplashOnScreen = false
        }

        enableEdgeToEdge()
        setContent {
            LaunchedEffect(Unit) {
                Log.d("MainActivity", "OnCreate - Procesando intent inicial")
                navigationViewModel.handleDeepLink(currentIntent)
            }

            LaunchedEffect(Unit) {
                snapshotFlow { currentIntent }
                    .distinctUntilChanged()
                    .collect{intent ->
                        Log.d("MainActivity", "Nuevo intent recibido: ${intent?.action}")
                        navigationViewModel.handleDeepLink(intent)
                    }
            }

            MainApp(navigationViewModel)
            //MyApplicationTheme {
                //Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    //GithubScreen(modifier = Modifier.padding(innerPadding))
               //     SignInPage()
             //   }
            //}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawerHost(
    coroutineScope: CoroutineScope,
    drawerState: DrawerState,
    navigationViewModel: NavigationViewModel,
    navController : NavHostController
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                modifier = Modifier.statusBarsPadding(),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            drawerState.open()
                        }
                    }) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menu")
                    }
                }
            )
        }
    ) {
        innnerPadding -> AppNavigation(
        navController = navController,
        navigationViewModel = navigationViewModel,
        modifier = Modifier.padding(innnerPadding))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp( navigationViewModel: NavigationViewModel) {
    val maintenanceViewModel: MaintenanceViewModel = koinViewModel()
    val maintenanceMode by maintenanceViewModel.maintenanceMode.collectAsState()
    val navController: NavHostController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val notShowTopBar =
        (currentDestination?.route?.startsWith(Screen.MovieDetail.route) == true) ||
                (currentDestination?.route?.startsWith(Screen.Atulado.route) == true)

    LaunchedEffect(maintenanceMode) {
        Log.d("MainApp", "Maintenance mode cambió a: $maintenanceMode")
    }

    if(maintenanceMode){
        MaintenanceOverlay()
    }else{
        val navController: NavHostController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        val navigationDrawerItems = listOf(
            NavigationDrawer.Profile,
            NavigationDrawer.Dollar,
            NavigationDrawer.Movie,
            NavigationDrawer.Github
        )
        val drawerState =
            rememberDrawerState(initialValue =
                androidx.compose.material3.DrawerValue.Closed)
        val coroutineScope = rememberCoroutineScope()
        val isMovieDetail = currentDestination?.route?.startsWith(Screen.MovieDetail.route) == true
        if (isMovieDetail) {
            AppNavigation(
                navController = navController,
                navigationViewModel = navigationViewModel,
                modifier = Modifier
            )
        }else{
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(
                        modifier = Modifier.width(256.dp)
                    ) {
                        Box(
                            modifier = Modifier.width(256.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                modifier = Modifier.width(120.dp),
                                painter = painterResource(id =
                                    R.drawable.ic_launcher_background),
                                contentDescription = "Logo",
                            )
                            Image(
                                painter = painterResource(id =
                                    R.drawable.ic_launcher_foreground),
                                contentDescription = "Logo",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        navigationDrawerItems.forEach { item ->
                            val isSelected = currentDestination?.route == item.route
                            NavigationDrawerItem(
                                icon = {
                                    Icon(
                                        imageVector = if (isSelected) item.
                                        selectedIcon else item.unselectedIcon,
                                        contentDescription = item.label
                                    )
                                },
                                label = { Text(item.label) },
                                selected = isSelected,
                                onClick = {
                                    navController.navigate(item.route) {
                                        launchSingleTop = true
                                        restoreState = true
                                        popUpTo(
                                            navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                    }
                                    coroutineScope.launch {
                                        drawerState.close()
                                    }
                                }
                            )
                        }
                    }
                }
            ) {
                NavigationDrawerHost(coroutineScope, drawerState, navigationViewModel, navController)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}