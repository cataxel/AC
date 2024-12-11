package org.ac.sessionManager.interfaces

interface SessionManager {
    fun isUserLogginIn(): Boolean
    fun getUserId():String
    fun getUserToken(): String
    suspend fun getUserRol(): String
    suspend fun login(email:String, password:String)
    fun logout()
}