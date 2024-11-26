package com.example.studention.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationTabContent(navController: NavHostController, classId: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Verification") }
            )
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
            Text(text = "Verification for Class ID: $classId", style = MaterialTheme.typography.headlineMedium)
            // Add your verification content here
        }
    }
}