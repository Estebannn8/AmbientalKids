package com.universidad.ambientalkids.data.model

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONObject

object JsonUploader {

    fun cargarYSubirLecciones(context: Context) {
        val archivos = listOf(
            "agua.json" to "agua",
            "tierra.json" to "tierra",
            "biologia.json" to "biologia",
            "reciclaje.json" to "reciclaje"
        )

        val db = FirebaseFirestore.getInstance()

        for ((archivo, categoria) in archivos) {
            try {
                val jsonString = context.assets.open(archivo).bufferedReader().use { it.readText() }
                val jsonObject = JSONObject(jsonString)

                val keys = jsonObject.keys()
                while (keys.hasNext()) {
                    val leccionId = keys.next()
                    val leccionData = jsonObject.getJSONObject(leccionId)

                    val leccionMap = mutableMapOf<String, Any>()
                    leccionData.keys().forEach { key ->
                        leccionMap[key] = leccionData.get(key)
                    }

                    db.collection("categorias")
                        .document(categoria)
                        .collection("lecciones")
                        .document(leccionId)
                        .set(leccionMap)
                        .addOnSuccessListener {
                            println("Lección $leccionId subida a $categoria")
                        }
                        .addOnFailureListener { e ->
                            println("Error subiendo $leccionId: ${e.message}")
                        }
                }
            } catch (e: Exception) {
                println("Error procesando $archivo: ${e.message}")
            }
        }
    }
}
