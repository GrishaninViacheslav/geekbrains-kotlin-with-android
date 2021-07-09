package GeekBrians.Slava_5655380.domain.model.repositoryimpl

import GeekBrians.Slava_5655380.App
import GeekBrians.Slava_5655380.domain.MovieMetadata
import GeekBrians.Slava_5655380.domain.model.Repository
import GeekBrians.Slava_5655380.domain.model.repositoryimpl.room.MovieUserDataDAO
import GeekBrians.Slava_5655380.domain.model.repositoryimpl.tmdb.TMDBListLoader
import GeekBrians.Slava_5655380.domain.model.repositoryimpl.tmdb.TmdbMovieDTO

class RepositoryImpl(val listLoader: TMDBListLoader = TMDBListLoader(), val movieUserDataDAO: MovieUserDataDAO = App.instance.getMovieUserDataDao()) : Repository {
    private val MIN_INDEX = 0;
    private val MAX_INDEX = 20000;

    private var genreFilter: Int? = null

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

    override fun setGenreFilter(genreId: Int?) {
        this.genreFilter = genreId
    }

    // TODO: буфферизовать загруженные ранее данные
    override fun getRange(fromIndex: Int, toIndex: Int): List<MovieMetadata> {
        fun getDataFromTmdb(fromIndex: Int, toIndex: Int): List<MovieMetadata>{
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
            val tmdbDTOsResults: ArrayList<TmdbMovieDTO> = ArrayList()
            for (i in fromPage..toPage) {
                tmdbDTOsResults.addAll(listLoader.load(i, genreFilter).results)
            }
            return toMovieMetadataArray(tmdbDTOsResults, (fromPage - 1) * listLoader.resultLength).subList(
                croppedFromIndex % listLoader.resultLength,
                (croppedToIndex % listLoader.resultLength) + (toPage - fromPage) * listLoader.resultLength + 1
            )
        }
        fun fillWithUserData(movieMetadataArray: List<MovieMetadata>): List<MovieMetadata>{
            for(movieMetadata in movieMetadataArray){
                movieMetadata.userScore = movieUserDataDAO.getDataById(movieMetadata.id)?.userScore
            }
            return movieMetadataArray
        }
        // TODO: как это пределать в реактивную цепочку?
        return fillWithUserData(getDataFromTmdb(fromIndex, toIndex))
    }

    override fun getMovieData(movieId: String): MovieMetadata {
        TODO("Not yet implemented")
    }
}