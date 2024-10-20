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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlin.random.Random
import androidx.compose.material3.ButtonDefaults
import androidx.navigation.NavGraph.Companion.findStartDestination

import retrofit2.Call
import retrofit2.Callback



@Composable
fun MainScreen(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTab) {
                0 -> HomeTabContent()
                1 -> {
                    // Navega a ButtonScreen y limpia la pila de navegación
                    navController.navigate("buttonScreen") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true // Esto evita crear múltiples instancias
                        restoreState = true // Restaura el estado anterior
                    }
                }
                2 -> ProfileTabContent()
            }
        }

        BottomNavigationBar(
            selectedTabIndex = selectedTab,
            onTabSelected = { index ->
                selectedTab = index
                if (index == 1) {
                    navController.navigate("buttonScreen") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true // Esto evita crear múltiples instancias
                        restoreState = true // Restaura el estado anterior
                    }
                }
            }
        )
    }
}

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
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botón para "Rate your Class!"
        Button(
            onClick = { navController.navigate("buttonScreen") }, // Navega a ButtonScreen
            modifier = Modifier.padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue) // Color del botón
        ) {
            Text("Rate your Class!", color = Color.White)
        }
    }
}



@Composable
fun HomeTabContent() {

    var code by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(onClick = {

            code = generateCode()


        })

        {
            Text("Generate Code")



        }

        Text(text = "" +
                "Code: $code")
    }
}

@Composable
fun BottomNavigationBar(selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Button(
            onClick = { onTabSelected(0) },
            colors = ButtonDefaults.buttonColors(containerColor = if (selectedTabIndex == 0) Color.Blue else Color.Gray)
        ) {
            Text("Home", color = Color.White)
        }

        Button(
            onClick = { onTabSelected(1) },
            colors = ButtonDefaults.buttonColors(containerColor = if (selectedTabIndex == 1) Color.Blue else Color.Gray)
        ) {
            Text("Rate your Class!", color = Color.White)
        }

        Button(
            onClick = { onTabSelected(2) },
            colors = ButtonDefaults.buttonColors(containerColor = if (selectedTabIndex == 2) Color.Blue else Color.Gray)
        ) {
            Text("Profile", color = Color.White)
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
        modifier = Modifier.fillMaxWidth()
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

fun generateCode(): String {
    println("Code generated")
    asyncLlamada()

    return Random(1000).nextInt().toString();
}


fun asyncLlamada() {



    //Simular llamada a una API
    val call = object : Callback<String> {
        override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
            println("Response received")
        }

        override fun onFailure(call: Call<String>, t: Throwable) {
            println("Error received")
        }
    }
}






