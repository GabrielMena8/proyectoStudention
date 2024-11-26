package com.example.studention.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.database.database.UsersUtil
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassesTabContent(navController: NavHostController) {
    val usersUtil = UsersUtil()
    var classes by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var profesores by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var selectedClassId by remember { mutableStateOf<String?>(null) }

    // Fetch classes and professors when the composable is first composed
    LaunchedEffect(Unit) {
        usersUtil.obtenerTodosProfesores(
            onSuccess = { fetchedProfesores ->
                profesores = fetchedProfesores
                usersUtil.obtenerTodasLasClases(
                    onSuccess = { fetchedClasses ->
                        classes = fetchedClasses
                        loading = false
                    },
                    onFailure = { exception ->
                        // Handle the error (e.g., show a message)
                        loading = false
                    }
                )
            },
            onFailure = { exception ->
                // Handle the error (e.g., show a message)
                loading = false
            }
        )
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Clases que tuviste hoy", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            if (loading) {
                CircularProgressIndicator() // Show a loading indicator
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(classes) { clase ->
                        val profesor = profesores.find { prof ->
                            (prof["clases"] as? List<*>)?.contains(clase["id"]) == true
                        }
                        val profesorNombre = profesor?.get("nombre") ?: "Sin profesor"
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xCC6200FF)) // Light purple color
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "Materia: ${clase["materia"]}", style = MaterialTheme.typography.bodyLarge, color = Color.White)
                                Text(
                                    text = "Profesor: $profesorNombre",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        selectedClassId = clase["id"] as? String
                                        selectedClassId?.let { id ->
                                            navController.navigate("verification/$id")
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                                ) {
                                    Text(text = "Verificar Asistencia", color = Color(0xCC6200FF)) // Light purple text color
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}