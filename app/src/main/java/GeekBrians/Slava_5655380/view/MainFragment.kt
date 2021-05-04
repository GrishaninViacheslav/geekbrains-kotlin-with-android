package GeekBrians.Slava_5655380.view

import GeekBrians.Slava_5655380.databinding.MainFragmentBinding
import GeekBrians.Slava_5655380.model.MovieMetadata
import GeekBrians.Slava_5655380.viewmodel.Adapter
import GeekBrians.Slava_5655380.viewmodel.AppState
import GeekBrians.Slava_5655380.viewmodel.MainViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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

    private fun initRecyclerView(recyclerView: RecyclerView, data: Array<MovieMetadata>) {
        recyclerView.setHasFixedSize(false)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        val adapter = Adapter(data)
        recyclerView.adapter = adapter
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                Log.d("[AppStateLog]", "AppState.Success")
                val movieMetadataArray = appState.movieMetadataArray
                initRecyclerView(binding.recyclerViewLines, movieMetadataArray)
            }
            is AppState.Loading -> {
                Log.d("[AppStateLog]", "AppState.Loading")
            }
            is AppState.Error -> {
                Log.d("[AppStateLog]", "AppState.Error")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getMetadata()
    }
}