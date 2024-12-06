package org.ac.APIConf

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json

object APIConf {
    const val BASE_URL = "https://backendac-w661.onrender.com"
    const val USUARIOS_ENDPOINT = "$BASE_URL/usuarios/"
    const val PERFIL_ENDPOINT = "$BASE_URL/perfiles/"
    const val LOGIN_ENDPOINT = "$BASE_URL/login/"
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
    }
}