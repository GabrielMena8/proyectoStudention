package com.example.studention

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.studention.ui.theme.StudentionTheme
import java.util.regex.Pattern

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudentionTheme {
                App()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App() {
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
            .fillMaxWidth()
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
        ) {

            TextField(
                value = email.value,
                onValueChange = {
                    email.value = it
                    emailError = !isEmailValid(it.text)
                },
                label = { Text("Ingresa tu correo electrónico") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 64.dp),
                isError = emailError
            )

            if (emailError) {
                Text(
                    text = "Correo no válido. Ejemplo: usuario@ejemplo.com",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            TextField(
                value = password.value,
                onValueChange = {
                    password.value = it
                    passwordError = !isPasswordValid(it.text)
                },
                label = { Text("Ingresa tu Contraseña") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                isError = passwordError
            )

            if (passwordError) {
                Text(
                    text = "La contraseña debe tener al menos 6 caracteres y una letra.",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.weight(3f))

            Button(
                onClick = {
                    if (isEmailValid(email.value.text) && isPasswordValid(password.value.text)) {
                        // Funcion para iniciar sesion...
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Iniciar sesión")
            }

        }
    }
}