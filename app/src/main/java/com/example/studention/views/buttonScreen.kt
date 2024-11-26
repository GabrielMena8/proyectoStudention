package com.example.studention.views

import android.util.Log
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
import com.example.studention.ValidarUser
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ButtonScreen(navController: NavHostController, classId: String, carnet: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "¿Cómo fue tú clase de hoy?",
            modifier = Modifier.padding(bottom = 32.dp),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    navController.navigate("positive/$classId/$carnet")
                },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Review Positive", color = Color.White)
            }

            Button(
                onClick = {
                    navController.navigate("negative/$classId/$carnet")
                },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
            ) {
                Text("Review Negativa", color = Color.White)
            }
        }
    }
}

@Composable
fun PositiveScreen(navController: NavHostController, classId: String, carnet: String, validarUser: ValidarUser) {
    val db = FirebaseFirestore.getInstance()
    var comentario by remember { mutableStateOf("") }
    var mostrarDialogo by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F7E0)), // Color verde suave
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¿Tienes algún comentario adicional?",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Los comentarios son opcionales. Puedes omitir esto.",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            TextField(
                value = comentario,
                onValueChange = { comentario = it },
                label = { Text("Escribe tu comentario aquí...") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    db.collection("voto").document(classId).update(
                        "reviewP", FieldValue.arrayUnion(comentario),
                        "asistencia", FieldValue.arrayUnion(carnet)
                    ).addOnSuccessListener {
                        mostrarDialogo = true
                    }.addOnFailureListener {
                        // Manejar el error
                    }
                },
                modifier = Modifier.padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)) // Color verde
            ) {
                Text("Enviar", color = Color.White)
            }
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Éxito") },
            text = { Text("Tu reseña ha sido enviada exitosamente.") },
            confirmButton = {
                Button(onClick = {
                    mostrarDialogo = false
                    navController.navigate("streaks/$carnet")
                }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun NegativeScreen(navController: NavHostController, classId: String, carnet: String, validarUser: ValidarUser) {
    val db = FirebaseFirestore.getInstance()
    var comentario by remember { mutableStateOf("") }
    var mostrarDialogo by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFE0E0)), // Color rojo suave
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¿Tienes algún comentario adicional?",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Los comentarios son opcionales. Puedes omitir esto.",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            TextField(
                value = comentario,
                onValueChange = { comentario = it },
                label = { Text("Escribe tu comentario aquí...") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    db.collection("voto").document(classId).update(
                        "reviewN", FieldValue.arrayUnion(comentario),
                        "asistencia", FieldValue.arrayUnion(carnet)
                    ).addOnSuccessListener {
                        mostrarDialogo = true
                    }.addOnFailureListener {
                        // Manejar el error
                    }
                },
                modifier = Modifier.padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)) // Color rojo
            ) {
                Text("Enviar", color = Color.White)
            }
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Éxito") },
            text = { Text("Tu reseña ha sido enviada exitosamente.") },
            confirmButton = {
                Button(onClick = {
                    mostrarDialogo = false
                    navController.navigate("streaks/$carnet")
                }) {
                    Text("OK")
                }
            }
        )
    }
}