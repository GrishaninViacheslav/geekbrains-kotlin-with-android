package GeekBrians.Slava_5655380.domain.model.tmdbrepository

import GeekBrians.Slava_5655380.BuildConfig
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TMDBFilmDataLoader() {
    private val tmdbApi = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build().create(TmdbAPI::class.java)

    fun load(filmId: String): TmdbMovieDTO {
        return tmdbApi.getMovie(
            filmId,
            BuildConfig.TMDB_API_KEY,
        ).execute().body() ?: TmdbMovieDTO()
    }

}
