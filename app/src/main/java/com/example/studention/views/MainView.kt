package com.example.studention.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface





import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.painterResource
import com.example.studention.R
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.LaunchedEffect

@Composable
fun MainScreen(navController: NavHostController) {
    var carnet by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) } // Para mostrar un indicador de carga mientras se obtiene el carnet


    // Recuperar el carnet de Firebase
    LaunchedEffect(Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    carnet = document.getString("carnet") ?: "Desconocido"
                    isLoading = false
                }
                .addOnFailureListener {
                    carnet = "Error"
                    isLoading = false
                }
        } else {
            carnet = "No autenticado"
            isLoading = false
        }
    }

    if (isLoading) {
        // Mostrar un indicador de carga mientras se obtiene el carnet
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        // Mostrar la interfaz principal una vez que se obtiene el carnet
        MainScreenContent(navController = navController, carnet = carnet)
    }
}

@Composable
fun MainScreenContent(navController: NavHostController, carnet: String) {
    var selectedTab by remember { mutableStateOf(0) }

    val backgroundColor = when (selectedTab) {
        0 -> Color(0xFFE3F2FD)
        1 -> Color(0xFFFCE4EC)
        2 -> Color(0xFFE8F5E9)
        3 -> Color(0xFFFFF3E0)
        else -> Color.White
    }

    Scaffold(
        containerColor = backgroundColor,
        bottomBar = {
            BottomNavigationBar(
                selectedTabIndex = selectedTab,
                onTabSelected = { index ->
                    selectedTab = index
                    if (index == 1) navController.navigate("buttonScreen")
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTab) {
                0 -> HomeTabContent(navController)
                2 -> ProfileTabContent(carnet)
                3 -> StreaksTabContent(navController)
            }
        }
    }
}


@Composable
fun ClassCard(
    title: String,
    description: String,
    time: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = time,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_silver_medal),
                    contentDescription = "Time",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Rate this Class", color = Color.White)
            }
        }
    }
}


//Variables
var streakDays : Int = 0
var correctPassword = ""



//Boton para debug de notificaciones




@Composable
fun ClassesTabContent(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botón para "Rate your Class!" habilitado directamente
        Button(
            onClick = { navController.navigate("buttonScreen") },
            modifier = Modifier.padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text("Rate your Class!", color = Color.White)
        }
    }
}

@Composable
fun PasswordDialog(onDismiss: () -> Unit, onPasswordCorrect: () -> Unit) {
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }  // Para controlar si se muestra el error


    AlertDialog(
        onDismissRequest = onDismiss, // Al presionar fuera, se ejecuta el onDismiss
        title = { Text(text = "Enter Password") },
        text = {
            Column {
                Text(text = "Please enter the password to enable the button:")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation()
                )
                // Mostrar mensaje de error si la contraseña es incorrecta
                if (showError) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Incorrect password, please try again.",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (password == correctPassword) {
                        onPasswordCorrect() // Contraseña correcta, habilitar botón
                    } else {
                        showError = true // Mostrar error si es incorrecta
                    }
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}





@Composable
fun HomeTabContent(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bienvenido a la pestaña de inicio",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Acción para cerrar sesión
                FirebaseAuth.getInstance().signOut()
                navController.navigate("welcome") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Cerrar Sesión")
        }
    }
}



@Composable
    fun popUpCode(code: String, onDismiss: () -> Unit) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Código de la API") },
            text = { Text("El código de la API es: $code") },
            confirmButton = {
                Button(onClick = onDismiss) {
                    Text("OK")
                }
            }
        )
    }





// Clase de datos para las clases
data class ClassData(
    val title: String,
    val description: String,
    val time: String
)




@Composable
fun BottomNavigationBar(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    backgroundColor: Color = Color(0xFF6200EE) // Personalizable
) {
    Surface(
        color = backgroundColor,
        contentColor = Color.White,
        modifier = Modifier.height(56.dp) // Grosor reducido
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavigationItem(
                selected = selectedTabIndex == 0,
                onClick = { onTabSelected(0) },
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home") }
            )

            BottomNavigationItem(
                selected = selectedTabIndex == 1,
                onClick = { onTabSelected(1) },
                icon = { Icon(Icons.Default.List, contentDescription = "Classes") },
                label = { Text("Rate") }
            )

            BottomNavigationItem(
                selected = selectedTabIndex == 2,
                onClick = { onTabSelected(2) },
                icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Classes") }
            )

            BottomNavigationItem(
                selected = selectedTabIndex == 3,
                onClick = { onTabSelected(3) },
                icon = { Icon(Icons.Default.Star, contentDescription = "Streaks") },
                label = { Text("Streaks") }
            )
        }
    }
}

