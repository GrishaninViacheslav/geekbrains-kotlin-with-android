package GeekBrians.Slava_5655380.ui.viewmodel.recommendationfeed

import GeekBrians.Slava_5655380.App
import GeekBrians.Slava_5655380.domain.MovieMetadata
import android.os.Handler
import android.os.Looper
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer

sealed class RVItemState {
    class Success(movieMetadata: MovieMetadata) : RVItemState() {
        val movieDataItem: MovieDataItem = MovieDataItem(
            index = movieMetadata.index,
            localizedTitle = movieMetadata.originalTitle,
            description = movieMetadata.description,
            genres = arrayOf("sci-fi", "horror"),
            director = "Режисёр Режисёрович",
            screenwriter = "Сценарист Сценаристович",
            // TODO: сделать что-то вроде пула плееров размер которого был бы равен feedBuffer.size,
            //             чтобы плееры можно было переиспользовать назначая им новый MediaItem,
            //             вместо создания новго плеера
            trailer = SimpleExoPlayer.Builder(App.context!!).build().apply {
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