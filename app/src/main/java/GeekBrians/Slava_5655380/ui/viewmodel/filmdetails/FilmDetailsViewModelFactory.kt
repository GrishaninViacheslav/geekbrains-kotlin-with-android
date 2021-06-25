package GeekBrians.Slava_5655380.ui.viewmodel.filmdetails

import GeekBrians.Slava_5655380.domain.MovieMetadata
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FilmDetailsViewModelFactory(
    private val movieMetadata: MovieMetadata
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FilmDetailsViewModel(movieMetadata) as T
    }
}