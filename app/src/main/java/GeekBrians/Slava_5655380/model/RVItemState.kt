package GeekBrians.Slava_5655380.model

sealed class RVItemState {
    data class Success(val movieMetadata: MovieMetadata) : RVItemState()
    object Loading : RVItemState()
}