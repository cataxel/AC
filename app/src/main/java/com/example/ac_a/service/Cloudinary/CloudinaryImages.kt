package com.example.ac_a.service.Cloudinary


import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import org.ac.APIConf.APIConf
import org.ac.APIConf.NetworkClient
import com.example.ac_a.Model.Images.CloudinaryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import org.ac.APIConf.CloudinaryConf
import org.ac.service.Cloudinary.interfaces.CloudinaryInterface
import java.io.File

class CloudinaryImages : CloudinaryInterface {
    private val client = NetworkClient.httpClient

    override suspend fun getAlbumPhotos(): CloudinaryResponse {
        return try {
            val response: HttpResponse = client.get(APIConf.INTEGRACION_ENDPOINT) {
                contentType(ContentType.Application.Json)
            }

            // Deserializa la respuesta completa en CloudinaryResponse
            val responseBody = response.bodyAsText()
            Json.decodeFromString(responseBody)
        }
        catch (e: Exception) {
            // Manejar errores y devolver una respuesta vacía o con un estado de error
            CloudinaryResponse(
                estado = false,
                mensaje = "Error al obtener imágenes: ${e.message}",
                data = emptyList()
            )
        }
    }

    override suspend fun uploadImageProfile(filePath: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val result = CloudinaryConf.cloudinary.uploader().upload(filePath, mapOf("folder" to "Profile"))
                result["url"] as String
            } catch (e: Exception) {
                throw Exception("Error al subir la imagen: ${e.message}")
            }
        }
    }

}