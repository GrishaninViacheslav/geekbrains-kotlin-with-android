package GeekBrians.Slava_5655380.ui.view

import GeekBrians.Slava_5655380.databinding.MainFragmentBinding
import GeekBrians.Slava_5655380.ui.viewmodel.RecommendationFeed.AppState
import GeekBrians.Slava_5655380.ui.viewmodel.RecommendationFeed.RecommendationFeedViewModel
import android.os.Bundle
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
        binding.recyclerViewLines.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        binding.recyclerViewLines.layoutManager = layoutManager
        binding.recyclerViewLines.itemAnimator =
            null // https://stackoverflow.com/questions/35653439/recycler-view-inconsistency-detected-invalid-view-holder-adapter-positionviewh
        binding.recyclerViewLines.adapter = viewModel.adapter

        viewModel.feed(true)

        var isInitialScrollStateChanged: Boolean = false
        binding.recyclerViewLines.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                isInitialScrollStateChanged = true
                if (layoutManager.findLastVisibleItemPosition() > viewModel.getItemCount() - 3) {
                    viewModel.feed(true)
                }
                if (layoutManager.findFirstVisibleItemPosition() < 3) {
                    viewModel.feed(false)
                }

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isInitialScrollStateChanged) {
                    if (!recyclerView.canScrollVertically(1)) {
                        viewModel.feed(true)
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupBindings(inflater, container)
        return binding.root
    }
}