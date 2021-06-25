package GeekBrians.Slava_5655380

import GeekBrians.Slava_5655380.domain.model.repositoryimpl.room.MovieUserDataDAO
import GeekBrians.Slava_5655380.domain.model.repositoryimpl.room.MovieUserDataDB
import android.app.Application
import android.util.Log
import androidx.room.Room
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import java.lang.Exception

class App : Application() {
    companion object {
        private var _instance: App? = null
        val instance: App
            get() = _instance ?: throw Exception("Can not provide application instance")
    }

    private var db: MovieUserDataDB? = null
    private val DB_NAME = "MovieUserData.db"

    @Synchronized
    fun getMovieUserDataDao(): MovieUserDataDAO {
        if (db == null) {
            synchronized(MovieUserDataDB::class.java) {
                if (db == null) {
                    val databaseBuilderThread = Thread {
                        db = Room.databaseBuilder(
                            instance.applicationContext,
                            MovieUserDataDB::class.java,
                            DB_NAME
                        )
                            .build()
                    }
                    databaseBuilderThread.start()
                    databaseBuilderThread.join()
                }
            }
        }
        return db!!.movieUserDataDao()
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
        super.onCreate()
        _instance = this
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            val FBM_TAG = "[FBM]"
            if (!task.isSuccessful) {
                Log.w(FBM_TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            Log.d(FBM_TAG, "FCM token: ${task.result?:"null"}")
        })
        Thread {
            db = Room.databaseBuilder(
                instance.applicationContext,
                MovieUserDataDB::class.java,
                DB_NAME
            )
                .build()
        }.start()
    }
}

data class Settings(var showAdultContent: Boolean) {
    companion object {
        val PREFERENCES_NAME: String = "SETTINGS"
        val SHOW_ADULT_CONTENT_VALUE_KEY = "SHOW_ADULT_CONTENT_VALUE"
    }
}