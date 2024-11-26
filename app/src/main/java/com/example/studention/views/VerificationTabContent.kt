package com.example.studention.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationTabContent(navController: NavHostController, classId: String, carnet: String) {
    val db = FirebaseFirestore.getInstance()
    var correctColor by remember { mutableStateOf<Color?>(null) }
    var options by remember { mutableStateOf<List<Color>>(emptyList()) }
    var resultMessage by remember { mutableStateOf("") }
    var classDetails by remember { mutableStateOf<Map<String, Any>?>(null) }
    var timeLeft by remember { mutableStateOf(30) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(classId) {
        db.collection("voto").document(classId).get().addOnSuccessListener { document ->
            val colorName = document.getString("color") ?: "red"
            correctColor = mapColorNameToColor(colorName)
            options = generateColorOptions(correctColor!!)
        }.addOnFailureListener {
            resultMessage = "Failed to load data. Please try again."
        }

        db.collection("clase").document(classId).get().addOnSuccessListener { document ->
            classDetails = document.data
        }.addOnFailureListener {
            resultMessage = "Failed to load class details. Please try again."
        }

        scope.launch {
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
            if (timeLeft == 0) {
                navController.navigate("classes/$carnet")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Verificación", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6200EE))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (correctColor == null || classDetails == null) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text(
                    text = "Detalles de la Clase",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF6200EE),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF6200EE))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Materia: ${classDetails!!["materia"]}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                        Text(
                            text = "Aula: ${classDetails!!["aula"]}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Text(
                    text = "Escoge el color correcto para verificar tu asistencia:",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF6200EE),
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Text(
                    text = "Tiempo restante: $timeLeft segundos",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Red,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    options.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(color, shape = CircleShape)
                                .border(2.dp, Color.Black, shape = CircleShape)
                                .clickable {
                                    if (color == correctColor) {
                                        navController.navigate("buttonScreen/$classId/$carnet")
                                    } else {
                                        navController.navigate("classes/$carnet")
                                    }
                                }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = resultMessage, style = MaterialTheme.typography.bodyLarge, color = Color(0xFF6200EE))
            }
        }
    }
}

fun generateColorOptions(correctColor: Color): List<Color> {
    val allColors = listOf(
        Color.Red,    // Red
        Color.Green,  // Green
        Color.Blue,   // Blue
        Color(0xFFFFD230), // Light Yellow
        Color(0xFFB100CE)  // Light Magenta
    )
    val otherColors = allColors.filter { it != correctColor }.shuffled()
    return (listOf(correctColor) + otherColors.take(2)).shuffled()
}

// Helper function to map color names to Compose colors
fun mapColorNameToColor(colorName: String): Color {
    return when (colorName.lowercase()) {
        "red" -> Color.Red
        "green" -> Color.Green
        "blue" -> Color.Blue
        "yellow" -> Color.Yellow
        "magenta" -> Color.Magenta
        else -> Color.Gray // Default if the color is unknown
    }
}