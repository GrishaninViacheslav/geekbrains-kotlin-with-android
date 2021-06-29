package GeekBrians.Slava_5655380.ui.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import GeekBrians.Slava_5655380.R
import GeekBrians.Slava_5655380.ui.viewmodel.collectionsfeed.CollectionsFeedViewModel

class CollectionsFeedFragment : Fragment() {

    companion object {
        fun newInstance() = CollectionsFeedFragment()
    }

    private lateinit var viewModel: CollectionsFeedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.collections_feed_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CollectionsFeedViewModel::class.java)
        // TODO: Use the ViewModel
    }

}