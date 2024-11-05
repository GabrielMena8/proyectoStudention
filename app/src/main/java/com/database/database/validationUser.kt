package com.example.studention
import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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