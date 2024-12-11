package com.example.ac_a.Controller.loginLogout

import com.example.ac_a.APIRespuesta
import com.example.ac_a.Model.Images.LoginImage
import com.example.ac_a.service.Cloudinary.CloudinaryImages
import org.ac.sessionManager.UserSessionManager


class Controlador(
    private val sessionManager: UserSessionManager
) {
    suspend fun Login(email:String, password:String):APIRespuesta<Unit>{
        return try {
            sessionManager.login(email,password)
            APIRespuesta(
                estado = true,
                mensaje = "Inicio de sesión exitoso",
                data = null
            )
        }catch (e: Exception) {
            APIRespuesta(
                estado = false,
                mensaje = "Error al iniciar sesión",
                data = null
            )
        }
    }

    fun cerrarSesion(): APIRespuesta<Unit> {
        sessionManager.logout()
        return APIRespuesta(
            estado = true,
            mensaje = "Sesión cerrada exitosamente",
            data = null
        )
    }
}

class ImagenesControlador( private val cloudinaryService: CloudinaryImages){
    suspend fun listarImagenes(): APIRespuesta<List<LoginImage>> {
        return try {
            val imagenes = cloudinaryService.getAlbumPhotos()
            APIRespuesta(
                estado = imagenes.estado,
                mensaje = imagenes.mensaje,
                data = imagenes.data
            )
        } catch (e: Exception) {
            APIRespuesta(
                estado = false,
                mensaje = "Error al listar imágenes: ${e.message}",
                data = null
            )
        }
    }
}