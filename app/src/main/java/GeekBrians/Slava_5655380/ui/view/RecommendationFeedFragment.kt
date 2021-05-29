package GeekBrians.Slava_5655380.ui.view

import GeekBrians.Slava_5655380.databinding.MainFragmentBinding
import GeekBrians.Slava_5655380.ui.viewmodel.RecommendationFeed.AppState
import GeekBrians.Slava_5655380.ui.viewmodel.RecommendationFeed.RecomendationFeedEvent
import GeekBrians.Slava_5655380.ui.viewmodel.RecommendationFeed.RecommendationFeedViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*

class RecommendationFeedFragment : Fragment() {

    companion object {
        fun newInstance() = RecommendationFeedFragment()
    }

    private lateinit var viewModel: RecommendationFeedViewModel

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private fun setupBindings(
        inflater: LayoutInflater, container: ViewGroup?
    ) {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(RecommendationFeedViewModel::class.java)
        viewModel.getFeedState().observe(viewLifecycleOwner, Observer { renderFeedState(it) })
        binding.recyclerViewLines.setHasFixedSize(false)
        val layoutManager = LinearLayoutManager(context)
        binding.recyclerViewLines.layoutManager = layoutManager
        binding.recyclerViewLines.itemAnimator =
            null // https://stackoverflow.com/questions/35653439/recycler-view-inconsistency-detected-invalid-view-holder-adapter-positionviewh
        viewModel.adapter.setHasStableIds(false)
        viewModel.adapter.getEventSource().observe(viewLifecycleOwner, Observer { handleEvent(it) })
        binding.recyclerViewLines.adapter = viewModel.adapter


        viewModel.feed(true)

        binding.recyclerViewLines.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var scrollState = SCROLL_STATE_SETTLING
            private var lastDy = 0
            private val feedNecessityThreshold = 3

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                scrollState = newState
                if(newState == SCROLL_STATE_IDLE){
                    if (lastDy > 0 && layoutManager.findLastVisibleItemPosition() > viewModel.getItemCount() - feedNecessityThreshold) {
                        viewModel.feed(true)
                    }
                    if (lastDy < 0 && layoutManager.findFirstVisibleItemPosition() < feedNecessityThreshold) {
                        viewModel.feed(false)
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
                if(scrollState == SCROLL_STATE_DRAGGING){
                    // TODO: проконтролировать, чтобы значение feedNecessityThreshold
                    //             не был слишком большим, то есть таким при котором
                    //             постоянно вызывались бы feed для обоих концов ленты
                    if (dy > 0 && layoutManager.findLastVisibleItemPosition() > viewModel.getItemCount() - feedNecessityThreshold) {
                        viewModel.feed(true)
                    }
                    if (dy < 0 && layoutManager.findFirstVisibleItemPosition() < feedNecessityThreshold) {
                        viewModel.feed(false)
                    }
                }
            }
        })
    }

    private fun renderFeedState(feedState: AppState){
        when(feedState){
            is AppState.Success -> {
                binding.errorTextView.visibility = GONE
            }
            is AppState.Error -> {
                binding.errorTextView.visibility = VISIBLE
                binding.errorTextView.text = feedState.error.message
            }
            AppState.Loading -> {

            }
        }
    }

    private fun handleEvent(event: Bundle){
        when(event.getString(RecomendationFeedEvent.action)){
            RecomendationFeedEvent.openFilmDetails -> {
                Log.d("[MYLOG]", "open film #${event.getInt(RecomendationFeedEvent.filmIndex)} details ")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupBindings(inflater, container)
        return binding.root
    }
}