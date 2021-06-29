package GeekBrians.Slava_5655380.domain.model

import GeekBrians.Slava_5655380.domain.MovieMetadata

interface Repository {
    fun setGenreFilter(genreId: Int?)
    fun getRange(fromIndex: Int, toIndex: Int): List<MovieMetadata>
    fun getMovieData(movieId: String): MovieMetadata
}