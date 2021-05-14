package GeekBrians.Slava_5655380.viewmodel

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var adapter: Adapter? = null
    var model = arrayOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelfth", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen", "twenty", "twenty-one", "twenty-two")

    init {
        adapter = Adapter(this)
    }

    fun getItemCount(): Int{
        return model.size
    }

    fun getItem(index: Int): String{
        return model[index]
    }


}