package GeekBrians.Slava_5655380.domain.model.experimental

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WorldtimeAPI {
    @GET("api/TimeZone/zone")
    fun getTime(
        @Query("timeZone") timezone: String
    ): Call<WorldtimeDTO>
}