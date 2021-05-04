package GeekBrians.Slava_5655380.model

class RepositoryImpl : Repository {
    override fun getMetadataFromServer(): Array<MovieMetadata> {
        return arrayOf(
            MovieMetadata("First movie", "First movie original title", "Lorem ipsum dolor sit amet"),
            MovieMetadata("Second movie", "Second movie original title", "Lorem ipsum dolor sit amet"),
            MovieMetadata("Third movie", "Third movie original title", "Lorem ipsum dolor sit amet"),
            MovieMetadata("Fourth movie", "Fourth movie original title", "Lorem ipsum dolor sit amet"),
            MovieMetadata("Fifth movie", "Fifth movie original title", "Lorem ipsum dolor sit amet"),
            MovieMetadata("Sixth movie", "Sixth movie original title", "Lorem ipsum dolor sit amet"),
            MovieMetadata("Seventh movie", "Seventh movie original title", "Lorem ipsum dolor sit amet"),
            MovieMetadata("Eighth movie", "Eighth movie original title", "Lorem ipsum dolor sit amet"),
            MovieMetadata("Ninth movie", "Ninth movie original title", "Lorem ipsum dolor sit amet"),
            MovieMetadata("Tenth movie", "Tenth movie original title", "Lorem ipsum dolor sit amet"),
        )
    }

    override fun getMetadataFromLocalStorage(): Array<MovieMetadata> {
        return getMetadataFromServer()
    }
}