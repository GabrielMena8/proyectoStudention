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

@Composable
fun ButtonScreen(navController: NavHostController, classId: String) {
    val context = LocalContext.current
    val validarUser = remember { ValidarUser(context) }
    var carnet by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        validarUser.obtenerCarnetUsuarioActual(
            onSuccess = { carnetValue ->
                carnet = carnetValue
                isLoading = false
            },
            onFailure = { e ->
                Log.w("TAG", "Error al obtener el carnet", e)
                isLoading = false
            }
        )
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "How was your class today?",
                modifier = Modifier.padding(bottom = 32.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Positive review button
                Button(
                    onClick = {
                        navController.navigate("positive/$classId/$carnet") {
                            popUpTo("buttonScreen") { inclusive = true } // Avoid returning to ButtonScreen
                        }
                    },
                    modifier = Modifier.padding(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)) // Green color
                ) {
                    Text("Positive Review", color = Color.White)
                }

                // Negative review button
                Button(
                    onClick = {
                        navController.navigate("negative/$classId/$carnet") {
                            popUpTo("buttonScreen") { inclusive = true } // Avoid returning to ButtonScreen
                        }
                    },
                    modifier = Modifier.padding(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)) // Red color
                ) {
                    Text("Negative Review", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun PositiveScreen(navController: NavHostController, classId: String, carnet: String, validarUser: ValidarUser) {
    // Soft green background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F7E0)), // Soft green color
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title
            Text(
                text = "Positive Review",
                color = Color.Black,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Thank you message
            Text(
                text = "Thank you for your positive feedback!",
                color = Color.Green,
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Optional feedback block
            Text(
                text = "Do you have any additional comments?",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Comments are optional. You can skip this.",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Text field for additional comments
            var comment by remember { mutableStateOf("") }
            TextField(
                value = comment,
                onValueChange = { comment = it },
                placeholder = { Text("Write your comment here...") },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Green,
                    unfocusedIndicatorColor = Color.LightGray
                )
            )

            // Button to submit the review
            Button(
                onClick = {
                    // Handle review submission
                    navController.navigate("main")
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Submit Review")
            }
        }
    }
}

@Composable
fun NegativeScreen(navController: NavHostController, classId: String, carnet: String, validarUser: ValidarUser) {
    // Soft red background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFE0E0)), // Soft red color
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title
            Text(
                text = "Negative Review",
                color = Color.Black,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Apology message
            Text(
                text = "We are sorry to hear that you had a negative experience.",
                color = Color.Red,
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Optional feedback block
            Text(
                text = "Do you have any additional comments?",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Comments are optional. You can skip this.",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Text field for additional comments
            var comment by remember { mutableStateOf("") }
            TextField(
                value = comment,
                onValueChange = { comment = it },
                placeholder = { Text("Write your comment here...") },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Red,
                    unfocusedIndicatorColor = Color.LightGray
                )
            )

            // Button to submit the review
            Button(
                onClick = {
                    // Handle review submission
                    navController.navigate("main")
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Submit Review")
            }
        }
    }
}