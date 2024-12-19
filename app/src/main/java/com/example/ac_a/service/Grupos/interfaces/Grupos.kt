package com.example.ac_a.service.Grupos.interfaces

import com.example.ac_a.APIRespuesta
import com.example.ac_a.Model.Grupos.Grupo
import com.example.ac_a.Model.Grupos.GrupoRequest
import com.example.ac_a.Model.Grupos.GrupoResponse
import org.ac.APIConf.APIConf
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface Grupos {
    suspend fun obtenerGrupo(): APIRespuesta<List<Grupo>>
    suspend fun crearGrupo(grupo: Grupo): APIRespuesta<Grupo>
    suspend fun actualizarGrupo(grupo: Grupo): APIRespuesta<Grupo>
    suspend fun eliminarGrupo(grupo: Grupo): APIRespuesta<Grupo>
}

interface GrupoRetrofit {
    @GET(APIConf.GRUPOS_ENDPOINT)
    suspend fun obtenerGrupo(): GrupoResponse

    @POST(APIConf.GRUPOS_ENDPOINT)
    suspend fun crearGrupo(@Body grupo: GrupoRequest): APIRespuesta<Grupo>

    @PUT("${APIConf.GRUPOS_ENDPOINT}{guid}/")
    suspend fun actualizarGrupo(@Path("guid") guid: String, @Body grupo: GrupoRequest): APIRespuesta<Grupo>

    @DELETE("${APIConf.GRUPOS_ENDPOINT}{guid}/")
    suspend fun eliminarGrupo(@Path("guid") guid: String): APIRespuesta<Grupo>
}