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
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date
import java.util.Timer
import java.util.TimerTask


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
                    navController.navigate("positive") {
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
                    navController.navigate("negative") {
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
@Composable
fun VoteSelectionScreen(navController: NavHostController) {
    val firestore = FirebaseFirestore.getInstance()
    var voteItems by remember { mutableStateOf<List<VoteItem>>(emptyList()) }

    LaunchedEffect(Unit) {
        firestore.collection("vote").get()
            .addOnSuccessListener { result ->
                voteItems = result.map { document ->
                    VoteItem(
                        id = document.id,
                        boton1 = document.getLong("boton1")?.toInt() ?: 0,
                        boton2 = document.getLong("boton2")?.toInt() ?: 0,
                        color = document.getString("color") ?: "",
                        createdAt = document.getTimestamp("created_at")?.toDate() ?: Date()
                    )
                }
            }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        voteItems.forEach { item ->
            Button(
                onClick = { navController.navigate("colorVerification/${item.id}") },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Vote Item: ${item.id}")
            }
        }
    }
}

data class VoteItem(
    val id: String,
    val boton1: Int,
    val boton2: Int,
    val color: String,
    val createdAt: Date
)
@Composable
fun ColorVerificationScreen(navController: NavHostController, voteId: String) {
    val firestore = FirebaseFirestore.getInstance()
    var voteItem by remember { mutableStateOf<VoteItem?>(null) }
    var selectedColor by remember { mutableStateOf("") }
    var timeLeft by remember { mutableStateOf(30) }
    val timer = remember { Timer() }

    LaunchedEffect(voteId) {
        firestore.collection("vote").document(voteId).get()
            .addOnSuccessListener { document ->
                voteItem = VoteItem(
                    id = document.id,
                    boton1 = document.getLong("boton1")?.toInt() ?: 0,
                    boton2 = document.getLong("boton2")?.toInt() ?: 0,
                    color = document.getString("color") ?: "",
                    createdAt = document.getTimestamp("created_at")?.toDate() ?: Date()
                )
            }
    }

    LaunchedEffect(Unit) {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (timeLeft > 0) {
                    timeLeft--
                } else {
                    timer.cancel()
                }
            }
        }, 1000, 1000)
    }

    if (timeLeft == 0) {
        if (selectedColor == voteItem?.color) {
            navController.navigate("feedback")
        } else {
            // Handle incorrect color selection
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Select the correct color for ${voteItem?.id}")
        Row {
            Button(onClick = { selectedColor = "green" }) {
                Text("Green")
            }
            Button(onClick = { selectedColor = "red" }) {
                Text("Red")
            }
            Button(onClick = { selectedColor = "blue" }) {
                Text("Blue")
            }
        }
        Text("Time left: $timeLeft seconds")
    }
}

@Composable
fun FeedbackScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("How was your class today?", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { navController.navigate("positive") },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Positive Review", color = Color.White)
            }
            Button(
                onClick = { navController.navigate("negative") },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
            ) {
                Text("Negative Review", color = Color.White)
            }
        }
    }
}

@Composable
fun PositiveScreen(navController: NavHostController, carnet: String, validarUser: ValidarUser) {
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
                    validarUser.modificarRacha(carnet,
                        onSuccess = {
                            navController.navigate("streaks") {
                                popUpTo("buttonScreen") { inclusive = true } // Avoid returning to ButtonScreen
                            }
                        },
                        onFailure = { e ->
                            Log.w("TAG", "Error al actualizar la racha", e)
                        }
                    )
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Submit Review")
            }
        }
    }
}

@Composable
fun NegativeScreen(navController: NavHostController, carnet: String, validarUser: ValidarUser) {
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
                text = "Thank you for your feedback, we're sorry your experience wasn't good!",
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
                    validarUser.modificarRacha(carnet,
                        onSuccess = {
                            navController.navigate("streaks") {
                                popUpTo("buttonScreen") { inclusive = true } // Avoid returning to ButtonScreen
                            }
                        },
                        onFailure = { e ->
                            Log.w("TAG", "Error al actualizar la racha", e)
                        }
                    )
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Submit Review")
            }
        }
    }
}