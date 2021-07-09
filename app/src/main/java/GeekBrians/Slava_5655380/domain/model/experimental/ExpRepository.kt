package GeekBrians.Slava_5655380.domain.model.experimental

import GeekBrians.Slava_5655380.App
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.MutableLiveData

class ExpRepository {
    val data = MutableLiveData<String>()

    private val expReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            data.value = intent.getStringExtra(ExpService.DATA_KEY)
        }
    }.also { App.instance.registerReceiver(it, IntentFilter(ExpService.EXP_BROADCAST_INTENT_FILTER))}

    fun fetchData() {
        App.instance.startService(Intent(App.instance, ExpService::class.java).apply {
            putExtra(ExpService.TIMEZONE_KEY, "Europe/Moscow")
        })
    }
}