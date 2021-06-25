package GeekBrians.Slava_5655380.ui.view.filmdetails

import GeekBrians.Slava_5655380.R
import GeekBrians.Slava_5655380.databinding.RateDialogFragmentBinding
import GeekBrians.Slava_5655380.ui.Event
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData

class RateDialogFragment(
    val filmId: String,
    val resultEvent: MutableLiveData<Event> = MutableLiveData()
) : DialogFragment() {
    private var _binding: RateDialogFragmentBinding? = null
    private val binding get() = _binding!!

    val RESULT_EVENT_VALUE_KEY = "USER_SCORE"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = RateDialogFragmentBinding.inflate(LayoutInflater.from(context))
        val contentView: View =
            requireActivity().layoutInflater.inflate(R.layout.rate_dialog_fragment, null)
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
            .setTitle(R.string.rate_dialog_fragment_title)
            .setView(binding.root)
            .setNegativeButton(R.string.cancel) { _, _ -> Unit}
            .setPositiveButton(
                R.string.confirm_score
            ) { _, _ ->
                resultEvent.value =
                    Event(
                        Bundle().apply { putInt(RESULT_EVENT_VALUE_KEY, binding.userScore.rating.toInt()) }
                    )
            }
        return builder.create()
    }
}