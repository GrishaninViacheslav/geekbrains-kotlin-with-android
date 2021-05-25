package GeekBrians.Slava_5655380.ui.viewmodel.RecommendationFeed

sealed class AppState {
    object Success : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}