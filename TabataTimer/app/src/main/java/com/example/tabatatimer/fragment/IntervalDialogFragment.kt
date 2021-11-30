package com.example.tabatatimer.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tabatatimer.databinding.FragmentIntervalDialogBinding
import com.example.tabatatimer.data.Interval
import com.example.tabatatimer.data.Workout
import com.example.database.AppDatabase
import com.example.tabatatimer.R
import com.example.tabatatimer.viewmodels.IntervalViewModel
import com.example.tabatatimer.viewmodels.WorkoutViewModel

import kotlinx.android.synthetic.main.fragment_interval_list.*

class IntervalDialogFragment(private val fragment: Fragment) : DialogFragment() {
    private lateinit var binding: FragmentIntervalDialogBinding
    private var intervalValue: String = ""
    private lateinit var dialog: AlertDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Setup Dialog Activity
        return activity?.let {
            binding = DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.fragment_interval_dialog, null, false)

            // Build the dialog alert
            val builder: AlertDialog.Builder? = activity?.let {
                AlertDialog.Builder(it)
            }



            // Set the properties of the dialog alert
            builder
                ?.setCancelable(false)
                ?.setView(binding.root)
                ?.setPositiveButton(
                    "Submit"
                ) { dialog, _ ->
                    submitInterval()
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
        dialog = getDialog() as AlertDialog
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
        setupDataBinding()
    }

    private fun setupDataBinding() {
        binding.intervalTypeGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.prepareRadioButton)
            {
                intervalValue = Interval.IntervalType.Prepare.value
            }
            else if (checkedId == R.id.workRadioButton)
            {
                intervalValue = Interval.IntervalType.Work.value
            }
            else if (checkedId == R.id.restRadioButton)
            {
                intervalValue = Interval.IntervalType.Rest.value
            }
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = true
        }
    }

    private fun submitInterval() {
        val intervalName = ""
        val intervalReps = null
        val intervalTime = 20
        val workout = requireArguments().getSerializable("workout") as Workout
        val intervalIndex = requireArguments().getInt("newIndex")
        val workoutId = workout.workoutId!!

        val interval = Interval(
            null,
            intervalName,
            intervalValue,
            intervalTime,
            intervalReps,
            intervalIndex,
            workoutId
        )

        val appDatabase = AppDatabase.getInstance(context)

        val intervalViewModel = ViewModelProvider(this)[IntervalViewModel::class.java]
        val workoutViewModel = ViewModelProvider(this)[WorkoutViewModel::class.java]

        intervalViewModel.insert(interval)

        workout.length += intervalTime
        workoutViewModel.update(workout)

        fragment.intervalViewTotalTime.text = Interval.getIntervalDuration(workout.length)
    }

}