package GeekBrians.Slava_5655380.ui.view

import GeekBrians.Slava_5655380.App
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import GeekBrians.Slava_5655380.R
import GeekBrians.Slava_5655380.Settings
import GeekBrians.Slava_5655380.databinding.MainFragmentBinding
import GeekBrians.Slava_5655380.databinding.SettingsFragmentBinding
import android.app.Application
import android.content.Context.MODE_PRIVATE
import java.lang.Exception

class SettingsFragment : Fragment() {

    private var _binding: SettingsFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false).apply {
            switchNsfw.isChecked = App.instance.getSharedPreferences(Settings.PREFERENCES_NAME, MODE_PRIVATE).getBoolean(Settings.SHOW_ADULT_CONTENT_VALUE_KEY, false)
            switchNsfw.setOnClickListener {
                App.instance.getSharedPreferences(Settings.PREFERENCES_NAME, MODE_PRIVATE).edit().putBoolean(Settings.SHOW_ADULT_CONTENT_VALUE_KEY, switchNsfw.isChecked).apply()
                App.instance.settings.showAdultContent = switchNsfw.isChecked
            }
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}