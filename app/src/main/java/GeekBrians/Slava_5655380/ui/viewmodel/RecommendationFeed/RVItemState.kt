package GeekBrians.Slava_5655380.ui.viewmodel.RecommendationFeed

import GeekBrians.Slava_5655380.domain.MovieMetadata

sealed class RVItemState {
    data class Success(val movieMetadata: MovieMetadata) : RVItemState()
    object Loading : RVItemState()
}