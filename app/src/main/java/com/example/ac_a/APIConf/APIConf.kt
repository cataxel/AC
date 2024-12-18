package org.ac.APIConf

import com.cloudinary.Cloudinary
import com.example.ac_a.service.Actividades.interfaces.ActividadesRetrofit
import com.example.ac_a.service.Grupos.interfaces.GrupoRetrofit
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import org.ac.service.Usuarios.interfaces.UsuarioApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIConf {
    const val BASE_URL = "https://backendac-w661.onrender.com"
    const val ROLES_ENDPOINT = "$BASE_URL/roles/"
    const val USUARIOS_ENDPOINT = "$BASE_URL/usuarios/"
    const val PERFIL_ENDPOINT = "$BASE_URL/perfiles/"
    const val LOGIN_ENDPOINT = "$BASE_URL/login/"
    const val ACTIVIDADES_ENDPOINT="$BASE_URL/actividades/"
    const val GRUPOS_ENDPOINT="$BASE_URL/grupos/"
    const val INSCRIPCIONES_ENDPOINT="$BASE_URL/inscripciones/"
    const val INTEGRACION_ENDPOINT = "${BASE_URL}/integracion/cloudinary-images/"
}

object NetworkClient {
    val httpClient: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 30_000
            socketTimeoutMillis = 30_000
        }
        defaultRequest {
            url(APIConf.BASE_URL)
        }

        HttpResponseValidator {
            validateResponse { response ->
                if (!response.status.isSuccess()) {
                    throw ResponseException(response, "Error en el servidor: ${response.status}")
                }
            }
            handleResponseExceptionWithRequest { exception, request ->
                throw Exception(
                    "No se pudo establecer conexión con el servidor: ${exception.message}. " +
                            "URL: ${request.url}"
                )
            }
        }
    }
}

object CloudinaryConf {
    private const val CLOUD_NAME = "do5pjsoef"
    private const val API_KEY = "378119892223313"
    private const val API_SECRET = "Y7rCrUtqWnLu08y04_Kno6N-KJA"

    val cloudinary: Cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to CLOUD_NAME,
            "api_key" to API_KEY,
            "api_secret" to API_SECRET
        )
    )
}


object RetrofitClient {
    private const val BASE_URL = APIConf.BASE_URL

    val apiServiceUsuarios: UsuarioApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UsuarioApi::class.java)
    }

    val apiServiceActividades: ActividadesRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ActividadesRetrofit::class.java)
    }

    val apiServiceGrupos: GrupoRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GrupoRetrofit::class.java)
    }
}