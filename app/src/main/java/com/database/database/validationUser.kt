package com.example.studention
import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar
import java.util.Date

class ValidarUser(context: Context) {
    private val auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    init {
        FirebaseApp.initializeApp(context)
        auth = FirebaseAuth.getInstance()
    }

    fun obtenerCorreoUsuarioLogueado(onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val user = auth.currentUser
        if (user != null) {
            onSuccess(user.email ?: "Correo no disponible")
        } else {
            onFailure(Exception("No hay usuario logueado"))
        }
    }

    fun obtenerCarnetUsuarioActual(onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val user = auth.currentUser
        if (user != null) {
            val email = user.email
            if (email != null) {
                db.collection("estudiantes")
                    .whereEqualTo("correo", email)
                    .get()
                    .addOnSuccessListener { result ->
                        val document = result.documents.firstOrNull()
                        if (document != null) {
                            val carnet = document.getString("carnet")
                            if (carnet != null) {
                                onSuccess(carnet)
                            } else {
                                onFailure(Exception("Carnet not found"))
                            }
                        } else {
                            onFailure(Exception("No student found with this email"))
                        }
                    }
                    .addOnFailureListener { e ->
                        onFailure(e)
                    }
            } else {
                onFailure(Exception("Email not available"))
            }
        } else {
            onFailure(Exception("No user logged in"))
        }
    }

    fun modificarRacha(carnet: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val userRef = db.collection("estudiantes").document(carnet)
        userRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val ultimaActualizacion = document.getDate("ultimaActualizacion")
                    val rachaActual = document.getLong("racha")?.toInt() ?: 0

                    val hoy = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }.time

                    if (ultimaActualizacion == null || ultimaActualizacion.before(hoy)) {
                        val nuevaRacha = rachaActual + 1
                        userRef.update(mapOf(
                            "racha" to nuevaRacha,
                            "ultimaActualizacion" to hoy
                        ))
                            .addOnSuccessListener {
                                Log.d("TAG", "Racha actualizada correctamente")
                                onSuccess()
                            }
                            .addOnFailureListener { e ->
                                Log.w("TAG", "Error al actualizar la racha", e)
                                onFailure(e)
                            }
                    } else {
                        Log.d("TAG", "La racha ya fue actualizada hoy")
                        onSuccess()
                    }
                } else {
                    onFailure(Exception("No existe usuario con este carnet"))
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun obtenerEstudiantesPorRacha(onSuccess: (List<RankingItemData>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("estudiantes")
            .get()
            .addOnSuccessListener { result ->
                val estudiantes = result.documents.mapNotNull { document ->
                    val nombre = document.getString("nombre")
                    val racha = document.getLong("racha")?.toInt()
                    if (nombre != null && racha != null) {
                        RankingItemData(name = nombre, days = racha, rank = 0)
                    } else {
                        null
                    }
                }.sortedByDescending { it.days }

                onSuccess(estudiantes)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun agregarDatos(nombre: String, cedula: String, aula: String, rating: Double, profesor: String) {
        val datos = hashMapOf(
            "nombre" to nombre,
            "cedula" to cedula,
            "aula" to aula,
            "rating" to rating,
            "profesor" to profesor
        )

        db.collection("estudiantes")
            .add(datos)
            .addOnSuccessListener { documentReference ->
                Log.d("TAG", "Documento agregado con ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error al agregar el documento", e)
            }
    }

    fun agregarDatosRegister(nombre: String, carnet: String, correo: String, password: String, racha: Int, clases: List<String>) {
        val datos = hashMapOf(
            "nombre" to nombre,
            "carnet" to carnet,
            "correo" to correo,
            "password" to password,
            "racha" to racha,
            "clases" to clases
        )

        db.collection("estudiantes")
            .document(carnet)
            .set(datos)
            .addOnSuccessListener {
                Log.d("TAG", "Documento agregado con ID: $carnet")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error al agregar el documento", e)
            }
    }

    fun obtenerDatosCarnet(carnet: String, onSuccess: (User) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("estudiantes").document(carnet).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = User(
                        carnet = carnet,
                        password = document.getString("password") ?: ""
                    )
                    onSuccess(user)
                } else {
                    onFailure(Exception("No existe usuario con este carnet"))
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
}

data class User(
    val carnet: String,
    val password: String
)