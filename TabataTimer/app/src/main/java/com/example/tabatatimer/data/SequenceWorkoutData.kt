package com.example.tabatatimer.data

class SequenceWorkoutData {
    var type // 1 is Sequence and 2 is workout
            = 0
    var sequence : SequenceWithWorkouts? = null
    var workout: Workout? = null

    companion object {
        fun merge(
            sequences: List<SequenceWithWorkouts>,
            workouts: List<Workout>
        ): List<SequenceWorkoutData> {
            val datas: MutableList<SequenceWorkoutData> = ArrayList()
            for (workout in workouts) {
                val data = SequenceWorkoutData()
                data.sequence = null
                data.workout = workout
                data.type = 2
                datas.add(data)
            }
            for (sequence in sequences) {
                val data = SequenceWorkoutData()
                data.sequence = sequence
                data.workout = null
                data.type = 1
                datas.add(data)
            }

            return datas
        }
    }
}