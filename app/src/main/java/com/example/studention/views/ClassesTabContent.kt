package com.example.studention.views

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.studention.R
import com.database.database.UsersUtil
import com.example.studention.ValidarUser
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassesTabContent(navController: NavHostController, carnet: String) {
    val usersUtil = UsersUtil()
    val validationUser = ValidarUser(navController.context)
    var classes by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var profesores by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableStateOf(1) }
    val attendanceState = remember { mutableStateMapOf<String, Boolean>() } // Almacena el estado de asistencia por clase

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
                        loading = false
                    }
                )
            },
            onFailure = { exception ->
                loading = false
            }
        )
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTabIndex = selectedTab,
                onTabSelected = { index ->
                    selectedTab = index
                    when (index) {
                        1 -> navController.navigate("classes/$carnet")
                        2 -> navController.navigate("profile/$carnet")
                        3 -> navController.navigate("streaks/$carnet")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("login") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }) {
                Icon(Icons.Filled.ExitToApp, contentDescription = "Cerrar sesión")
            }
        }
    ) { paddingValues ->
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
                CircularProgressIndicator()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(classes) { clase ->
                        val profesor = profesores.find { prof ->
                            (prof["clases"] as? List<*>)?.contains(clase["id"]) == true
                        }

                        val profesorNombre = profesor?.get("nombre") ?: "Sin profesor"
                        val classId = clase["id"] as String

                        // Solo validar asistencia si no está almacenada en el estado
                        if (!attendanceState.containsKey(classId)) {
                            LaunchedEffect(classId) {
                                validationUser.validarEstudianteEnListaDeAsistencia(
                                    classId,
                                    carnet,
                                    onSuccess = { present ->
                                        attendanceState[classId] = present
                                    },
                                    onFailure = {
                                        attendanceState[classId] = false
                                    }
                                )
                            }
                        }

                        val isPresent = attendanceState[classId] ?: false

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xCC6200FF))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Materia: ${clase["materia"]}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.White
                                )
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
                                        if (!isPresent) {
                                            navController.navigate("verification/$classId/$carnet")
                                        }
                                    },
                                    enabled = !isPresent,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                                ) {
                                    Text(
                                        text = if (isPresent) "Asistencia Verificada" else "Verificar Asistencia",
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
