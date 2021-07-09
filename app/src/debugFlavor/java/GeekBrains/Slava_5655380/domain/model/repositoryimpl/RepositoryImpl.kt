package GeekBrains.Slava_5655380.domain.model.repositoryimpl

import GeekBrians.Slava_5655380.domain.MovieMetadata
import GeekBrians.Slava_5655380.domain.model.Repository
import kotlin.random.Random

class RepositoryImpl : Repository {
    private val data = listOf(
        MovieMetadata(
            "0",
            0,
            null,
            "Zero movie",
            "Zero movie original title",
            "Lorem ipsum dolor sit amet"
        ),
        MovieMetadata(
            "1",
            1,
            null,
            "First movie",
            "First movie original title",
            "Lorem ipsum dolor sit amet"
        ),
        MovieMetadata(
            "2",
            2,
            null,
            "Second movie",
            "Second movie original title",
            "Lorem ipsum dolor sit amet"
        ),
        MovieMetadata(
            "3",
            3,
            null,
            "Third movie",
            "Third movie original title",
            "Lorem ipsum dolor sit amet"
        ),
        MovieMetadata(
            "4",
            4,
            null,
            "Fourth movie",
            "Fourth movie original title",
            "Lorem ipsum dolor sit amet"
        ),
        MovieMetadata(
            "5",
            5,
            null,
            "Fifth movie",
            "Fifth movie original title",
            "Lorem ipsum dolor sit amet"
        ),
        MovieMetadata(
            "6",
            6,
            null,
            "Sixth movie",
            "Sixth movie original title",
            "Lorem ipsum dolor sit amet"
        ),
        MovieMetadata(
            "7",
            7,
            null,
            "Seventh movie",
            "Seventh movie original title",
            "Lorem ipsum dolor sit amet"
        ),
        MovieMetadata(
            "8",
            8,
            null,
            "Eighth movie",
            "Eighth movie original title",
            "Lorem ipsum dolor sit amet"
        ),
        MovieMetadata(
            "9",
            9,
            null,
            "Ninth movie",
            "Ninth movie original title",
            "Lorem ipsum dolor sit amet"
        ),
        MovieMetadata(
            "10",
            10,
            null,
            "Tenth movie",
            "Tenth movie original title",
            "Lorem ipsum dolor sit amet"
        ),
        MovieMetadata("11",11, null,"11 movie", "11 movie original title", "Lorem ipsum dolor sit amet"),
        MovieMetadata("12",12, null,"12 movie", "12 movie original title", "Lorem ipsum dolor sit amet"),
        MovieMetadata("13",13, null,"13 movie", "13 movie original title", "Lorem ipsum dolor sit amet"),
        MovieMetadata("14",14, null,"14 movie", "14 movie original title", "Lorem ipsum dolor sit amet"),
        MovieMetadata("15",15, null,"15 movie", "15 movie original title", "Lorem ipsum dolor sit amet"),
        MovieMetadata("16",16, null,"16 movie", "16 movie original title", "Lorem ipsum dolor sit amet"),
        MovieMetadata("17",17, null,"17 movie", "17 movie original title", "Lorem ipsum dolor sit amet")
    )

    private val fetchingDelay = 1000L
    private val isThrowingErrors = false

    override fun getRange(fromIndex: Int, toIndex: Int): List<MovieMetadata> {
        Thread.sleep(fetchingDelay)

        if (isThrowingErrors && fromIndex > 7 && Random.nextBoolean()) {
            throw Throwable("Ошибка загрузки")
        }

        if (fromIndex > data.size - 1 || toIndex < 0) return listOf()
        val begin = if (fromIndex < 0) 0 else fromIndex
        val end = if (toIndex > data.size - 1) data.size else toIndex + 1
        return data.subList(begin, end)
    }

    override fun getMovieData(movieId: String): MovieMetadata {
        TODO("Not yet implemented")
    }
}