package GeekBrians.Slava_5655380.ui.view

import GeekBrians.Slava_5655380.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, RecommendationFeedFragment.newInstance())
                    .commitNow()
        }
    }
}