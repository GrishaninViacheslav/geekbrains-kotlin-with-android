package GeekBrians.Slava_5655380.ui.viewmodel.RecommendationFeed

import GeekBrians.Slava_5655380.R
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Space
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ui.PlayerView

class Adapter(private val viewModel: RecommendationFeedViewModel) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        when(val rvItemState = viewModel.getItem(i)){
            is RVItemState.Success -> {
                viewHolder.view.findViewById<PlayerView>(R.id.background_video).visibility = VISIBLE
                viewHolder.view.findViewById<ImageView>(R.id.movie_poster).visibility = VISIBLE
                viewHolder.view.findViewById<TextView>(R.id.localized_title).visibility = VISIBLE
                viewHolder.view.findViewById<ProgressBar>(R.id.progress_bar).visibility = GONE

                viewHolder.view.findViewById<TextView>(R.id.localized_title).text = rvItemState.movieDataItem.index.toString()
                viewHolder.view.findViewById<PlayerView>(R.id.background_video).player = rvItemState.movieDataItem.trailer
            }
            is RVItemState.Loading -> {
                viewHolder.view.findViewById<PlayerView>(R.id.background_video).visibility = GONE
                viewHolder.view.findViewById<ImageView>(R.id.movie_poster).visibility = GONE
                viewHolder.view.findViewById<TextView>(R.id.localized_title).visibility = GONE
                viewHolder.view.findViewById<ProgressBar>(R.id.progress_bar).visibility = VISIBLE
            }

        }

    }

    override fun getItemCount(): Int {
        return viewModel.getItemCount()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view: ConstraintLayout = itemView as ConstraintLayout
    }
}