package com.example.studention.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.studention.R

@Composable
fun MainScreen(navController: NavHostController, carnet: String) {
    var selectedTab by remember { mutableStateOf(1) }

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
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTab) {
                1 -> ClassesTabContent(navController, carnet)
                2 -> ProfileTabContent(navController, carnet)
                3 -> StreaksTabContent(navController, carnet)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    backgroundColor: Color = Color(0xFF6200EE) // Personalizable
) {
    Surface(
        color = backgroundColor,
        contentColor = Color.White,
        modifier = Modifier.height(56.dp) // Altura de la barra inferior
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavigationItem(
                selected = selectedTabIndex == 1,
                onClick = { onTabSelected(1) },
                icon = { Icon(Icons.Filled.List, contentDescription = null) },
                label = { Text("Clases") }
            )
            BottomNavigationItem(
                selected = selectedTabIndex == 2,
                onClick = { onTabSelected(2) },
                icon = { Icon(Icons.Filled.Person, contentDescription = null) },
                label = { Text("Perfil") }
            )
            BottomNavigationItem(
                selected = selectedTabIndex == 3,
                onClick = { onTabSelected(3) },
                icon = { Icon(Icons.Filled.Star, contentDescription = null) },
                label = { Text("Racha") }
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
        shape = MaterialTheme.shapes.medium, // Forma del ítem
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