package GeekBrians.Slava_5655380.ui.view

import GeekBrians.Slava_5655380.R
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

    override fun openFilmDetails() {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, FilmDetailsFragment.newInstance())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}

interface FragmentManager {
    fun openRecommendationFeed()
    fun openFilmDetails()
}