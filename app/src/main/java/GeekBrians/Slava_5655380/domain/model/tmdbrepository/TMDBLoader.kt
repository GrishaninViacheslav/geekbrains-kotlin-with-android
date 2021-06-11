package GeekBrians.Slava_5655380.domain.model.tmdbrepository

import GeekBrians.Slava_5655380.BuildConfig
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TMDBLoader(val resultLength: Int = 20) {
    private val tmdbApi = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build().create(TmdbAPI::class.java)

    fun findPageIndex(movieIndex: Int): Int {
        return movieIndex / resultLength + 1
    }

    fun load(pageIndex: Int): TmdbDTO =
        tmdbApi.getMovie(BuildConfig.TMDB_API_KEY, pageIndex).execute().body() ?: TmdbDTO()
}
