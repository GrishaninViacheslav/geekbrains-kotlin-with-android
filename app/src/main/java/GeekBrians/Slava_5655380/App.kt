package GeekBrians.Slava_5655380

import android.app.Application
import java.lang.Exception

class App : Application() {
    companion object {
        private var _instance: App? = null
        val instance: App
            get() = _instance ?: throw Exception("Can not provide application instance")
    }

    val settings: Settings by lazy {
        Settings(
            instance.getSharedPreferences(
                Settings.PREFERENCES_NAME,
                MODE_PRIVATE
            ).getBoolean(Settings.SHOW_ADULT_CONTENT_VALUE_KEY, false)

        )
    }

    override fun onCreate() {
        _instance = this
        super.onCreate()
    }
}

data class Settings(var showAdultContent: Boolean) {
    companion object {
        val PREFERENCES_NAME: String = "SETTINGS"
        val SHOW_ADULT_CONTENT_VALUE_KEY = "SHOW_ADULT_CONTENT_VALUE"
    }
}