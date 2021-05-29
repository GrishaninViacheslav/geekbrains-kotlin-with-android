package GeekBrians.Slava_5655380.ui.viewmodel.recommendationfeed

sealed class AppState {
    object Success : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}