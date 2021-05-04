package GeekBrians.Slava_5655380.viewmodel

import GeekBrians.Slava_5655380.model.MovieMetadata

sealed class AppState {
    data class Success(val movieMetadataArray: Array<MovieMetadata>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}