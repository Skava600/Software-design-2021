package com.example.tabatatimer.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.tabatatimer.data.Workout
import com.example.tabatatimer.R
import com.example.tabatatimer.databinding.AddWorkoutBinding
import com.example.tabatatimer.viewmodels.WorkoutViewModel

class WorkoutDialogFragment() : DialogFragment() {
    private lateinit var binding: AddWorkoutBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Setup Dialog Activity
        return activity?.let {
            binding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()),
                R.layout.add_workout, null, false)

            // Build the dialog alert
            val builder: AlertDialog.Builder? = activity?.let {
                AlertDialog.Builder(it)
            }

            // Set the properties of the dialog alert
            builder
                ?.setCancelable(false)
                ?.setCustomTitle(View.inflate(context,
                    R.layout.add_workout_title, null))
                ?.setView(binding.root)
                ?.setPositiveButton(
                    "Submit"
                ) { dialog, _ ->
                    submitWorkout()
                    dialog?.dismiss()
                }
                ?.setNegativeButton(
                    "Cancel"
                ) { dialog, _ ->
                    dialog?.cancel()
                }

            builder?.show()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onStart() {
        super.onStart()
        (requireDialog() as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
        setupDataBinding()
    }

    private fun submitWorkout() {
        val viewModel = ViewModelProvider(this)[WorkoutViewModel::class.java]
        val workoutName = binding.workoutNameEditText.text.toString()

        viewModel.insert(Workout(null, workoutName, 0, resources.getColor(R.color.color1)))
    }

    private fun setupDataBinding() {
        binding.workoutNameEditText.addTextChangedListener {
            (requireDialog() as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = !it.isNullOrEmpty()
        }
    }
}