package com.example.myapplication.features.dollar.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.navigation.Screen
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DollarScreen(navController: NavController,
                 viewModelDollar: DollarViewModel = koinViewModel(),
                 ) {
    val state = viewModelDollar.uiState.collectAsState()
    val history by viewModelDollar.history.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("CAMBIOS DEL DOLAR", style = MaterialTheme.typography.headlineMedium)
        when (val stateValue = state.value) {
            is DollarViewModel.DollarUIState.Error -> Text(stateValue.message)
            DollarViewModel.DollarUIState.Loading -> CircularProgressIndicator()
            is DollarViewModel.DollarUIState.Success -> {

                val date = remember(stateValue.data) {
                    val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    format.format(System.currentTimeMillis())
                }

                DollarCard("Oficial", stateValue.data.dollarOfficial)
                DollarCard("Paralelo", stateValue.data.dollarParalelo)
                DollarCard("USDT", stateValue.data.usdt)
                DollarCard("USDC", stateValue.data.usdc)
                Text("Actualizado: $date", style = MaterialTheme.typography.headlineMedium)
            }
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {navController.navigate(Screen.MoviesScreen.route)}
        ) {
            Text("Ver Peliculas")
        }
    }


    /*Spacer(Modifier.height(16.dp))

    Text("Histórico", style = MaterialTheme.typography.titleMedium)
    LazyColumn {
        itemsIndexed(history) { index, dollar ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Oficial: ${dollar.dollarOfficial}")
                    Text("Paralelo: ${dollar.dollarParalelo}")
                    Text("Fecha: ${dollar.timestamp}")
                }

                IconButton(onClick = { viewModelDollar.deleteDollar(dollar) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar"
                    )
                }
            }
        }
    }*/
}
