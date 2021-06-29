package GeekBrians.Slava_5655380.domain.model.repositoryimpl.tmdb

import GeekBrians.Slava_5655380.App
import GeekBrians.Slava_5655380.BuildConfig
import android.util.Log
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TMDBListLoader(val resultLength: Int = 20) {
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

    fun load(pageIndex: Int, genreId: Int?): TmdbMovieListDTO {
        Log.d("[MYLOG]", "load adultContent: ${App.instance.settings.showAdultContent}")
        return if(genreId == null){
            tmdbApi.getRecommendationsList(
                BuildConfig.TMDB_API_KEY,
                pageIndex,
                App.instance.settings.showAdultContent
            ).execute().body() ?: TmdbMovieListDTO()
        }else{
            tmdbApi.getRecommendationsListWithGenre(
                BuildConfig.TMDB_API_KEY,
                pageIndex,
                App.instance.settings.showAdultContent,
                genreId
            ).execute().body() ?: TmdbMovieListDTO()
        }
    }

}
