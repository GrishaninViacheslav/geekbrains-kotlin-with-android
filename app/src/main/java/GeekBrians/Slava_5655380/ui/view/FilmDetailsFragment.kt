package GeekBrians.Slava_5655380.ui.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import GeekBrians.Slava_5655380.R
import GeekBrians.Slava_5655380.databinding.FilmDetailsFragmentBinding
import GeekBrians.Slava_5655380.databinding.MainFragmentBinding
import GeekBrians.Slava_5655380.ui.viewmodel.filmdetails.FilmDetailsViewModel
import GeekBrians.Slava_5655380.ui.viewmodel.recommendationfeed.RecommendationFeedEvent
import android.widget.TextView

class FilmDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = FilmDetailsFragment()
    }

    private lateinit var viewModel: FilmDetailsViewModel

    private var _binding: FilmDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FilmDetailsFragmentBinding.inflate(inflater, container, false)
        binding.localizedTitle.text = "${arguments?.getInt(RecommendationFeedEvent.filmIndex)}"
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FilmDetailsViewModel::class.java)
    }
}