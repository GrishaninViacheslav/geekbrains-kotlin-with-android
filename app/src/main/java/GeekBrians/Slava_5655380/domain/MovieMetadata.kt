package GeekBrians.Slava_5655380.domain

data class MovieMetadata(
    val id: String,
    var index: Int,
    var localizedTitle: String? = null,
    var originalTitle: String? = null,
    var description: String? = null,
    var posterUri: String? = null
)