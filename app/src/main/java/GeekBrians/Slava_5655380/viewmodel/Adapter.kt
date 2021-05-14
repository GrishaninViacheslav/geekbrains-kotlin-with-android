package GeekBrians.Slava_5655380.viewmodel

import GeekBrians.Slava_5655380.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class Adapter(private val viewModel: MainViewModel) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.view.findViewById<TextView>(R.id.textView).text = viewModel.getItem(i)
    }

    override fun getItemCount(): Int {
        return viewModel.getItemCount()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view: ConstraintLayout = itemView as ConstraintLayout
    }
}