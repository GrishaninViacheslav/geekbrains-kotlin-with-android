package GeekBrians.Slava_5655380.domain.model.tmdbrepository

import GeekBrians.Slava_5655380.domain.MovieMetadata
import GeekBrians.Slava_5655380.domain.model.Repository

class TMDBRepository(val loader: TMDBLoader = TMDBLoader()) : Repository {
    private val MIN_INDEX = 0;
    private val MAX_INDEX = 20000;

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
        var croppedFromIndex = fromIndex;
        var croppedToIndex = toIndex;
        if (croppedToIndex < MIN_INDEX || croppedFromIndex > MAX_INDEX) {
            return listOf();
        }
        if (croppedFromIndex < MIN_INDEX) {
            croppedFromIndex = MIN_INDEX;
        }
        if (croppedToIndex > MAX_INDEX) {
            croppedToIndex = MAX_INDEX;
        }
        val fromPage = loader.findPageIndex(croppedFromIndex)
        val toPage = loader.findPageIndex(croppedToIndex)
        // Разделение на TMDBDTO и MovieMetadata так как в дальнеёшем,
        // в MovieMetadata будут поля значения которых нужно будет
        // получать из других баз данных, так как их не найти в TMDBD
        val tmdbDTOsResults: ArrayList<TmdbDTOResult> = ArrayList()
        for (i in fromPage..toPage) {
            tmdbDTOsResults.addAll(loader.load(i).results)
        }
        return toMovieMetadataArray(tmdbDTOsResults, (fromPage - 1) * loader.resultLength).subList(
            croppedFromIndex % loader.resultLength,
            (croppedToIndex % loader.resultLength) + (toPage - fromPage) * loader.resultLength + 1
        )
    }
}