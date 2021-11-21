package com.example.tabatatimer.fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.tabatatimer.R
import com.example.tabatatimer.viewmodels.WorkoutViewModel
import java.util.*

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen, rootKey)

        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .registerOnSharedPreferenceChangeListener(this)

        val button: Preference? = findPreference(getString(R.string.delete_all))
        button?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val viewModel = ViewModelProviders.of(this).get(WorkoutViewModel::class.java)

            val dialogBuilder = context?.let { it1 -> AlertDialog.Builder(it1) }
            dialogBuilder?.setMessage(getString(R.string.delete_confirmation))
                ?.setCancelable(false)
                ?.setPositiveButton(getString(R.string.yes)) { _, _ ->
                    viewModel.deleteAllWorkouts()
                    viewModel.deleteAllSequences()
                }
                ?.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                    dialog.cancel()
                }
            val alert = dialogBuilder?.create()
            alert?.setTitle(getString(R.string.confirmation))
            alert?.show()
            true
        }
    }

    override fun onSharedPreferenceChanged(prefs: SharedPreferences?, key: String?) {
        when (key){
            activity?.getString(R.string.dark_theme) -> {
                activity?.recreate()
            }
            activity?.getString(R.string.language) ->{
                val locale = Locale("ru")
                Locale.setDefault(locale)
                activity?.recreate()
            }
            activity?.getString(R.string.font_scale) ->{
                activity?.recreate()
            }
        }
    }

}