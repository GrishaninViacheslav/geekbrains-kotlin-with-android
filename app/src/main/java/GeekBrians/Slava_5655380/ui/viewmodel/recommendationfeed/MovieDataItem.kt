package GeekBrians.Slava_5655380.ui.viewmodel.recommendationfeed

import com.google.android.exoplayer2.SimpleExoPlayer
import com.squareup.picasso.RequestCreator

data class MovieDataItem(
    val index: Int,
    val localizedTitle: String?,
    val description: String?,
    val genres: Array<String>,
    val director: String,
    val screenwriter: String,
    val trailer: SimpleExoPlayer,
    val poster: RequestCreator?
)
