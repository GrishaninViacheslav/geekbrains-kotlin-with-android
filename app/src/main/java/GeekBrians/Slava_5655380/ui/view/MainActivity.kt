package GeekBrians.Slava_5655380.ui.view

import GeekBrians.Slava_5655380.R
import GeekBrians.Slava_5655380.ui.viewmodel.recommendationfeed.RecommendationFeedEvent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), FragmentManager {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            openRecommendationFeed()
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