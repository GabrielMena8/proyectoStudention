
package com.example.studention

import MainScreen

import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager


import com.database.database.UsersUtil;

import android.os.Build
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit
import com.google.firebase.messaging.FirebaseMessaging







class MainActivity : ComponentActivity() {
    private lateinit var validarUser: ValidarUser

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        val userUtils = UsersUtil()
       userUtils.obtenerEstudiantePorRacha(
            onSuccess = { estudiante ->
                Log.d("TAG", "Estudiante con racha: $estudiante")
            },
            onFailure = { e ->
                Log.w("TAG", "Error al obtener el estudiante", e)
            }
        )

        validarUser = ValidarUser(this)
        //validarUser.agregarDatos("Andres","29883404","A1-202",4.5,"Pilar Cuencas")
        validarUser.obtenerCorreoUsuarioLogueado(
            onSuccess = { correo ->
                Log.d("TAG", "Correo del usuario logueado: $correo")
            },
            onFailure = { e ->
                Log.w("TAG", "Error al obtener el correo", e)
            }
        )


        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Obtener el token de FCM
            val token = task.result
            Log.d(TAG, "FCM Token: $token")
            // Envíalo a tu servidor si es necesario
        }

        setContent {
            MyApp()
        }

        createNotificationChannel()
        scheduleDailyReminder()

    }
    companion object {
        private const val TAG = "MainActivity"
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default_channel_id",
                "Default Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    //Notificaciones

   fun scheduleDailyReminder() {
        val reminderRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(
            24, TimeUnit.HOURS
        ).setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "dailyReminder",
            ExistingPeriodicWorkPolicy.REPLACE,
            reminderRequest
        )
    }

    //Debug notification
    public fun sendDebugNotification() {
        val notification = NotificationCompat.Builder(this, "DEBUG_CHANNEL")

            .setContentTitle("Notificación de Prueba")
            .setContentText("¡Esta es una notificación enviada en modo debug!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        NotificationManagerCompat.from(this).notify(1002, notification)
    }

    private fun calculateInitialDelay(): Long {
        // Programa la notificación para las 9:00 AM
        val currentTime = System.currentTimeMillis()
        val calendar = Calendar.getInstance().apply {
            if (get(Calendar.HOUR_OF_DAY) >= 9) add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        return calendar.timeInMillis - currentTime
    }


}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val validarUser = remember { ValidarUser(context) }
    var carnet by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        validarUser.obtenerCarnetUsuarioActual(
            onSuccess = { carnetValue ->
                carnet = carnetValue
                isLoading = false
            },
            onFailure = { e ->
                Log.w("TAG", "Error al obtener el carnet", e)
                isLoading = false
            }
        )
    }

    if (isLoading) {
        // Show a loading screen or some placeholder while carnet is being fetched
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        NavHost(
            navController = navController,
            startDestination = if (carnet != null) "main" else "welcome"
        ) {
            composable("welcome") {
                WelcomeScreen(
                    onLoginClick = { navController.navigate("login") },
                    onRegisterClick = { navController.navigate("register") }
                )
            }
            composable("login") {
                LoginScreen(navController)
            }
            composable("register") {
                RegisterScreen(navController)
            }
            composable("main") {
                MainScreen(navController)
            }
            composable("streaks") {
                StreaksTabContent(navController)
            }
            composable("buttonScreen") {
                ButtonScreen(navController)
            }
            composable("positive") {
                PositiveScreen(navController, carnet!!, validarUser)
            }
            composable("negative") {
                NegativeScreen(navController, carnet!!, validarUser)
            }
        }
    }
}







