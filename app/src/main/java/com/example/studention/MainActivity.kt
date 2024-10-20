
package com.example.studention

import MainScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    // Controlador de navegación
    val navController = rememberNavController()

    // NavHost que gestiona las pantallas de la app
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // Pantalla de login
        composable("login") {
            Login(navController)
        }

        // Pantalla principal (con barra inferior)
        composable("main") {
            MainScreen(navController)
        }

        // Pantalla ButtonScreen
        composable("buttonScreen") {
            ButtonScreen(navController)
        }

        // Pantallas de retroalimentación positiva y negativa
        composable("positive") { PositiveScreen(navController) }
        composable("negative") { NegativeScreen(navController) }
    }
}
