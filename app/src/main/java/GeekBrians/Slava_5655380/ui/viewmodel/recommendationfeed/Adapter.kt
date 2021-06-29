package GeekBrians.Slava_5655380.ui.viewmodel.recommendationfeed

import GeekBrians.Slava_5655380.databinding.ItemBinding
import GeekBrians.Slava_5655380.domain.MovieMetadata
import GeekBrians.Slava_5655380.ui.Event
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView

class Adapter(
    private val dataSource: ArrayList<RVItemState>,
    private val eventSource: MutableLiveData<Event> = MutableLiveData()
) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {

    private fun openFilmDetails(movieMetadata: MovieMetadata) {
        eventSource.value = Event(Bundle().apply {
            putString(
                RecommendationFeedEvent.action,
                RecommendationFeedEvent.openFilmDetails
            )
            putParcelable(
                RecommendationFeedEvent.movieMetadata,
                movieMetadata
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
            when (val rvItemState = dataSource[i]) {
                is RVItemState.Success -> {
                    backgroundVideo.visibility = VISIBLE
                    moviePoster.visibility = VISIBLE
                    localizedTitle.visibility = VISIBLE
                    progressBar.visibility = GONE

                    localizedTitle.text =
                        rvItemState.movieDataItem.metadata.originalTitle
                    backgroundVideo.player = rvItemState.movieDataItem.trailer
                    rvItemState.movieDataItem.poster?.into(moviePoster)

                    moviePoster.setOnClickListener { openFilmDetails(rvItemState.movieDataItem.metadata) }
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

    override fun getItemCount() = dataSource.size

    fun getEventSource() = eventSource

    inner class ViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)
}