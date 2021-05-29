package GeekBrians.Slava_5655380.domain.model

import GeekBrians.Slava_5655380.domain.MovieMetadata

interface Repository {
    fun getRange(fromIndex: Int, toIndex: Int): Array<MovieMetadata>
}