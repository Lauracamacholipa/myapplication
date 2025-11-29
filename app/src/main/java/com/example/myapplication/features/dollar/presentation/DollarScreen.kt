package com.example.myapplication.features.dollar.presentation

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.navigation.Screen
import org.koin.androidx.compose.koinViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DollarScreen(
    navController: NavController,
    viewModelDollar: DollarViewModel = koinViewModel(), // CORREGIDO: koinViewModel en lugar de hiltViewModel
) {
    val state = viewModelDollar.uiState.collectAsState()
    val history by viewModelDollar.history.collectAsState()
    val currentTime by viewModelDollar.currentTime.collectAsState()

    // FORZAR RECOMPOSICIÓN CADA SEGUNDO - SOLUCIÓN CLAVE
    var forceRecompose by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            forceRecompose++
        }
    }

    // DEBUG: Verificar que la UI se está actualizando
    LaunchedEffect(currentTime) {
        Log.d("UI_Debug", "🔄 UI actualizada con: $currentTime")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // TARJETA DE FECHA/HORA EN TIEMPO REAL
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "🕒 FECHA/HORA ACTUAL",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Blue,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = currentTime,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF1565C0),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    "Sincronizado con servidor en tiempo real",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // BOTÓN PARA ACTUALIZACIÓN MANUAL
        Button(
            onClick = {
                viewModelDollar.refreshTime()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("🔄 Actualizar Hora Manualmente")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // SECCIÓN ORIGINAL DEL DÓLAR
        Text("CAMBIOS DEL DÓLAR", style = MaterialTheme.typography.headlineMedium)

        when (val stateValue = state.value) {
            is DollarViewModel.DollarUIState.Error -> Text(
                "Error: ${stateValue.message}",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
            DollarViewModel.DollarUIState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Text("Cargando datos del dólar...", modifier = Modifier.padding(8.dp))
                }
            }
            is DollarViewModel.DollarUIState.Success -> {
                val data = stateValue.data

                // Mostrar la fecha de actualización del dólar (separada de la hora del servidor)
                val dollarUpdateTime = remember(data) {
                    val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                    format.format(System.currentTimeMillis())
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DollarCard("Oficial", data.dollarOfficial)
                    DollarCard("Paralelo", data.dollarParalelo)
                    DollarCard("USDT", data.usdt)
                    DollarCard("USDC", data.usdc)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "Datos actualizados: $dollarUpdateTime",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // BOTÓN PARA NAVEGAR A PELÍCULAS
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { navController.navigate(Screen.MoviesScreen.route) }
        ) {
            Text("🎬 Ver Películas")
        }
    }

    /* SECCIÓN COMENTADA DEL HISTORIAL
    Spacer(Modifier.height(16.dp))

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