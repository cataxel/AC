package com.example.ac_a.service.Actividades.interfaces

import com.example.ac_a.APIRespuesta
import com.example.ac_a.Model.Actividades.Actividad
import com.example.ac_a.Model.Actividades.ActividadResponse
import org.ac.APIConf.APIConf
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface Actividades {
    suspend fun obtenerActividad(): APIRespuesta<List<Actividad>>
    suspend fun crearActividad(actividad: Actividad): APIRespuesta<Actividad>
    suspend fun actualizarActividad(actividad: Actividad): APIRespuesta<Actividad>
    suspend fun eliminarActividad(actividad: Actividad): APIRespuesta<Actividad>
}

interface ActividadesRetrofit {
    @GET(APIConf.ACTIVIDADES_ENDPOINT)
    suspend fun obtenerActividad(): ActividadResponse
    @POST(APIConf.ACTIVIDADES_ENDPOINT)
    suspend fun crearActividad(@Body actividad: Actividad): APIRespuesta<Actividad>

    @PUT("${APIConf.ACTIVIDADES_ENDPOINT}{guid}/")
    suspend fun actualizarActividad(@Path("guid") guid: String, @Body actividad: Actividad): APIRespuesta<Actividad>

    @DELETE("${APIConf.ACTIVIDADES_ENDPOINT}{guid}/")
    suspend fun eliminarActividad(@Path("guid") guid: String): APIRespuesta<Actividad>
}
