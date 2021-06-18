package GeekBrians.Slava_5655380

import android.app.Application
import android.content.Context

// https://stackoverflow.com/questions/9445661/how-to-get-the-context-from-anywhere
class App : Application() {
    companion object {
        var instance: App? = null
            private set
    }

    override fun onCreate() {
        instance = this
        super.onCreate()
    }
}
