package hu.bme.aut.kutyapp.network

import retrofit2.Call
import retrofit2.http.GET
import hu.bme.aut.kutyapp.model.DogData

interface DogApi {
    @GET("/api/breeds/image/random")
    fun getDog(): Call<DogData>
}