package com.database.database

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class UsersUtil {
    private val db = FirebaseFirestore.getInstance() // Initialize Firestore instance

    // Function to get all students from the "estudiantes" collection
    fun obtenerTodosEstudiantes(onSuccess: (List<Map<String, Any>>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("estudiantes")
            .get()
            .addOnSuccessListener { result ->
                val estudiantes = result.map { it.data } // Map each document to its data
                onSuccess(estudiantes) // Call onSuccess with the list of students
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error al obtener los documentos.", exception) // Log error
                onFailure(exception) // Call onFailure with the exception
            }
    }

    // Function to get a student by ID from the "estudiantes" collection
    fun obtenerEstudiantePorId(id: String, onSuccess: (Map<String, Any>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("estudiantes")
            .document(id)
            .get()
            .addOnSuccessListener { result ->
                result.data?.let { onSuccess(it) } // Call onSuccess with the student data if it exists
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error al obtener el documento.", exception) // Log error
                onFailure(exception) // Call onFailure with the exception
            }
    }

    // Function to get a student by email from the "estudiantes" collection
    fun obtenerEstudiantePorCorreo(correo: String, onSuccess: (Map<String, Any>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("estudiantes")
            .whereEqualTo("correo", correo)
            .get()
            .addOnSuccessListener { result ->
                val estudiante = result.documents.firstOrNull()?.data // Get the first matching document
                estudiante?.let { onSuccess(it) } // Call onSuccess with the student data if it exists
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error al obtener el documento.", exception) // Log error
                onFailure(exception) // Call onFailure with the exception
            }
    }

    // Function to get students by name from the "estudiantes" collection
    fun obtenerEstudiantePorNombre(nombre: String, onSuccess: (List<Map<String, Any>>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("estudiantes")
            .whereEqualTo("nombre", nombre)
            .get()
            .addOnSuccessListener { result ->
                val estudiantes = result.map { it.data } // Map each document to its data
                onSuccess(estudiantes) // Call onSuccess with the list of students
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error al obtener los documentos.", exception) // Log error
                onFailure(exception) // Call onFailure with the exception
            }
    }

    // Function to get students ordered by streak from the "estudiantes" collection
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
                onSuccess(estudiantes) // Call onSuccess with the list of students
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error al obtener los documentos.", exception) // Log error
                onFailure(exception) // Call onFailure with the exception
            }
    }

    // Function to get all classes from the "clase" collection
    fun obtenerTodasLasClases(onSuccess: (List<Map<String, Any>>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("clase")
            .get()
            .addOnSuccessListener { result ->
                val clases = result.map { document ->
                    val data = document.data
                    val id = document.id // Get the document ID
                    val materia = data["materia"] ?: "Sin materia" // Ensure "materia" is not null
                    data + mapOf("id" to id, "materia" to materia) // Combine document data with ID and materia
                }
                onSuccess(clases) // Call onSuccess with the list of classes
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error al obtener los documentos.", exception) // Log error
                onFailure(exception) // Call onFailure with the exception
            }
    }

    // Function to get all teachers from the "profesor" collection
    fun obtenerTodosProfesores(onSuccess: (List<Map<String, Any>>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("profesor")
            .get()
            .addOnSuccessListener { result ->
                val profesores = result.map { document ->
                    val data = document.data
                    val id = document.id // Get the document ID
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
                onSuccess(profesores) // Call onSuccess with the list of teachers
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error al obtener los documentos.", exception) // Log error
                onFailure(exception) // Call onFailure with the exception
            }
    }

    // Function to get a class by ID from the "clase" collection
    fun obtenerClasePorId(id: String, onSuccess: (Map<String, Any>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("clase")
            .document(id)
            .get()
            .addOnSuccessListener { result ->
                result.data?.let { onSuccess(it) } // Call onSuccess with the class data if it exists
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error al obtener el documento.", exception) // Log error
                onFailure(exception) // Call onFailure with the exception
            }
    }

    // Function to get classes by name from the "clase" collection
    fun obtenerClasePorNombre(nombre: String, onSuccess: (List<Map<String, Any>>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("clase")
            .whereEqualTo("nombre", nombre)
            .get()
            .addOnSuccessListener { result ->
                val clases = result.map { it.data } // Map each document to its data
                onSuccess(clases) // Call onSuccess with the list of classes
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error al obtener los documentos.", exception) // Log error
                onFailure(exception) // Call onFailure with the exception
            }
    }
}


