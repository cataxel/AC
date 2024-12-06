package org.ac.Model.Usuarios

import kotlinx.serialization.Serializable

@Serializable
data class Rol(
    val id: String,
    val nombre: String,
    val descripcion: String
)

@Serializable
data class Profile(
    val id: String,
    val id_user: String,
    val telefono: String,
    val direccion: String,
    val carrera: String,
    val numero_control: Int,
    val imagen_url: String
)

@Serializable
data class Usuario(
    val id: String,
    val nombre: String,
    val correo: String,
    val contraseña: String,
    val id_Rol: String
)

@Serializable
data class UsuarioLogin(
    val id: String,
    val correo: String,
    val contraseña: String
)

@Serializable
data class TokenSession(
    val user_guid: String,
    val refresh: String,
    val access: String,
    val rol: String
)