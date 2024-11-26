package com.example.studention.views

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studention.R
import com.example.studention.ValidarUser
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    val validarUser = remember { ValidarUser(context) }
    var carnet by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var carnetError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    val isCarnetValid: (String) -> Boolean = { it.isNotEmpty() }
    val isPasswordValid: (String) -> Boolean = { it.length >= 6 && it.any { char -> char.isLetter() } }

    fun loginUser(carnet: String, password: String) {
        validarUser.obtenerDatosCarnet(carnet,
            onSuccess = { user ->
                if (user.password == password) {
                    Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    navController.navigate("classes/$carnet") {
                        popUpTo("login") { inclusive = true }
                    }
                } else {
                    Toast.makeText(context, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                }
            },
            onFailure = {
                Toast.makeText(context, "No existe usuario con este carnet", Toast.LENGTH_SHORT).show()
            }
        )
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.c),
            contentDescription = "Logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                TextField(
                    value = carnet,
                    onValueChange = {
                        carnet = it
                        carnetError = !isCarnetValid(it)
                    },
                    label = { Text("Carnet") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = carnetError,
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Blue,
                        unfocusedIndicatorColor = Color.Gray,
                        errorIndicatorColor = Color.Red
                    )
                )
            }

            if (carnetError) {
                Text(
                    text = "Carnet no válido.",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                TextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = !isPasswordValid(it)
                    },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    isError = passwordError,
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Blue,
                        unfocusedIndicatorColor = Color.Gray,
                        errorIndicatorColor = Color.Red
                    )
                )
            }

            if (passwordError) {
                Text(
                    text = "La contraseña debe tener al menos 6 caracteres y una letra.",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (isCarnetValid(carnet) && isPasswordValid(password)) {
                        loginUser(carnet, password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text("Iniciar sesión", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("register") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("¿No te has registrado?", color = Color.White)
            }
        }
    }
}