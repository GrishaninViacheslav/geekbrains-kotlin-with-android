package GeekBrians.Slava_5655380.ui.viewmodel.collectionsfeed

import GeekBrians.Slava_5655380.domain.model.experimental.ExpRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CollectionsFeedViewModel(
    private val rep: ExpRepository = ExpRepository(),
    val data: MutableLiveData<String> = rep.data
) : ViewModel() {
    init {
        rep.fetchData()
    }
}