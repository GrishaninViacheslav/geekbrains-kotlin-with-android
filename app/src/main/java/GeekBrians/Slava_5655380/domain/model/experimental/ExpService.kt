package GeekBrians.Slava_5655380.domain.model.experimental

import android.app.IntentService
import android.content.Intent

class ExpService(name: String? = "ExpService") : IntentService(name) {
    private val loader = WorldtimeLoader()

    companion object {
        val TIMEZONE_KEY = "TIMEZONE"
        val DATA_KEY = "TIME"
        val EXP_BROADCAST_INTENT_FILTER = "GeekBrians.Slava_5655380.exp_data_fetched\""
    }

    override fun onHandleIntent(intent: Intent?) {
        val timezone = intent?.getStringExtra(TIMEZONE_KEY)
        if (timezone == null) {
            throw Exception("Incorrect ExpService intent extras")
        }
        val data = loader.load(timezone).currentLocalTime
        val intent = Intent(EXP_BROADCAST_INTENT_FILTER)
        intent.putExtra(DATA_KEY, data)
        sendBroadcast(intent)
    }
}