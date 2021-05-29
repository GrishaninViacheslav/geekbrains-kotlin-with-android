package GeekBrians.Slava_5655380.ui

import android.os.Bundle

class Event(private val content: Bundle) {
    var hasBeenHandled = false
    fun getContentIfNotHandled(): Bundle? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}