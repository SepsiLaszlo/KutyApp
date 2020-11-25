package hu.bme.aut.shoppinglist.network

import android.os.Handler
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import hu.bme.aut.shoppinglist.model.DogData

object NetworkManager {
    private const val SERVICE_URL = "https://dog.ceo"
//    private const val APP_ID = "ide_jon_a_token"

    private val dogApi: DogApi

    init {

        val retrofit = Retrofit.Builder()
            .baseUrl(SERVICE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        dogApi = retrofit.create(DogApi::class.java)
    }

    private fun <T> runCallOnBackgroundThread(
        call: Call<T>,
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val handler = Handler()
        Thread {
            try {
                val response = call.execute().body()!!
                handler.post { onSuccess(response) }

            } catch (e: Exception) {
                e.printStackTrace()
                handler.post { onError(e) }
            }
        }.start()
    }

    fun getDog(
            onSuccess: (DogData) -> Unit,
            onError: (Throwable) -> Unit
    ){
        val getWeatherRequest = dogApi.getDog()
        runCallOnBackgroundThread(getWeatherRequest, onSuccess, onError)
    }
}