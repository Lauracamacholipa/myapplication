package com.example.myapplication.features.github.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun GithubScreen(modifier: Modifier,
                 vm: GirhubViewModel = koinViewModel()
                ){
    var nickname by remember { mutableStateOf("") }
    val state by vm.state.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Github Search", fontSize = 24.sp)

        OutlinedTextField(
            value = nickname,
            onValueChange = { nickname = it },
            label = { Text("Nickname") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedButton(
            onClick = { vm.fetchAlias(nickname) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Text("Buscar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val st = state) {
            is GirhubViewModel.GithubStateUI.Error -> {
                Text(st.message, color = Color.Red)
            }
            GirhubViewModel.GithubStateUI.Init -> {
                Text("Esperando búsqueda...")
            }
            GirhubViewModel.GithubStateUI.Loading -> {
                Text("Buscando...")
            }
            is GirhubViewModel.GithubStateUI.Success -> {
                Text("Usuario: ${st.github.nickName}")
                AsyncImage(
                    model = st.github.pathURL.value,
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .padding(top = 10.dp),
                    contentScale = ContentScale.Crop,
                )
                Text(st.github.pathURL.value, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}