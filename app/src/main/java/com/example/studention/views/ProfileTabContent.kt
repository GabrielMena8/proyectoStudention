package com.example.studention.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.database.database.UsersUtil
import com.example.studention.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue

@Composable
fun ProfileTabContent(carnet: String) {
    val usersUtil = UsersUtil()
    var classes by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(carnet) {
        usersUtil.obtenerClasesUsuario(
            carnet = carnet,
            onSuccess = { classDetails ->
                classes = classDetails
                loading = false
            },
            onFailure = { exception ->
                loading = false
            }
        )
    }

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
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
            Text(text = "Mis clases", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            if (loading) {
                CircularProgressIndicator()
            } else {
                LazyColumn {
                    items(classes) { clase ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "Materia: ${clase["materia"]}", style = MaterialTheme.typography.bodyLarge)
                                Text(text = "Aula: ${clase["aula"]}", style = MaterialTheme.typography.bodyMedium)
                                Text(text = "Profesor: ${clase["profesor"]}", style = MaterialTheme.typography.bodyMedium)
                                Text(text = "Cantidad de Estudiantes: ${clase["cant_estudiantes"]}", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            text = {
                AddClassScreen(firestore = FirebaseFirestore.getInstance(), carnet = carnet) { classId ->
                    classId?.let {
                        usersUtil.añadirClaseUsuario(
                            carnet = carnet,
                            nuevaClaseId = it,
                            onSuccess = {
                                usersUtil.obtenerClasesUsuario(
                                    carnet = carnet,
                                    onSuccess = { updatedClassDetails ->
                                        classes = updatedClassDetails
                                        showDialog = false
                                    },
                                    onFailure = { /* Manejar error */ }
                                )
                            },
                            onFailure = { /* Manejar error */ }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@Composable
fun AddClassScreen(firestore: FirebaseFirestore, carnet: String, onClassAdded: (String?) -> Unit) {
    var classCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ingrese el código de la clase",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                TextField(
                    value = classCode,
                    onValueChange = {
                        classCode = it.uppercase()
                        errorMessage = ""
                    },
                    label = { Text("Código de la clase") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                Button(
                    onClick = {
                        if (classCode.isNotBlank()) {
                            firestore.collection("clase").document(classCode).get()
                                .addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        firestore.collection("estudiantes").document(carnet)
                                            .update("clases", FieldValue.arrayUnion(classCode))
                                            .addOnSuccessListener {
                                                onClassAdded(classCode)
                                            }
                                            .addOnFailureListener {
                                                errorMessage = "Error al agregar la clase. Intente nuevamente."
                                            }
                                    } else {
                                        errorMessage = "La clase con el código $classCode no existe."
                                    }
                                }
                                .addOnFailureListener {
                                    errorMessage = "Error al buscar la clase. Intente nuevamente."
                                }
                        } else {
                            errorMessage = "Debe ingresar un código válido."
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Buscar Clase")
                }

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}