package com.example.studention.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.database.database.UsersUtil
import com.example.studention.R

//import com.google.androidgamesdk.gametextinput.Settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTabContent() {
    val usersUtil = UsersUtil()
    var classes by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedClass by remember { mutableStateOf<Map<String, Any>?>(null) }
    var profesores by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                actions = {
                    IconButton(onClick = { /* TODO: Add settings action */ }) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
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
            Image(
                painter = painterResource(id = R.drawable.add_svg),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
            )
            Text(text = "Mis clases", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            if (loading) {
                CircularProgressIndicator() // Show a loading indicator
            } else {
                LazyColumn {
                    items(classes) { clase ->
                        val profesor = profesores.find { prof ->
                            (prof["clases"] as? List<*>)?.contains(clase["id"]) == true
                        }
                        val profesorNombre = profesor?.get("nombre") ?: "Sin profesor"
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "Materia: ${clase["materia"]}", style = MaterialTheme.typography.bodyLarge)
                                Text(text = "Profesor: $profesorNombre", style = MaterialTheme.typography.bodyMedium)
                                Spacer(modifier = Modifier.height(8.dp))
                               /* Button(
                                    onClick = {
                                        // Handle button click
                                    }
                                ) {
                                    Text("Ver detalles")
                                }*/
                            }
                        }
                    }
                }
            }
        }
    }

    // Show dialog when button is clicked
    if (showDialog) {
        ClassSelectionDialog(classes, profesores, onDismiss = { showDialog = false }, onSelectClass = { selectedClass = it })
    }
}

@Composable
fun ClassSelectionDialog(classes: List<Map<String, Any>>, profesores: List<Map<String, Any>>, onDismiss: () -> Unit, onSelectClass: (Map<String, Any>) -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Selecciona una clase") },
        text = {
            LazyColumn {
                items(classes) { clase ->
                    val profesor = profesores.find { prof ->
                        (prof["clases"] as? List<*>)?.contains(clase["id"]) == true
                    }
                    val profesorNombre = profesor?.get("nombre") ?: "Sin profesor"
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${clase["id"]} - ${clase["materia"]} (Profesor: $profesorNombre)",
                            modifier = Modifier.weight(1f) // Take up space
                        )
                        Button(
                            onClick = {
                                onSelectClass(clase)
                            }
                        ) {
                            Text("Añadir")
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cerrar")
            }
        }
    )
}