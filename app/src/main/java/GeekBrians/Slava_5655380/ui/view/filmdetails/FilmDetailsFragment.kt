package GeekBrians.Slava_5655380.ui.view.filmdetails

import GeekBrians.Slava_5655380.databinding.FilmDetailsFragmentBinding
import GeekBrians.Slava_5655380.ui.viewmodel.filmdetails.FilmDetailsViewModel
import GeekBrians.Slava_5655380.ui.viewmodel.filmdetails.FilmDetailsViewModelFactory
import GeekBrians.Slava_5655380.ui.viewmodel.recommendationfeed.RecommendationFeedEvent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class FilmDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = FilmDetailsFragment()
    }

    private val viewModel: FilmDetailsViewModel by lazy {
        ViewModelProvider(this, FilmDetailsViewModelFactory(arguments?.getString(RecommendationFeedEvent.filmId).toString())).get(FilmDetailsViewModel::class.java)
    }

    private var _binding: FilmDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FilmDetailsFragmentBinding.inflate(inflater, container, false)
        binding.localizedTitle.text = "${arguments?.getString(RecommendationFeedEvent.filmId)}"
        binding.buttonScore.setOnClickListener{
            val dlgBuilder: DialogFragment = RateDialogFragment()
            fragmentManager?.let { it1 -> dlgBuilder.show(it1, "transactionTag") }
        }
        return binding.root
    }
}