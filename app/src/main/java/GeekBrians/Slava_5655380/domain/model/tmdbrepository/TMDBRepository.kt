package GeekBrians.Slava_5655380.domain.model.tmdbrepository

import GeekBrians.Slava_5655380.domain.MovieMetadata
import GeekBrians.Slava_5655380.domain.model.Repository

class TMDBRepository(val listLoader: TMDBListLoader = TMDBListLoader(), val filmDataLoader: TMDBFilmDataLoader = TMDBFilmDataLoader()) : Repository {
    private val MIN_INDEX = 0;
    private val MAX_INDEX = 20000;

    private fun toMovieMetadataArray(
        tmdbDTOsArray: ArrayList<TmdbMovieDTO>,
        pageFirstEllIndex: Int
    ): ArrayList<MovieMetadata> = ArrayList<MovieMetadata>().apply {
        addAll(Array<MovieMetadata>(tmdbDTOsArray.size) { i ->
            MovieMetadata(id = tmdbDTOsArray[i].id.toString(), index = pageFirstEllIndex + i).apply {
                originalTitle = tmdbDTOsArray[i].original_title
                posterUri =
                    if (tmdbDTOsArray[i].poster_path != "") tmdbDTOsArray[i].poster_path else null
            }
        })
    }

    // TODO: буфферизовать загруженные ранее данные
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
        val fromPage = listLoader.findPageIndex(croppedFromIndex)
        val toPage = listLoader.findPageIndex(croppedToIndex)
        // Разделение на TMDBDTO и MovieMetadata так как в дальнеёшем,
        // в MovieMetadata будут поля значения которых нужно будет
        // получать из других баз данных, так как их не найти в TMDBD
        val tmdbDTOsResults: ArrayList<TmdbMovieDTO> = ArrayList()
        for (i in fromPage..toPage) {
            tmdbDTOsResults.addAll(listLoader.load(i).results)
        }
        return toMovieMetadataArray(tmdbDTOsResults, (fromPage - 1) * listLoader.resultLength).subList(
            croppedFromIndex % listLoader.resultLength,
            (croppedToIndex % listLoader.resultLength) + (toPage - fromPage) * listLoader.resultLength + 1
        )
    }

    override fun getMovieData(movieId: String): MovieMetadata {
        TODO("Not yet implemented")
    }
}