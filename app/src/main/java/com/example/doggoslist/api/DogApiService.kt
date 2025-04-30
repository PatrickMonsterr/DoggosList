import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


data class DogImageResponse(val message: String)

interface DogApi {
    @GET("breeds/image/random")
    suspend fun getRandomDogImage(): DogImageResponse
}

val dogApi: DogApi = Retrofit.Builder()
    .baseUrl("https://dog.ceo/api/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(DogApi::class.java)
