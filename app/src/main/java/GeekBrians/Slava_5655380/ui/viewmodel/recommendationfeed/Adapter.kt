package GeekBrians.Slava_5655380.ui.viewmodel.recommendationfeed

import GeekBrians.Slava_5655380.databinding.ItemBinding
import GeekBrians.Slava_5655380.ui.Event
import android.os.Bundle
import android.util.Log
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
        val eventContent = Bundle()
        eventContent.putString(
            RecommendationFeedEvent.action,
            RecommendationFeedEvent.openFilmDetails
        )
        eventContent.putInt(
            RecommendationFeedEvent.filmIndex,
            index
        )
        eventSource.value = Event(eventContent)
        Log.d("[MYLOG]", "eventSource.value: ${eventSource.value}")
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        with(viewHolder) {
            when (val rvItemState = viewModel.getItem(i)) {
                is RVItemState.Success -> {
                    binding.backgroundVideo.visibility = VISIBLE
                    binding.moviePoster.visibility = VISIBLE
                    binding.localizedTitle.visibility = VISIBLE
                    binding.progressBar.visibility = GONE

                    binding.localizedTitle.text =
                        rvItemState.movieDataItem.index.toString()
                    binding.backgroundVideo.player = rvItemState.movieDataItem.trailer

                    binding.moviePoster.setOnClickListener { openFilmDetails(rvItemState.movieDataItem.index) }
                }
                is RVItemState.Loading -> {
                    binding.backgroundVideo.visibility = GONE
                    binding.moviePoster.visibility = GONE
                    binding.localizedTitle.visibility = GONE
                    binding.progressBar.visibility = VISIBLE
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return viewModel.getItemCount()
    }

    fun getEventSource() = eventSource

    inner class ViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)
}