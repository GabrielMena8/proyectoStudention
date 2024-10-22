package com.example.studention

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import java.util.regex.Pattern

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Login(navController: NavHostController) {
    var email = remember { mutableStateOf(TextFieldValue("")) }
    var password = remember { mutableStateOf(TextFieldValue("")) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    val emailPattern = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    )
    val isEmailValid: (String) -> Boolean = { emailPattern.matcher(it).matches() }

    val isPasswordValid: (String) -> Boolean = {
        it.length >= 6 && it.any { char -> char.isLetter() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.c),
            contentDescription = "Logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Columna centrada
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center, // Centrar verticalmente
            horizontalAlignment = Alignment.CenterHorizontally // Centrar horizontalmente
        ) {
            // Card para el campo de email
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = MaterialTheme.shapes.medium // Bordes redondeados
            ) {
                TextField(
                    value = email.value,
                    onValueChange = {
                        email.value = it
                        emailError = !isEmailValid(it.text)
                    },
                    label = { Text("Ingresa tu correo electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = emailError,
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Blue,
                        unfocusedIndicatorColor = Color.Gray,
                        errorIndicatorColor = Color.Red
                    )
                )
            }

            if (emailError) {
                Text(
                    text = "Correo no válido. Ejemplo: usuario@ejemplo.com",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            // Card para el campo de password
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = MaterialTheme.shapes.medium // Bordes redondeados
            ) {
                TextField(
                    value = password.value,
                    onValueChange = {
                        password.value = it
                        passwordError = !isPasswordValid(it.text)
                    },
                    label = { Text("Ingresa tu Contraseña") },
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

            Spacer(modifier = Modifier.weight(1f)) // Espacio flexible para centrar

            // Botón de inicio de sesión
            Button(
                onClick = {
                    if (isEmailValid(email.value.text) && isPasswordValid(password.value.text)) {
                        navController.navigate("main")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp), // Botón ocupa el ancho completo
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue) // Color del botón
            ) {
                Text("Iniciar sesión", color = Color.White)
            }
        }
    }
}
