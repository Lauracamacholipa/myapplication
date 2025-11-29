package com.example.myapplication.features.login.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel

@Composable
fun LogInPage(vm: LogInViewModel = koinViewModel(), onSuccess:() -> Unit){
    var userSignIn by remember { mutableStateOf("") }
    var passwordSignIn by remember { mutableStateOf("") }
    val state by vm.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", fontSize = 24.sp)

        TextField(
            value = userSignIn,
            onValueChange = { userSignIn = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = passwordSignIn,
            onValueChange = { passwordSignIn = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { vm.login(userSignIn, passwordSignIn) }
        ) {
            Text("Ingresar")
        }

        when (val st = state) {
            is LogInViewModel.LoginStateUI.Init -> Text("Esperando credenciales...")
            is LogInViewModel.LoginStateUI.Loading -> Text("Verificando...")
            is LogInViewModel.LoginStateUI.Error -> Text("Error: ${st.message}", color = Color.Red)
            is LogInViewModel.LoginStateUI.Success -> {
                Text("Bienvenido ${st.user.username}!")
                onSuccess() // cuando loguea con éxito
            }

            is LogInViewModel.LoginStateUI.Error -> TODO()
            LogInViewModel.LoginStateUI.Init -> TODO()
            LogInViewModel.LoginStateUI.Loading -> TODO()
            is LogInViewModel.LoginStateUI.Success -> TODO()
        }
    }
}