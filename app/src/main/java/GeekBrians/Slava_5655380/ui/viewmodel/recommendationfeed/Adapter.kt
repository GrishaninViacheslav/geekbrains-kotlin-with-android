package GeekBrians.Slava_5655380.ui.viewmodel.recommendationfeed

import GeekBrians.Slava_5655380.databinding.ItemBinding
import GeekBrians.Slava_5655380.ui.Event
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView

class Adapter(
    private val viewModel: RecommendationFeedViewModel,
    private val eventSource: MutableLiveData<Event> = MutableLiveData()
) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {

    private fun openFilmDetails(index: Int) {
        eventSource.value = Event(Bundle().apply {
            putString(
                RecommendationFeedEvent.action,
                RecommendationFeedEvent.openFilmDetails
            )
            putInt(
                RecommendationFeedEvent.filmIndex,
                index
            )
        })
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int) = ViewHolder(
        ItemBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
    )

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        with(viewHolder.binding) {
            when (val rvItemState = viewModel.getItem(i)) {
                is RVItemState.Success -> {
                    backgroundVideo.visibility = VISIBLE
                    moviePoster.visibility = VISIBLE
                    localizedTitle.visibility = VISIBLE
                    progressBar.visibility = GONE

                    localizedTitle.text =
                        rvItemState.movieDataItem.index.toString()
                    backgroundVideo.player = rvItemState.movieDataItem.trailer

                    moviePoster.setOnClickListener { openFilmDetails(rvItemState.movieDataItem.index) }
                }
                is RVItemState.Loading -> {
                    backgroundVideo.visibility = GONE
                    moviePoster.visibility = GONE
                    localizedTitle.visibility = GONE
                    progressBar.visibility = VISIBLE
                }
            }
        }
    }

    override fun getItemCount() = viewModel.getItemCount()

    fun getEventSource() = eventSource

    inner class ViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)
}