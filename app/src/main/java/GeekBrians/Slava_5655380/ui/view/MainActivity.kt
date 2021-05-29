package GeekBrians.Slava_5655380.ui.view

import GeekBrians.Slava_5655380.R
import GeekBrians.Slava_5655380.ui.viewmodel.recommendationfeed.RecommendationFeedEvent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction

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
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = FilmDetailsFragment.newInstance()
        val args = Bundle()
        args.putInt(RecommendationFeedEvent.filmIndex, index)
        fragment.arguments = args
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}

interface FragmentManager {
    fun openRecommendationFeed()
    fun openFilmDetails(index: Int)
}