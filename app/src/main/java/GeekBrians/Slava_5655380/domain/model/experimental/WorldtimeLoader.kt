package GeekBrians.Slava_5655380.domain.model.experimental

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WorldtimeLoader {
    private val expApi = Retrofit.Builder()
        .baseUrl("https://www.timeapi.io/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build().create(WorldtimeAPI::class.java)

    fun load(timezone: String): WorldtimeDTO {
        return expApi.getTime(timezone).execute().body() ?: WorldtimeDTO(null)
    }
}