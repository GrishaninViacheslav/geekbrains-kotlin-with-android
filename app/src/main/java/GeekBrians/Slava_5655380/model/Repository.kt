package GeekBrians.Slava_5655380.model

interface Repository {
    fun getRange(fromIndex: Int, toIndex: Int): Array<MovieMetadata>
}