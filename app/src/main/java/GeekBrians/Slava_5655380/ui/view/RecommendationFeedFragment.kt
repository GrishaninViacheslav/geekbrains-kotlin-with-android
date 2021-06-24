package GeekBrians.Slava_5655380.ui.view

import GeekBrians.Slava_5655380.databinding.MainFragmentBinding
import GeekBrians.Slava_5655380.domain.MovieMetadata
import GeekBrians.Slava_5655380.ui.viewmodel.recommendationfeed.AppState
import GeekBrians.Slava_5655380.ui.viewmodel.recommendationfeed.RecommendationFeedEvent
import GeekBrians.Slava_5655380.ui.viewmodel.recommendationfeed.RecommendationFeedViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*

class RecommendationFeedFragment : Fragment() {
    // TODO: как сделать object из FeedScrollListener?
    //             Если просто заменить inner class на object, то тогда FeedScrollListener
    //             не будет иметь доступ к необходимым ему полям RecommendationFeedFragment
    private inner class FeedScrollListener : RecyclerView.OnScrollListener() {
        private var scrollState = SCROLL_STATE_SETTLING
        private var lastDy = 0

        // TODO: проконтролировать, чтобы значение feedNecessityThreshold
        //             не был слишком большим, то есть таким при котором
        //             постоянно вызывались бы feed для обоих концов ленты
        private val feedNecessityThreshold = 3

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            scrollState = newState
            if (newState == SCROLL_STATE_IDLE) {
                with(binding.recyclerViewLines.layoutManager as LinearLayoutManager) {
                    if (lastDy > 0 && findLastVisibleItemPosition() > viewModel.getItemCount() - feedNecessityThreshold) {
                        viewModel.feed(true)
                    }
                    if (lastDy < 0 && findFirstVisibleItemPosition() < feedNecessityThreshold) {
                        viewModel.feed(false)
                    }
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            lastDy = dy
            if (dx == 0 && dy == 0) {
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.feed(true)
                }
            }
            if (scrollState == SCROLL_STATE_DRAGGING) {
                with(binding.recyclerViewLines.layoutManager as LinearLayoutManager) {
                    if (dy > 0 && findLastVisibleItemPosition() > viewModel.getItemCount() - feedNecessityThreshold) {
                        viewModel.feed(true)
                    }
                    if (dy < 0 && findFirstVisibleItemPosition() < feedNecessityThreshold) {
                        viewModel.feed(false)
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance() = RecommendationFeedFragment()
    }

    private val viewModel: RecommendationFeedViewModel by lazy {
        ViewModelProvider(this).get(RecommendationFeedViewModel::class.java).apply {
            getFeedState().observe(viewLifecycleOwner, ::renderFeedState)
            feed(true)
        }
    }

    // TODO: Можно ли это упростить, например заменить делегатом?
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private fun renderFeedState(feedState: AppState) {
        when (feedState) {
            is AppState.Success -> {
                binding.errorTextView.visibility = GONE
            }
            is AppState.Error -> {
                binding.errorTextView.visibility = VISIBLE
                binding.errorTextView.text = feedState.error.message
            }
        }
    }

    private fun handleEvent(event: Bundle) {
        when (event.getString(RecommendationFeedEvent.action)) {
            RecommendationFeedEvent.openFilmDetails -> {
                // TODO: как избежать использование каста?
                (activity as FragmentManager).openFilmDetails(event.getParcelable<MovieMetadata>(RecommendationFeedEvent.movieMetadata)!!)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false).apply {
            recyclerViewLines.layoutManager = LinearLayoutManager(context)
            recyclerViewLines.adapter = viewModel.adapter.apply {
                // TODO: разобраться почему это нельзя разместить в lazy
                //            делегате viewModel
                //            (https://github.com/GrishaninVyacheslav/geekbrains-kotlin-with-android/pull/7)
                getEventSource()
                    .observe(viewLifecycleOwner) { event ->
                        event.getContentIfNotHandled()?.let { handleEvent(it) }
                    }
            }
            recyclerViewLines.addOnScrollListener(FeedScrollListener())
        }
        return binding.root
    }
}