package hu.bme.aut.shoppinglist.network

import retrofit2.Call
import retrofit2.http.GET
import hu.bme.aut.shoppinglist.model.DogData

interface DogApi {
    @GET("/api/breeds/image/random")
    fun getDog(): Call<DogData>
}