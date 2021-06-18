package GeekBrians.Slava_5655380.ui.view

import GeekBrians.Slava_5655380.R
import GeekBrians.Slava_5655380.databinding.MainActivityBinding
import GeekBrians.Slava_5655380.ui.viewmodel.recommendationfeed.RecommendationFeedEvent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity(), FragmentManager {
    private var lastActiveToggle: MaterialButton? = null
    private val binding: MainActivityBinding by lazy {
        MainActivityBinding.inflate(layoutInflater).apply {
            fun toggleButton(enabledButton: MaterialButton) {
                lastActiveToggle?.background = ColorDrawable(Color.TRANSPARENT)
                enabledButton.background = ColorDrawable(getColor(R.color.purple_700))
                lastActiveToggle = enabledButton
            }

            buttonRecommendation.setOnClickListener {
                if (buttonRecommendation == lastActiveToggle) {
                    return@setOnClickListener
                }
                toggleButton(buttonRecommendation)
                openRecommendationFeed()
            }
            buttonCollections.setOnClickListener {
                if (buttonCollections == lastActiveToggle) {
                    return@setOnClickListener
                }
                toggleButton(buttonCollections)
                openFragment(COLLECTIONS_FEED_FRAGMENT_NAME, CollectionsFeedFragment.newInstance())
            }
            buttonSearch.setOnClickListener {
                if (buttonSearch == lastActiveToggle) {
                    return@setOnClickListener
                }
                toggleButton(buttonSearch)
                openFragment(SEARCH_FRAGMENT_NAME, SearchFragment.newInstance())
            }
            buttonLibrary.setOnClickListener {
                if (buttonLibrary == lastActiveToggle) {
                    return@setOnClickListener
                }
                toggleButton(buttonLibrary)
                openFragment(LIBRARY_FRAGMENT_NAME, LibraryFragment.newInstance())
            }
            buttonSettings.setOnClickListener {
                if (buttonSettings == lastActiveToggle) {
                    return@setOnClickListener
                }
                toggleButton(buttonSettings)
                openFragment(SETTINGS_FRAGMENT_NAME, SettingsFragment.newInstance())
            }
            toggleButton(buttonRecommendation)
        }
    }

    val RECOMMENDATION_FEED_FRAGMENT_NAME = "RECOMMENDATION_FEED"
    val FILM_DETAILS_FRAGMENT_NAME = "FILM_DETAILS"
    val COLLECTIONS_FEED_FRAGMENT_NAME = "COLLECTIONS_FEED"
    val SEARCH_FRAGMENT_NAME = "SEARCH"
    val LIBRARY_FRAGMENT_NAME = "LIBRARY"
    val SETTINGS_FRAGMENT_NAME = "SETTINGS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            openRecommendationFeed()
        }
    }

    fun openFragment(name: String, instance: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, supportFragmentManager.findFragmentByTag(name) ?: instance, name)
            .addToBackStack(null)
            .commit()
    }

    override fun openRecommendationFeed() {
        // TODO: как можно ускорить эту транзакцию?
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, supportFragmentManager.findFragmentByTag(RECOMMENDATION_FEED_FRAGMENT_NAME) ?: RecommendationFeedFragment.newInstance(), RECOMMENDATION_FEED_FRAGMENT_NAME)
            .commitNow()
    }

    override fun openFilmDetails(index: Int) {
        with(supportFragmentManager.beginTransaction())
        {
            replace(
                R.id.container,
                supportFragmentManager.findFragmentByTag(FILM_DETAILS_FRAGMENT_NAME) ?:
                        FilmDetailsFragment.newInstance().apply {
                    arguments = Bundle().apply { putInt(RecommendationFeedEvent.filmIndex, index) }
                }, FILM_DETAILS_FRAGMENT_NAME)
            addToBackStack(null)
            commit()
        }
    }
}

interface FragmentManager {
    fun openRecommendationFeed()
    fun openFilmDetails(index: Int)
}