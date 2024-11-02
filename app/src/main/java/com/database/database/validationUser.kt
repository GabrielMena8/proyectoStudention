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
            // Si hay un usuario logueado, retorna su correo
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
}