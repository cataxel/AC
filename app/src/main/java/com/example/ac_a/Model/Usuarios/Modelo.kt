package org.ac.Model.Usuarios

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Rol(
    val guid: String,
    val nombre: String,
    val descripcion: String
)

@Serializable
data class Profile(
    var guid: String,
    val usuario: String,
    val telefono: String,
    val direccion: String,
    val carrera: String,
    val numero_control: Int,
    var imagen: String
)

@Serializable
data class ProfileModificar(
    val usuario: String?,
    val telefono: String?,
    val direccion: String?,
    val carrera: String?,
    val numero_control: Int?,
    var imagen: String?
)

@Serializable
data class ProfileRespuesta(
    val usuario: String,
    val telefono: String,
    val direccion: String,
    val carrera: String,
    val numero_control: Int,
    var imagen: String
)

@Serializable
data class Usuario(
    val guid: String,
    val nombre: String,
    val correo: String,
    val contraseña: String,
    val rol: String
)

@Serializable
data class UsuarioRespuesta(
    val guid: String,
    val nombre: String,
    val correo: String,
    val rol: String
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
    @Transient val rol: String = ""
)