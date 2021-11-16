package com.example.timer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.example.tabatatimer.R
import kotlinx.android.synthetic.main.activity_timer.*

class TimerActivity : AppCompatActivity() {
    class TimerActivity : AppCompatActivity() {

        private var mediaPlayer: MediaPlayer? = null
        private var buttonSwitch = false
        private var totaltime: Long = 10000 + 1000
        private val countdown: Long = 1000
        private var ctimer: CountDownTimer? = null

        fun playTimer() {

            ctimer = object : CountDownTimer(totaltime, countdown) {

                override fun onTick(millis: Long) {
                    totaltime = millis
                    var minutes = (millis / 1000) / 60
                    var seconds = (millis / 1000) % 60

                    var updateStringTimer = String.format("%02d:%02d", minutes, seconds)

                    timer_text.text = updateStringTimer

                }


                override fun onFinish() {
                    mediaPlayer?.start()
                }
            }
            ctimer?.start()

        }

        fun pause() {
            ctimer?.cancel()
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            setContentView(R.layout.activity_timer)

            val actionBar = supportActionBar
            actionBar!!.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)

            mediaPlayer = MediaPlayer.create(this, R.raw.song)

            workout_name.text = "Push ups" // placeholder code for setting text at startup
            reps_text.text = "# of reps 50" // placeholder code for setting text at startup

            timer_text.text = "00:10" // placeholder code for setting text at startup

            play_button.setOnClickListener {

                if (buttonSwitch == false) {
                    play_button.setBackgroundResource(R.drawable.ic_pause_24)
                    playTimer()
                    buttonSwitch = true
                } else {
                    play_button.setBackgroundResource(R.drawable.ic_play_24)
                    pause()
                    buttonSwitch = false
                }

            }

            forward_button.setOnClickListener {
                mediaPlayer?.stop()
                totaltime = 70000 + 1000
                workout_name.text =
                    "Curls" // placeholder code for setting text when hitting forward
                reps_text.text =
                    "# of reps 80" // placeholder code for setting text when hitting forward
                timer_text.text = "01:10" // placeholder code for setting text at startup
                play_button.setBackgroundResource(R.drawable.ic_play_24)
                pause()
                buttonSwitch = false
            }

            back_button.setOnClickListener {
                mediaPlayer?.stop()
                totaltime = 10000 + 1000
                workout_name.text =
                    "Push ups" // placeholder code for setting text when hitting back
                reps_text.text =
                    "# of reps 50" // placeholder code for setting text when hitting back
                timer_text.text = "00:10" // placeholder code for setting text at startup
                play_button.setBackgroundResource(R.drawable.ic_play_24)
                pause()
                buttonSwitch = false
            }

            end_button.setOnClickListener {
                mediaPlayer?.release()
                mediaPlayer = null
            }


            // val audioManager: AudioManager = getSystemService(AUDIO_SERVICE) as AudioManager // Audio manager in case we need it

        }

        override fun onSupportNavigateUp(): Boolean {
            //val navController = this.findNavController(R.id.myNavHostFragment)
            //navController.navigate(R.id.mainFragment)
            return false
        }
    }
}