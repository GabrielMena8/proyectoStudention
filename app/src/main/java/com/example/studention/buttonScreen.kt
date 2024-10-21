package com.example.studention
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextAlign


@Composable
fun ButtonScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "¿Cómo fue tu clase de hoy?",
            modifier = Modifier.padding(bottom = 32.dp),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Botón de revisión positiva
            Button(
                onClick = {
                    navController.navigate("positive") {
                        popUpTo("buttonScreen") { inclusive = true } // Evita volver a ButtonScreen
                    }
                },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)) // Color verde
            ) {
                Text("Revisión Positiva", color = Color.White)
            }

            // Botón de revisión negativa
            Button(
                onClick = {
                    navController.navigate("negative") {
                        popUpTo("buttonScreen") { inclusive = true } // Evita volver a ButtonScreen
                    }
                },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)) // Color rojo
            ) {
                Text("Revisión Negativa", color = Color.White)
            }
        }
    }
}
@Composable
fun PositiveScreen(navController: NavHostController) {
    // Fondo verde suave
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F7E0)), // Color verde suave
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Título
            Text(
                text = "Revisión Positiva",
                color = Color.Black,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Mensaje de agradecimiento
            Text(
                text = "¡Gracias por tu opinión positiva!",
                color = Color.Green,
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Bloque de feedback opcional
            Text(
                text = "¿Tienes algún comentario adicional?",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Comentarios son opcionales. Puedes omitirlo.",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Campo de texto para comentarios adicionales
            var comment by remember { mutableStateOf("") }
            TextField(
                value = comment,
                onValueChange = { comment = it },
                placeholder = { Text("Escribe tu comentario aquí...") },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Green,
                    unfocusedIndicatorColor = Color.LightGray
                )
            )

            // Botón para enviar la revisión
            Button(
                onClick = { navController.navigate("main") }, // Navegar a MainScreen
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Enviar revisión")
            }
        }
    }
}

@Composable
fun NegativeScreen(navController: NavHostController) {
    // Fondo rojo suave
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFE0E0)), // Color rojo suave
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Título
            Text(
                text = "Revisión Negativa",
                color = Color.Black,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Mensaje de disculpa
            Text(
                text = "¡Gracias por tu opinión, lamentamos que tu experiencia no fue buena!",
                color = Color.Red,
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Bloque de feedback opcional
            Text(
                text = "¿Tienes algún comentario adicional?",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Comentarios son opcionales. Puedes omitirlo.",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Campo de texto para comentarios adicionales
            var comment by remember { mutableStateOf("") }
            TextField(
                value = comment,
                onValueChange = { comment = it },
                placeholder = { Text("Escribe tu comentario aquí...") },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Red,
                    unfocusedIndicatorColor = Color.LightGray
                )
            )

            // Botón para enviar la revisión
            Button(
                onClick = { navController.navigate("main") }, // Navegar a MainScreen
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Enviar revisión")
            }
        }
    }
}
