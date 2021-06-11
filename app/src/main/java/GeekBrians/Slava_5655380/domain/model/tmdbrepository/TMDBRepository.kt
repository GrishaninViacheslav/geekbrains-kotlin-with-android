package GeekBrians.Slava_5655380.domain.model.tmdbrepository

import GeekBrians.Slava_5655380.domain.MovieMetadata
import GeekBrians.Slava_5655380.domain.model.Repository
import android.util.Log

class TMDBRepository(val loader: TMDBLoader = TMDBLoader()) : Repository {
    private fun toMovieMetadataArray(
        tmdbDTOsArray: ArrayList<TmdbDTOResult>,
        pageFirstEllIndex: Int
    ): ArrayList<MovieMetadata> = ArrayList<MovieMetadata>().apply {
        addAll(Array<MovieMetadata>(tmdbDTOsArray.size) { i ->
            MovieMetadata(index = pageFirstEllIndex + i).apply {
                originalTitle = tmdbDTOsArray[i].original_title
            }
        })
    }


    override fun getRange(fromIndex: Int, toIndex: Int): List<MovieMetadata> {
        val fromPage = loader.findPageIndex(fromIndex)
        val toPage = loader.findPageIndex(toIndex)
        // Разделение на TMDBDTO и MovieMetadata так как в дальнеёшем,
        // в MovieMetadata будут поля значения которых нужно будет
        // получать из других баз данных, так как их не найти в TMDBD
        val tmdbDTOsResults: ArrayList<TmdbDTOResult> = ArrayList()
        for (i in fromPage..toPage) {
            tmdbDTOsResults.addAll(loader.load(i).results)
        }
        Log.d("[MYLOG]", "fromPage: $fromPage, toPage: $toPage")
        Log.d("[MYLOG]", "tmdbDTOsResults.size: ${tmdbDTOsResults.size}")
        Log.d("[MYLOG]", "tmdbDTOsResults: $tmdbDTOsResults")
        Log.d("[MYLOG]", "fromIndex: ${fromIndex % loader.resultLength}, toIndex: ${(toIndex % loader.resultLength) + (toPage - fromPage)*loader.resultLength + 1}")
        return toMovieMetadataArray(tmdbDTOsResults, (fromPage - 1) * loader.resultLength).subList(
            fromIndex % loader.resultLength,
            (toIndex % loader.resultLength) + (toPage - fromPage)*loader.resultLength + 1
        )
    }
}