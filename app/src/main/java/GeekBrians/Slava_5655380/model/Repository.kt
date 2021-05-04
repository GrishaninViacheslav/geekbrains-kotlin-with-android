package GeekBrians.Slava_5655380.model

interface Repository {
    fun getMetadataFromServer(): Array<MovieMetadata>
    fun getMetadataFromLocalStorage(): Array<MovieMetadata>
}