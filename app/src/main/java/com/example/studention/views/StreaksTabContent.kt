package com.example.studention.views

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.studention.ValidarUser
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun StreaksTabContent(navController: NavHostController) {
    val db = FirebaseFirestore.getInstance()
    var rankingList by remember { mutableStateOf<List<RankingItemData>>(emptyList()) }

    LaunchedEffect(Unit) {
        val validarUser = ValidarUser(navController.context)
        validarUser.obtenerEstudiantesPorRacha(
            onSuccess = { estudiantes: List<RankingItemData> ->
                rankingList = estudiantes.mapIndexed { index: Int, estudiante: RankingItemData ->
                    RankingItemData(
                        name = estudiante.name,
                        days = estudiante.days,
                        rank = index + 1
                    )
                }
            },
            onFailure = { e: Exception ->
                Log.w("TAG", "Error al obtener el ranking de estudiantes", e)
            }
        )
    }

    StreakView(navController = navController, rankingList = rankingList)
}

@Composable
fun StreakView(navController: NavHostController, rankingList: List<RankingItemData>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Roadmap de Rachas",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar el ranking como roadmap
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Usamos weight para que ocupe el espacio disponible
        ) {
            rankingList.forEachIndexed { index, rankingItem ->
                RoadmapItem(rankingItem = rankingItem, isLastItem = index == rankingList.size - 1)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Contenedor para centrar el botón en la parte inferior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = { navController.navigate("main") }) {
                Text(text = "Volver")
            }
        }
    }
}

@Composable
fun RoadmapItem(rankingItem: RankingItemData, isLastItem: Boolean) {
    val circleColor = when (rankingItem.rank) {
        1 -> Color(0xFFFFD700) // Oro
        2 -> Color(0xFFC0C0C0) // Plata
        3 -> Color(0xFFCD7F32) // Bronce
        else -> Color(0xFF4CAF50) // Color por defecto
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Indicador circular para cada rango
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(circleColor, CircleShape)
            ) {
                Text(
                    text = "${rankingItem.rank}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            if (!isLastItem) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(40.dp)
                        .background(Color.Gray)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Información del estudiante con barra de progreso y mensaje motivacional
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color(0xFFF5F5F5), MaterialTheme.shapes.medium)
                .padding(16.dp)
        ) {
            Text(
                text = rankingItem.name,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
            Text(
                text = "${rankingItem.days} días de racha",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Barra de progreso de racha
            LinearProgressIndicator(
                progress = (rankingItem.days / 30f).coerceIn(0f, 1f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(Color.LightGray),
            )

            // Mensaje motivacional
            Text(
                text = getMotivationalText(rankingItem.rank),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

// Función para generar un mensaje motivacional según el ranking
fun getMotivationalText(rank: Int): String {
    return when (rank) {
        1 -> "¡Eres el número uno! Sigue así."
        2 -> "Muy cerca del primer lugar. ¡No te rindas!"
        3 -> "¡Gran trabajo! Mantén el impulso."
        else -> "¡Sigue subiendo en el ranking!"
    }
}

data class RankingItemData(
    val name: String,
    val days: Int,
    val rank: Int
)