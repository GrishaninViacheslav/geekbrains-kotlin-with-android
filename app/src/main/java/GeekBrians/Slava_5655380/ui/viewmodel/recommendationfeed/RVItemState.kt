package GeekBrians.Slava_5655380.ui.viewmodel.recommendationfeed

import GeekBrians.Slava_5655380.App
import GeekBrians.Slava_5655380.domain.MovieMetadata
import android.os.Handler
import android.os.Looper
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.squareup.picasso.Picasso

sealed class RVItemState {
    class Success(movieMetadata: MovieMetadata) : RVItemState() {
        companion object {
            val imgBaseUrl: String = "https://image.tmdb.org/t/p/w500/"
        }

        val movieDataItem: MovieDataItem = MovieDataItem(
            index = movieMetadata.index,
            localizedTitle = movieMetadata.originalTitle,
            description = movieMetadata.description,
            genres = arrayOf("sci-fi", "horror"),
            director = "Режисёр Режисёрович",
            screenwriter = "Сценарист Сценаристович",
            poster = if (movieMetadata.posterUri != null) Picasso.get().load("$imgBaseUrl${movieMetadata.posterUri}") else null,
            // TODO: сделать что-то вроде пула плееров размер которого был бы равен feedBuffer.size,
            //             чтобы плееры можно было переиспользовать назначая им новый MediaItem,
            //             вместо создания новго плеера
            trailer = SimpleExoPlayer.Builder(App.instance!!).build().apply {
                Handler(Looper.getMainLooper()).post {
                    setMediaItem(MediaItem.fromUri("https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4"))
                    volume = 0f
                    repeatMode = Player.REPEAT_MODE_ONE
                    playWhenReady = true
                    seekTo(0, 0)
                    prepare()
                }
            }
        )
    }

    object Loading : RVItemState()
}