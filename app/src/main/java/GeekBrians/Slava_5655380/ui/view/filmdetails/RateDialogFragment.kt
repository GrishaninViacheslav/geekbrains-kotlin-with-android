package GeekBrians.Slava_5655380.ui.view.filmdetails

import GeekBrians.Slava_5655380.R
import GeekBrians.Slava_5655380.databinding.RateDialogFragmentBinding
import GeekBrians.Slava_5655380.ui.viewmodel.filmdetails.RateDialogViewModel
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment


class RateDialogFragment : DialogFragment() {
    private lateinit var viewModel: RateDialogViewModel

    private var _binding: RateDialogFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = RateDialogFragmentBinding.inflate(LayoutInflater.from(context))
        val contentView: View =
            requireActivity().layoutInflater.inflate(R.layout.rate_dialog_fragment, null)
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
            .setTitle(R.string.rate_dialog_fragment_title)
            .setView(binding.root)
            .setNegativeButton(R.string.cancel) { dialogInterface, i ->
                Log.d("[MYLOG]", "canceled")
            }
            .setPositiveButton(
                R.string.confirm_score
            ) { dialogInterface, i ->
                Log.d("[MYLOG]", "Confirmed user score: ${binding.userScore.rating}")
            }
        return builder.create()
    }

}