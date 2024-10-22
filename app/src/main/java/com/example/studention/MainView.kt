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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.studention.RetrofitInstance
import com.example.studention.showDailyReminder
import kotlinx.coroutines.GlobalScope


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun MainScreen(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf(0) }
    var showPasswordDialog by remember { mutableStateOf(false) } // Variable para mostrar el diálogo de contraseña


    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTabIndex = selectedTab,
                onTabSelected = { index ->
                    selectedTab = index
                    if (index == 1) { // Si selecciona la pestaña de "Classes"
                        showPasswordDialog = true // Muestra el diálogo de contraseña
                    }
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
                //1 -> ClassesTabContent(navController) // Eliminamos esto ya que gestionamos desde el dialogo
                2 -> ProfileTabContent()
                3 -> StreaksTabContent(navController)
            }
        }

        // Mostrar el diálogo de verificación de contraseña si es necesario
        if (showPasswordDialog) {
            PasswordDialog(
                onDismiss = { showPasswordDialog = false }, // Si se cierra el diálogo, volvemos a la vista principal
                onPasswordCorrect = {
                    showPasswordDialog = false
                    navController.navigate("buttonScreen") // Navegar a buttonScreen solo si la contraseña es correcta
                }
            )
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



@Composable
fun HomeTabContent() {
    val scope = rememberCoroutineScope()
    var code by remember { mutableStateOf("") }
    val context = LocalContext.current
    val voteId = 0;

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ClassCard(
            title = "Clase de Matemáticas",
            description = "Clase de matemáticas para el día de hoy",
            time = "10:00 AM - 11:00 AM",
            onClick = {

                NavHostController(context).navigate("buttonScreen")
            }
        )


        Button(onClick = {
            scope.launch {
                try {
                    // Llamada a la API aquí
                    val response = RetrofitInstance.api.getVotes()// Asegúrate de tener este método en tu servicio

                    response.forEach {
                        Log.d("API_RESPONSE", "Datos: ${it.id} - ${it.code} - ${it.boton1} - ${it.boton2}") // Imprimir la respuesta en Logcat
                        correctPassword = it.code.toString()
                    }

                    code = response.toString() // Asignar el valor de la respuesta a la variable code
                    Log.d("API_RESPONSE", "Datos: ${response.toString()}") // Imprimir la respuesta en Logcat

                } catch (e: Exception) {
                    Log.e("API_ERROR", "Error al obtener los datos: ${e.message}") // Manejo de errores
                }
            }

        }
        )
        {
            Text("Generate Request")



        }

        Text(text = "Código: $code")






    }
}






@Composable
fun BottomNavigationBar(selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.Black
    ) {
        BottomNavigationItem(
            selected = selectedTabIndex == 0,
            onClick = { onTabSelected(0) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            alwaysShowLabel = true
        )

        BottomNavigationItem(
            selected = selectedTabIndex == 1,
            onClick = { onTabSelected(1) }, // Cambiar navegación directamente a buttonScreen
            icon = { Icon(Icons.Default.List, contentDescription = "Classes") },
            label = { Text("Rate your Class!") },
            alwaysShowLabel = true
        )

        BottomNavigationItem(
            selected = selectedTabIndex == 2,
            onClick = { onTabSelected(2) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            alwaysShowLabel = true
        )

        BottomNavigationItem(
            selected = selectedTabIndex == 3,
            onClick = { onTabSelected(3) },
            icon = { Icon(Icons.Default.Star, contentDescription = "Streaks") },
            label = { Text("Rachas") },
            alwaysShowLabel = true
        )
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
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.headlineSmall)
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
            Text(text = time, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun BottomNavigation(
    backgroundColor: Color,
    contentColor: Color,
    content: @Composable () -> Unit
) {
    Surface(
        color = backgroundColor,
        contentColor = contentColor
    ) {
        Column {
            content()
        }
    }
}

@Composable
fun BottomNavigationItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    alwaysShowLabel: Boolean
) {
    val color = if (selected) Color.Blue else Color.Gray
    val contentColor = if (selected) Color.White else Color.Black

    Surface(
        color = color,
        contentColor = contentColor,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            icon()
            if (alwaysShowLabel || selected) {
                label()
            }
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