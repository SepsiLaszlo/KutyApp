package sepsi.laszlo.networking.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import sepsi.laszlo.networking.model.DogData

interface DogApi {
    @GET("/api/breeds/image/random")
    fun getDog(): Call<DogData>
}