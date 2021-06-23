package GeekBrians.Slava_5655380.ui.viewmodel.filmdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FilmDetailsViewModelFactory(
    private val filmId: String
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FilmDetailsViewModel(filmId) as T
    }
}