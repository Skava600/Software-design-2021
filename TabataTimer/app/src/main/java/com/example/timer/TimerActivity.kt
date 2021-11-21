package com.example.timer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tabatatimer.R
import kotlinx.android.synthetic.main.activity_timer.*

class TimerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.fragment_timer)
    }
}