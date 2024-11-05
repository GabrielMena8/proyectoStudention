
package com.example.studention

import MainScreen


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import StreaksTabContent
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager

import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit







class MainActivity : ComponentActivity() {
    private lateinit var validarUser: ValidarUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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


        setContent {
            MyApp()
        }

        createNotificationChannel()
        scheduleDailyReminder()



    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Studention"
            val descriptionText = "Studention notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("studention", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
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

    NavHost(
        navController = navController,
        startDestination = "welcome"
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
            RegisterScreen(
                navController = navController
            )
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
            PositiveScreen(navController)
        }
        composable("negative") {
            NegativeScreen(navController)
        }
    }
}






