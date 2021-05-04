package GeekBrians.Slava_5655380.viewmodel

import GeekBrians.Slava_5655380.R
import GeekBrians.Slava_5655380.model.MovieMetadata
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class Adapter(private val dataSource: Array<MovieMetadata>) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.view.findViewById<TextView>(R.id.textView).text = dataSource[i].localizedTitle
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view: ConstraintLayout = itemView as ConstraintLayout
    }
}