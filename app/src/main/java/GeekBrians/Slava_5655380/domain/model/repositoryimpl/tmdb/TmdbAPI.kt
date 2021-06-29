package GeekBrians.Slava_5655380.domain.model.repositoryimpl.tmdb

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbAPI {
    @GET("3/trending/movie/week")
    fun getRecommendationsList(
        @Query("api_key") token: String,
        @Query("page") lat: Int,
        @Query("include_adult") adult: Boolean
    ): Call<TmdbMovieListDTO>
}