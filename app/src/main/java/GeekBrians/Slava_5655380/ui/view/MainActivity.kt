package GeekBrians.Slava_5655380.ui.view

import GeekBrians.Slava_5655380.R
import GeekBrians.Slava_5655380.domain.MovieMetadata
import GeekBrians.Slava_5655380.domain.model.tmdbrepository.TMDBRepository
import GeekBrians.Slava_5655380.ui.viewmodel.recommendationfeed.RecommendationFeedEvent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), FragmentManager {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            //openRecommendationFeed()
            Thread {
                val rep = TMDBRepository()
                val arr: List<MovieMetadata> = rep.getRange(40, 41)
                Log.d("[MYLOG]", "arr: $arr")
            }.start()
        }
    }

    override fun openRecommendationFeed() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, RecommendationFeedFragment.newInstance())
            .commitNow()
    }

    override fun openFilmDetails(index: Int) {
        with(supportFragmentManager.beginTransaction()) {
            replace(
                R.id.container,
                FilmDetailsFragment.newInstance().apply {
                    arguments = Bundle().apply { putInt(RecommendationFeedEvent.filmIndex, index) }
                })
            addToBackStack(null)
            commit()
        }
    }
}

interface FragmentManager {
    fun openRecommendationFeed()
    fun openFilmDetails(index: Int)
}