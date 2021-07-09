package GeekBrians.Slava_5655380.ui.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import GeekBrians.Slava_5655380.databinding.CollectionsFeedFragmentBinding
import GeekBrians.Slava_5655380.ui.viewmodel.collectionsfeed.CollectionsFeedViewModel

class CollectionsFeedFragment : Fragment() {

    companion object {
        fun newInstance() = CollectionsFeedFragment()
    }

    private var _binding: CollectionsFeedFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CollectionsFeedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CollectionsFeedFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CollectionsFeedViewModel::class.java)
        viewModel.data.observe(viewLifecycleOwner){
            binding.data.text = "$it"
        }

    }

}