package GeekBrians.Slava_5655380.model

class DebugRepository : Repository {
    private val data = arrayOf(
        MovieMetadata(
            0,
            "Zero movie",
            "Zero movie original title",
            "Lorem ipsum dolor sit amet"
        ),
        MovieMetadata(
            1,
            "First movie",
            "First movie original title",
            "Lorem ipsum dolor sit amet"
        ),
        MovieMetadata(
            2,
            "Second movie",
            "Second movie original title",
            "Lorem ipsum dolor sit amet"
        ),
        MovieMetadata(
            3,
            "Third movie",
            "Third movie original title",
            "Lorem ipsum dolor sit amet"
        ),
        MovieMetadata(
            4,
            "Fourth movie",
            "Fourth movie original title",
            "Lorem ipsum dolor sit amet"
        ),
        MovieMetadata(
            5,
            "Fifth movie",
            "Fifth movie original title",
            "Lorem ipsum dolor sit amet"
        ),
        MovieMetadata(
            6,
            "Sixth movie",
            "Sixth movie original title",
            "Lorem ipsum dolor sit amet"
        ),
        MovieMetadata(
            7,
            "Seventh movie",
            "Seventh movie original title",
            "Lorem ipsum dolor sit amet"
        ),
        MovieMetadata(
            8,
            "Eighth movie",
            "Eighth movie original title",
            "Lorem ipsum dolor sit amet"
        ),
        MovieMetadata(
            9,
            "Ninth movie",
            "Ninth movie original title",
            "Lorem ipsum dolor sit amet"
        ),
        MovieMetadata(
            10,
            "Tenth movie",
            "Tenth movie original title",
            "Lorem ipsum dolor sit amet"
        ),
        MovieMetadata(11, "11 movie", "11 movie original title", "Lorem ipsum dolor sit amet"),
        MovieMetadata(12, "12 movie", "12 movie original title", "Lorem ipsum dolor sit amet"),
        MovieMetadata(13, "13 movie", "13 movie original title", "Lorem ipsum dolor sit amet"),
        MovieMetadata(14, "14 movie", "14 movie original title", "Lorem ipsum dolor sit amet"),
        MovieMetadata(15, "15 movie", "15 movie original title", "Lorem ipsum dolor sit amet"),
        MovieMetadata(16, "16 movie", "16 movie original title", "Lorem ipsum dolor sit amet"),
        MovieMetadata(17, "17 movie", "17 movie original title", "Lorem ipsum dolor sit amet")
    )

    override fun getRange(fromIndex: Int, toIndex: Int): Array<MovieMetadata> {
        if(fromIndex > data.size - 1 || toIndex < 0) return arrayOf()
        val begin = if (fromIndex < 0) 0 else fromIndex
        val end = if (toIndex > data.size - 1) data.size else toIndex + 1
        return data.copyOfRange(begin, end)
    }
}