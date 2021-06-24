package GeekBrians.Slava_5655380.ui.viewmodel.recommendationfeed

import GeekBrians.Slava_5655380.domain.MovieMetadata
import com.google.android.exoplayer2.SimpleExoPlayer
import com.squareup.picasso.RequestCreator

data class MovieDataItem(
    val metadata: MovieMetadata,
    val trailer: SimpleExoPlayer,
    val poster: RequestCreator?
)
