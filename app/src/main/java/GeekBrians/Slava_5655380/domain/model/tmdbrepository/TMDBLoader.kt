package GeekBrians.Slava_5655380.domain.model.tmdbrepository

import GeekBrians.Slava_5655380.BuildConfig
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class TMDBLoader(val resultLength: Int = 20, val readTimeout: Int = 10000) {

    fun findPageIndex(movieIndex: Int): Int {
        return movieIndex / resultLength + 1
    }

    fun load(pageIndex: Int): TmdbDTO {
        // TODO: разобраться почему api_key и page не добавляется с помощью addRequestProperty
        val uri =
            URL("https://api.themoviedb.org/3/trending/movie/week?api_key=${BuildConfig.TMDB_API_KEY}&page=$pageIndex")
        lateinit var urlConnection: HttpsURLConnection
        try {
            urlConnection = uri.openConnection() as HttpsURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.readTimeout = readTimeout
            val bufferedReader =
                BufferedReader(InputStreamReader(urlConnection.inputStream))
            val tmdbDTO = Gson().fromJson(bufferedReader, TmdbDTO::class.java)
            urlConnection.disconnect()
            return tmdbDTO
        } catch (e: Exception) {
            e.printStackTrace()
            urlConnection.disconnect()
            return TmdbDTO()
        }
    }
}