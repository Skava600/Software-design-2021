package com.example.tabatatimer.fragment

import android.app.Activity
import com.example.tabatatimer.R
import com.example.tabatatimer.data.Workout
import com.example.tabatatimer.viewmodels.WorkoutViewModel
import petrov.kristiyan.colorpicker.ColorPicker

class ColorPickerFragment(
    context: Activity?,
    private val workout: Workout,
    private val viewModel: WorkoutViewModel) : ColorPicker(context) {

    fun showColorPicker()
    {
        this.setColors(R.array.default_colors).
            setColumns(5).
            setRoundColorButton(true).
            setOnFastChooseColorListener(object : OnFastChooseColorListener {
                override fun setOnFastChooseColorListener(position: Int, color: Int) {
                    workout.color = color
                    viewModel.update(workout)
                }

                override fun onCancel() {
                }


            })
            .setDefaultColorButton(workout.color)
            .show()
    }
}
