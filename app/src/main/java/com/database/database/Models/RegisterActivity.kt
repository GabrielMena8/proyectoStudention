package com.database.database.Models

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studention.R
import com.example.studention.ValidarUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }
    val firestore = remember { FirebaseFirestore.getInstance() }
    val validarUser = remember { ValidarUser(context) }

    var fullName by remember { mutableStateOf("") }
    var carnet by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullNameError by remember { mutableStateOf(false) }
    var carnetError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    val isFullNameValid: (String) -> Boolean = { it.isNotEmpty() }
    val isCarnetValid: (String) -> Boolean = { it.all { char -> char.isDigit() } }
    val isEmailValid: (String) -> Boolean = { it.matches(Regex("^[^@]+@[^@]+\\.[a-zA-Z0-9]+$"))}
    val isPasswordValid: (String) -> Boolean = { it.length >= 6 && it.any { char -> char.isUpperCase() } && it.any { char -> char.isDigit() } }

    fun registerUser(fullName: String, carnet: String, email: String, password: String) {
        firestore.collection("estudiantes").document(carnet).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Toast.makeText(context, "Este numero de carnet ya tiene una cuenta asociada", Toast.LENGTH_SHORT).show()
                } else {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                validarUser.agregarDatosRegister(fullName, carnet, email, password, 0, emptyList())
                                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                navController.navigate("main") {
                                    popUpTo("register") { inclusive = true }
                                }
                            } else {
                                Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                TextField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                        fullNameError = !isFullNameValid(it)
                    },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = fullNameError,
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Blue,
                        unfocusedIndicatorColor = Color.Gray,
                        errorIndicatorColor = Color.Red
                    )
                )
            }

            if (fullNameError) {
                Text(
                    text = "Full Name is required.",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                TextField(
                    value = carnet,
                    onValueChange = {
                        carnet = it
                        carnetError = !isCarnetValid(it)
                    },
                    label = { Text("Carnet") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = carnetError,
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Blue,
                        unfocusedIndicatorColor = Color.Gray,
                        errorIndicatorColor = Color.Red
                    )
                )
            }

            if (carnetError) {
                Text(
                    text = "Carnet must contain only numbers.",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                TextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = !isEmailValid(it)
                    },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = emailError,
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Blue,
                        unfocusedIndicatorColor = Color.Gray,
                        errorIndicatorColor = Color.Red
                    )
                )
            }

            if (emailError) {
                Text(
                    text = "Email must be a valid address.",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                TextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = !isPasswordValid(it)
                    },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    isError = passwordError,
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Blue,
                        unfocusedIndicatorColor = Color.Gray,
                        errorIndicatorColor = Color.Red
                    )
                )
            }

            if (passwordError) {
                Text(
                    text = "Password must be at least 6 characters, contain an uppercase letter and a number.",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (isFullNameValid(fullName) && isCarnetValid(carnet) && isEmailValid(email) && isPasswordValid(password)) {
                        registerUser(fullName, carnet, email, password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text("Register", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("¿Ya tienes una cuenta?", color = Color.White)
            }
        }
    }
}