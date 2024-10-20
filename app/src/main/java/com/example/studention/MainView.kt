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

import retrofit2.Call
import retrofit2.Callback



@Composable
fun MainScreen(navController: NavHostController) {
    // Estado para controlar qué pestaña está seleccionada
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTabIndex = selectedTab,
                onTabSelected = { index -> selectedTab = index }
            )
        }
    ) { innerPadding ->
        // Contenido que cambia según la pestaña seleccionada
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTab) {
                0 -> HomeTabContent()
                1 -> ClassesTabContent()
                2 -> ProfileTabContent()
            }
        }
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
fun ClassesTabContent() {


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Classes")
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
            onClick = { onTabSelected(1) },
            icon = { Icon(Icons.Default.List, contentDescription = "Classes") },
            label = { Text("Classes") },
            alwaysShowLabel = true
        )

        BottomNavigationItem(
            selected = selectedTabIndex == 2,
            onClick = { onTabSelected(2) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            alwaysShowLabel = true
        )
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






