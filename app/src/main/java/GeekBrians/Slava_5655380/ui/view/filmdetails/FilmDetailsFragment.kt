package GeekBrians.Slava_5655380.ui.view.filmdetails

import GeekBrians.Slava_5655380.App
import GeekBrians.Slava_5655380.databinding.FilmDetailsFragmentBinding
import GeekBrians.Slava_5655380.domain.MovieMetadata
import GeekBrians.Slava_5655380.domain.model.repositoryimpl.room.MovieUserDataEntity
import GeekBrians.Slava_5655380.ui.viewmodel.filmdetails.FilmDetailsViewModel
import GeekBrians.Slava_5655380.ui.viewmodel.filmdetails.FilmDetailsViewModelFactory
import GeekBrians.Slava_5655380.ui.viewmodel.recommendationfeed.RecommendationFeedEvent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.lang.Exception

class FilmDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = FilmDetailsFragment()
    }

    private val viewModel: FilmDetailsViewModel by lazy {
        ViewModelProvider(
            this,
            FilmDetailsViewModelFactory(
                arguments?.getParcelable<MovieMetadata>(RecommendationFeedEvent.movieMetadata)
                    ?: throw Exception("FilmDetailsViewModel requires MovieMetadata to instantiate but null was provided")
            )
        ).get(FilmDetailsViewModel::class.java)
    }

    private var _binding: FilmDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FilmDetailsFragmentBinding.inflate(inflater, container, false)
        binding.localizedTitle.text = "${viewModel.movieMetadata.id}"
        binding.userScore.text =
            "Ваша оценка: ${viewModel.movieMetadata.userScore ?: "отсутствует"}"
        binding.buttonScore.setOnClickListener {
            val dlgBuilder = RateDialogFragment(viewModel.movieMetadata.id)
            dlgBuilder.resultEvent.observe(viewLifecycleOwner) {
                val userScope = it.getContentIfNotHandled()?.getInt(dlgBuilder.RESULT_EVENT_VALUE_KEY)!!
                Thread{
                    App.instance.getMovieUserDataDao().insert(MovieUserDataEntity(viewModel.movieMetadata.id, userScope))
                }.start()
                viewModel.movieMetadata.userScore = userScope
                binding.userScore.text = "Ваша оценка: ${viewModel.movieMetadata.userScore}"
            }
            fragmentManager?.let { it1 -> dlgBuilder.show(it1, "transactionTag") }
        }
        return binding.root
    }
}