@Composable
fun BottomNavigationItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit
) {
    val backgroundColor = if (selected) Color(0xFF3700B3) else Color.Transparent // Color para ítems seleccionados
    val contentColor = if (selected) Color.White else Color.Gray

    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        shape = MaterialTheme.shapes.medium, // Borde opcional
        modifier = Modifier
            .padding(4.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            icon()
            label()
        }
    }
}








/*
@Composable
fun StreaksTabContent(navController: NavHostController) {
    // Datos de prueba para la racha
    val rankingList = listOf(
        RankingItemData("John Doe", 30, 1),
        RankingItemData("Pedro Pérez", 26, 2),
        RankingItemData("Jane Doe", 24, 3)
    )

    // Llamada pasando los valores

    StreakView(
        navController = navController,
        streakDays = streakDays,
        rankingList = rankingList
    )
}

@Composable
fun StreakView(
    navController: NavHostController,
    streakDays: Int,
    rankingList: List<RankingItemData>
) {
    // Calculo del rango actual y del siguiente basado en los días de racha
    val (currentRank, nextRank, daysForNextRank) = calculateRanks(streakDays)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Días de Racha",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black
        )

        // Sección de progreso de racha
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$streakDays DÍAS EN RACHA",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFFFA500)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Rango Actual", style = MaterialTheme.typography.bodyLarge)
                    // Icono de rango actual
                    Icon(
                        painter = painterResource(id = when (currentRank) {
                            "Bronce" -> R.drawable.ic_bronze_medal
                            "Plata" -> R.drawable.ic_silver_medal
                            "Oro" -> R.drawable.ic_gold_medal
                            else -> R.drawable.ic_bronze_medal
                        }),
                        contentDescription = "Rango $currentRank",
                        tint = Color.Unspecified,
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Próximo Rango", style = MaterialTheme.typography.bodyLarge)
                    // Icono del próximo rango
                    Icon(
                        painter = painterResource(id = when (nextRank) {
                            "Bronce" -> R.drawable.ic_bronze_medal
                            "Plata" -> R.drawable.ic_silver_medal
                            "Oro" -> R.drawable.ic_gold_medal
                            else -> R.drawable.ic_gold_medal // Default
                        }),
                        contentDescription = "Rango $nextRank",
                        tint = Color.Unspecified,
                    )
                }
            }

            Text(
                text = "$daysForNextRank días más para conseguir la insignia de $nextRank",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Liga y recompensas
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = "Liga y Recompensas",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )

            Text(
                text = "¡Los tres primeros estudiantes con más puntuación podrán ganar diferentes recompensas!",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Ranking actual
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = "Ranking Actual",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )

            rankingList.forEach { rankingItem ->
                RankingItem(name = rankingItem.name, days = rankingItem.days, rank = rankingItem.rank)
            }
        }

        Button(onClick = { navController.navigate("main") }) {
            Text(text = "Volver")
        }
    }
}

// Función para calcular el rango actual, el próximo rango y los días restantes para el próximo rango
fun calculateRanks(streakDays: Int): Triple<String, String, Int> {
    val currentRank = when {
        streakDays < 15 -> "Bronce"
        streakDays < 30 -> "Plata"
        else -> "Oro"
    }

    val nextRank = when (currentRank) {
        "Bronce" -> "Plata"
        "Plata" -> "Oro"
        else -> "Oro" // Si ya es oro, no hay mas rangos
    }

    val daysForNextRank = when (currentRank) {
        "Bronce" -> 15 - streakDays
        "Plata" -> 30 - streakDays
        else -> 0 // Ya es oro, no se requieren más días
    }

    return Triple(currentRank, nextRank, daysForNextRank)
}

@Composable
fun RankingItem(name: String, days: Int, rank: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$rank. $name — $days días",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )
        // Icono de medalla segun el rango
        Icon(
            painter = painterResource(id = getRankIconByPosition(rank)),
            contentDescription = "Rango $rank",
            modifier = Modifier.size(24.dp),
            tint = Color.Unspecified
        )
    }
}

fun getRankIcon(rank: String): Int {
    return when (rank) {
        "Bronce" -> R.drawable.ic_bronze_medal
        "Plata" -> R.drawable.ic_silver_medal
        "Oro" -> R.drawable.ic_gold_medal
        else -> R.drawable.ic_bronze_medal
    }
}

fun getRankIconByPosition(rank: Int): Int {
    return when (rank) {
        1 -> R.drawable.ic_gold_medal
        2 -> R.drawable.ic_silver_medal
        else -> R.drawable.ic_bronze_medal
    }
}

data class RankingItemData(
    val name: String,
    val days: Int,
    val rank: Int
)*/