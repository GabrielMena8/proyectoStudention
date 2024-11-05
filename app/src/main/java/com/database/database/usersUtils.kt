package com.database.database

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class UsersUtil {
    private val db = FirebaseFirestore.getInstance()

    fun obtenerTodosEstudiantes(onSuccess: (List<Map<String, Any>>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("estudiantes")
            .get()
            .addOnSuccessListener { result ->
                val estudiantes = result.map { it.data }
                onSuccess(estudiantes)
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error al obtener los documentos.", exception)
                onFailure(exception)
            }
    }


    fun obtenerEstudiantePorId(id: String, onSuccess: (Map<String, Any>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("estudiantes")
            .document(id)
            .get()
            .addOnSuccessListener { result ->
                result.data?.let { onSuccess(it) }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error al obtener el documento.", exception)
                onFailure(exception)
            }
    }

    fun obtenerEstudiantePorCorreo(correo: String, onSuccess: (Map<String, Any>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("estudiantes")
            .whereEqualTo("correo", correo)
            .get()
            .addOnSuccessListener { result ->
                val estudiante = result.documents.firstOrNull()?.data
                estudiante?.let { onSuccess(it) }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error al obtener el documento.", exception)
                onFailure(exception)
            }
    }

    fun obtenerEstudiantePorNombre(nombre: String, onSuccess: (List<Map<String, Any>>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("estudiantes")
            .whereEqualTo("nombre", nombre)
            .get()
            .addOnSuccessListener { result ->
                val estudiantes = result.map { it.data }
                onSuccess(estudiantes)
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error al obtener los documentos.", exception)
                onFailure(exception)
            }
    }






    fun obtenerEstudiantePorRacha(onSuccess: (List<Map<String, Any>>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("estudiantes")
            .orderBy("racha")
            .get()
            .addOnSuccessListener { result ->
                val estudiantes = result.map { document ->
                    mapOf(
                        "nombre" to (document.getString("nombre") as Any),
                        "racha" to (document.getLong("racha") as Any)
                    )
                }
                onSuccess(estudiantes)
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error al obtener los documentos.", exception)
                onFailure(exception)
            }
    }



    fun obtenerTodasLasClases(onSuccess: (List<Map<String, Any>>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("clase")
            .get()
            .addOnSuccessListener { result ->
                val clases = result.map { document ->
                    val data = document.data
                    val id = document.id // Obtener el ID del documento
                    val materia = data["materia"] ?: "Sin materia" // Verificar que "materia" no sea nulo
                    data + mapOf("id" to id, "materia" to materia) // Combinar los datos del documento con su ID y materia
                }
                onSuccess(clases)
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error al obtener los documentos.", exception)
                onFailure(exception)
            }
    }

    fun obtenerTodosProfesores(onSuccess: (List<Map<String, Any>>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("profesor")
            .get()
            .addOnSuccessListener { result ->
                val profesores = result.map { document ->
                    val data = document.data
                    val id = document.id // Obtener el ID del documento
                    val nombre = data["nombre"] ?: "Sin nombre"
                    val correo = data["correo"] ?: "Sin correo"
                    val ratings = data["ratings"] ?: emptyList<Any>()
                    val clases = data["clases"] ?: emptyList<Any>()
                    data + mapOf(
                        "id" to id,
                        "nombre" to nombre,
                        "correo" to correo,
                        "ratings" to ratings,
                        "clases" to clases
                    )
                }
                onSuccess(profesores)
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error al obtener los documentos.", exception)
                onFailure(exception)
            }
    }

// Id ejemplo Fbtsp041

    fun obtenerClasePorId(id: String, onSuccess: (Map<String, Any>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("clase")
            .document(id)
            .get()
            .addOnSuccessListener { result ->
                result.data?.let { onSuccess(it) }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error al obtener el documento.", exception)
                onFailure(exception)
            }
    }

    fun obtenerClasePorNombre(nombre: String, onSuccess: (List<Map<String, Any>>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("clase")
            .whereEqualTo("nombre", nombre)
            .get()
            .addOnSuccessListener { result ->
                val clases = result.map { it.data }
                onSuccess(clases)
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error al obtener los documentos.", exception)
                onFailure(exception)
            }
    }


}


