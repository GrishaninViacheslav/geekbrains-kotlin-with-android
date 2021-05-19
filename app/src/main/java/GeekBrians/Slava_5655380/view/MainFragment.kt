package GeekBrians.Slava_5655380.view

import GeekBrians.Slava_5655380.databinding.MainFragmentBinding
import GeekBrians.Slava_5655380.viewmodel.MainViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private fun setupBindings(
        inflater: LayoutInflater, container: ViewGroup?
    ) {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

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
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.feed(true)
                }
                if (!recyclerView.canScrollVertically(-1)) {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupBindings(inflater, container)
        return binding.root
    }
}