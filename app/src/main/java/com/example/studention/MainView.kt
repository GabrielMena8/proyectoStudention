import android.util.Log
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlin.random.Random
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.painterResource
import com.example.studention.R
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.example.studention.RetrofitInstance
import com.example.studention.showDailyReminder
import kotlinx.coroutines.GlobalScope


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable

fun MainScreen(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf(0) }
    var showPasswordDialog by remember { mutableStateOf(false) }

    // Definimos los colores para cada pestaña
    val backgroundColor = when (selectedTab) {
        0 -> Color(0xFFE3F2FD) // Azul claro para Home
        1 -> Color(0xFFFCE4EC) // Rosa para Classes
        2 -> Color(0xFFE8F5E9) // Verde claro para Profile
        3 -> Color(0xFFFFF3E0) // Naranja claro para Streaks
        else -> Color.White
    }

    Scaffold(
        containerColor = backgroundColor, // Cambiar el fondo dinámicamente
        bottomBar = {
            BottomNavigationBar(
                selectedTabIndex = selectedTab,
                onTabSelected = { index ->
                    selectedTab = index
                    if (index == 1) showPasswordDialog = true
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
                0 -> HomeTabContent()
                2 -> ProfileTabContent()
                3 -> StreaksTabContent(navController)
            }
        }

        if (showPasswordDialog) {
            PasswordDialog(
                onDismiss = { showPasswordDialog = false },
                onPasswordCorrect = {
                    showPasswordDialog = false
                    navController.navigate("buttonScreen")
                }
            )
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
fun ProfileTabContent() {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Profile")
    }
}

@Composable
fun ClassesTabContent(navController: NavHostController) {
    var isButtonEnabled by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botón para "Rate your Class!" bloqueado hasta que se introduzca la contraseña
        Button(
            onClick = { showPasswordDialog = true },
            modifier = Modifier.padding(16.dp),
            enabled = isButtonEnabled,
            colors = ButtonDefaults.buttonColors(containerColor = if (isButtonEnabled) Color.Blue else Color.Gray)
        ) {
            Text("Rate your Class!", color = Color.White)
        }

        // Mostrar el diálogo para introducir la contraseña
        if (showPasswordDialog) {
            PasswordDialog(
                onDismiss = { showPasswordDialog = false },
                onPasswordCorrect = { isButtonEnabled = true }
            )
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





/*@Composable
    fun HomeTabContent() {
        val scope = rememberCoroutineScope()
        var code by remember { mutableStateOf("") } // Código recibido de la API
        var showPopup by remember { mutableStateOf(false) } // Estado del popup
        val context = LocalContext.current

        val classes = listOf(
            ClassData("Matemáticas", "Clase de álgebra", "10:00 AM - 11:00 AM"),
            ClassData("Historia", "Historia Universal", "12:00 PM - 1:00 PM"),
            ClassData("Ciencias", "Laboratorio de química", "2:00 PM - 3:00 PM"),
            ClassData("Inglés", "Clase de gramática", "4:00 PM - 5:00 PM")
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            items(classes) { classItem ->
                ClassCard(
                    title = classItem.title,
                    description = classItem.description,
                    time = classItem.time,
                    onClick = { /* Acción al hacer clic en una clase */ }
                )
            }




        }

        Button(onClick = {
            scope.launch {
                try {
                    val response = RetrofitInstance.api.getVotes()
                    response.forEach {
                        Log.d("API_RESPONSE", "Datos: ${it.id} - ${it.code}")
                        correctPassword = it.code.toString()
                    }
                    code = correctPassword// Asigna el valor del código
                    showPopup = true // Muestra el popup al recibir los datos
                } catch (e: Exception) {
                    Log.e("API_ERROR", "Error al obtener los datos: ${e.message}")
                }
            }
        }) {
            Text("Generate Request")
        }

        // Mostrar el popup si el estado es verdadero
        if (showPopup) {
            popUpCode(code) { showPopup = false }
        }
    }*/



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
                label = { Text("Profile") }
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
)