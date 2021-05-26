package GeekBrians.Slava_5655380.ui.view

import GeekBrians.Slava_5655380.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        var PACKAGE_NAME: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PACKAGE_NAME = packageName
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, RecommendationFeedFragment.newInstance())
                    .commitNow()
        }
    }
}